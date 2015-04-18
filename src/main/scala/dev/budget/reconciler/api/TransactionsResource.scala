package dev.budget.reconciler.api

import java.io.{IOException, Reader, StringReader}
import javax.ws.rs._
import javax.ws.rs.core.{MediaType, Response}

import com.github.tototoshi.csv.CSVReader
import dev.budget.reconciler.dao.ESTransactionDao
import dev.budget.reconciler.es.{ESIndex, ElasticSearchAdmin, MintESIndex, YnabESIndex}
import dev.budget.reconciler.model._
import dev.budget.reconciler.util.DateUtil
import org.joda.time.LocalDate
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import scaldi.{Injectable, Injector}

import scala.util.matching.Regex

@Path("/v1/transactions")
@Produces(Array(MediaType.APPLICATION_JSON))
class TransactionsResource(implicit val injector: Injector) extends Injectable {
  private val log: Logger = getLogger(getClass)

  private val esAdmin: ElasticSearchAdmin = inject [ElasticSearchAdmin]
  private val esDao: ESTransactionDao = inject [ESTransactionDao]

  @PUT
  @Path("/mint")
  @Consumes(Array("text/csv"))
  def uploadMint(mintTransactionsCsv: String): UploadResponse = {
    esAdmin.clearIndex(MintESIndex)

    val in: Reader = new StringReader(mintTransactionsCsv)
    val csvReader: CSVReader = CSVReader.open(in)

    val transactions: Seq[MintTransaction] = parseCsv(csvReader) { row: Map[String, String] =>
      for {
        date <- parseDate(row.get("Date"))
        desc <- row.get("Description")
        origDesc <- row.get("Original Description")
        amountCents <- parseCents(row.get("Amount"))
        transactionType <- row.get("Transaction Type")
        category <- row.get("Category")
        account <- row.get("Account Name")
      } yield new MintTransaction(date, desc, origDesc, amountCents, transactionType, category, account)
    }

    val successCount = indexTransactions(MintESIndex, transactions)

    csvReader.close()

    new UploadResponse(successCount)
  }

  @PUT
  @Path("/ynab")
  @Consumes(Array("text/csv"))
  def uploadYnab(ynabTransactionsCsv: String): UploadResponse = {
    esAdmin.clearIndex(YnabESIndex)

    val in: Reader = new StringReader(ynabTransactionsCsv)
    val csvReader: CSVReader = CSVReader.open(in)

    val transactions: Seq[YnabTransaction] = parseCsv(csvReader) { row: Map[String, String] =>
      for {
        account <- row.get("Account")
        date <- parseDate(row.get("Date"))
        payee <- row.get("Payee")
        masterCategory <- row.get("Master Category")
        subCategory <- row.get("Sub Category")

        outflowCents <- parseCents(row.get("Outflow"))
        inflowCents <- parseCents(row.get("Inflow"))

      } yield new YnabTransaction(account, date, payee, masterCategory, subCategory, outflowCents, inflowCents)
    }

    val successCount = indexTransactions(YnabESIndex, transactions)

    csvReader.close()

    new UploadResponse(successCount)
  }

  @GET
  @Path("/diff/range")
  def getDiffRange(): DiffRangeResponse = {
    val mintRange: (LocalDate, LocalDate) = esDao.dateRange(MintESIndex)
    val ynabRange: (LocalDate, LocalDate) = esDao.dateRange(YnabESIndex)

    val earliestDate = if (mintRange._1.isBefore(ynabRange._1)) mintRange._1 else ynabRange._1
    val latestDate = if (mintRange._2.isAfter(ynabRange._2)) mintRange._2 else ynabRange._2

    new DiffRangeResponse(earliestDate, latestDate)
  }

  @GET
  @Path("/diff/{year}/{month}")
  def getDiff(@PathParam("year") year: Int, @PathParam("month") month: Int): DiffResponse = {
    val mintTransactions: Seq[MintTransaction] = esDao.allForMonth[MintTransaction](MintESIndex, year, month)
    val ynabTransactions: Seq[YnabTransaction] = esDao.allForMonth[YnabTransaction](YnabESIndex, year, month)

    val ynabMatches: Seq[(YnabTransaction, Seq[MintTransaction])] = ynabTransactions.map{ ynab =>
      (ynab, esDao.closestMintTransactions(ynab))
    }

    val diffWithoutMintOnly = ynabMatches.foldLeft(DiffResponse()) { (diff, ynabMatch) =>
      ynabMatch match {
        // Ynab Only
        case (ynab, mints) if mints.isEmpty =>
          diff.copy(ynabOnly = diff.ynabOnly :+ YnabAsDiff(ynab))

         // Correct
        case (ynab, mint :: _) if exactMatch(ynab, mint) =>
          val ynabDiff = YnabAsDiff(ynab)
          val mintDiff = MintAsDiff(mint)
          diff.copy(correct = diff.correct :+ CorrectDiffResponse(ynabDiff, mintDiff))

        // Incorrect
        case (ynab, mints) =>
          val ynabDiff = YnabAsDiff(ynab)
          val mintsDiff: Array[MintDiffResponse] = mints.map( MintAsDiff(_) ).toArray
          diff.copy(incorrect = diff.incorrect :+ IncorrectDiffResponse(ynabDiff, mintsDiff))
      }
    }

    val correctMintIds: Set[String] = diffWithoutMintOnly.correct.flatMap( _.mint.id ).toSet
    // TODO: how to handle incorrect ids?  Currently, mint transactions show up in mint only and incorrect.

    val mintOnly = mintTransactions.flatMap{
      case mint if correctMintIds.contains(mint.id.get) => None
      case mint => Some(MintAsDiff(mint))
    }.toArray

    diffWithoutMintOnly.copy(mintOnly = mintOnly)
  }

  private def exactMatch(ynab: YnabTransaction, mint: MintTransaction): Boolean = {
    ynab.amountCents == mint.amountCents // TODO: include description? category? date?
  }

  private def indexTransactions[T <: Transaction](esIndex: ESIndex, transactions: Seq[T]): Int = {
    transactions.map {
      transaction => esDao.index(esIndex, transaction)
    }.foldLeft(0) { (totalSuccesses, isIndexSuccessful) => isIndexSuccessful match {
      case true => totalSuccesses + 1
      case false => totalSuccesses
    }}
  }

  private def parseCsv[T <: Transaction](csvReader: CSVReader)(f: Map[String, String] => Option[T]): Seq[T] = {
    csvReader.iteratorWithHeaders.flatMap(row => f(row)).toSeq
  }

  private val dateFormatter: DateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy")
  private [TransactionsResource] def parseDate(maybeStr: Option[String]): Option[LocalDate] = {
    maybeStr match {
      case Some(str) =>
        try {
          Some(LocalDate.parse(str, dateFormatter))
        } catch {
          case e: IllegalArgumentException =>
            log.warn(s"Can't parse date $str", e)
            None
        }
      case None => None
    }
  }

  private [TransactionsResource] def parseCents(maybeStr: Option[String]): Option[Long] = {
    maybeStr match {
      case Some(str) =>
        val money: Regex = "^[$]?(\\d+)[.](\\d{2})".r
        str match {
          case money(dollars, cents) => Some(dollars.toLong * 100 + cents.toLong)
          case _ =>
            log.warn(s"Can't extract cents from $str")
            None
        }
      case None => None
    }
  }
}
