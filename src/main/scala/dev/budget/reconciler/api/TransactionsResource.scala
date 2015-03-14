package dev.budget.reconciler.api

import java.io.{IOException, Reader, StringReader}
import javax.ws.rs._
import javax.ws.rs.core.{MediaType, Response}

import com.github.tototoshi.csv.CSVReader
import dev.budget.reconciler.dao.ESTransactionDao
import dev.budget.reconciler.es.{ESIndex, ElasticSearchAdmin, MintESIndex, YnabESIndex}
import dev.budget.reconciler.model.{MintTransaction, Transaction, UploadResponse, YnabTransaction}
import org.joda.time.LocalDate
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import scaldi.{Injectable, Injector}

import scala.util.matching.Regex

@Path("/v1/transactions")
class TransactionsResource(implicit val injector: Injector) extends Injectable {
  private val log: Logger = getLogger(getClass)

  private val esAdmin: ElasticSearchAdmin = inject [ElasticSearchAdmin]
  private val esDao: ESTransactionDao = inject [ESTransactionDao]

  @PUT
  @Path("/mint")
  @Consumes(Array("text/csv"))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @throws(classOf[IOException])
  def uploadMint(mintTransactionsCsv: String): Response = {
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

    Response.ok(new UploadResponse(successCount)).build
  }

  @PUT
  @Path("/ynab")
  @Consumes(Array("text/csv"))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @throws(classOf[IOException])
  def uploadYnab(ynabTransactionsCsv: String): Response = {
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

    Response.ok(new UploadResponse(successCount)).build
  }

  @GET
  @Path("/diff")
  @Produces(Array(MediaType.APPLICATION_JSON))
  @throws(classOf[IOException])
  def getDiff: Response = {
    val mintTransactions: Seq[MintTransaction] = esDao.allThisMonth(MintESIndex, classOf[MintTransaction])
    val ynabTransactions: Seq[YnabTransaction] = esDao.allThisMonth(YnabESIndex, classOf[YnabTransaction])
    Response.noContent.build
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
