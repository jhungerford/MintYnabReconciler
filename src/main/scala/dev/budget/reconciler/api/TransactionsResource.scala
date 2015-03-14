package dev.budget.reconciler.api

import java.io.{IOException, Reader, StringReader}
import javax.ws.rs._
import javax.ws.rs.core.{MediaType, Response}

import dev.budget.reconciler.csv.handler.{ChainTransactionHandler, CountTransactionHandler, ESTransactionHandler}
import dev.budget.reconciler.csv.{MintTransactionsReader, TransactionsReader, YnabTransactionsReader}
import dev.budget.reconciler.dao.ESTransactionDao
import dev.budget.reconciler.es.{ESIndex, ElasticSearchAdmin}
import dev.budget.reconciler.model.{MintTransaction, Transaction, UploadResponse, YnabTransaction}
import scaldi.{Injectable, Injector}

@Path("/v1/transactions")
class TransactionsResource(implicit val injector: Injector) extends Injectable {

  private val esAdmin: ElasticSearchAdmin = inject [ElasticSearchAdmin]
  private val esDao: ESTransactionDao = inject [ESTransactionDao]

  @PUT
  @Path("/mint")
  @Consumes(Array("text/csv"))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @throws(classOf[IOException])
  def uploadMint(mintTransactionsCsv: String): Response = {
    esAdmin.clearIndex(ESIndex.MINT)
//    val mintTransactions: List[MintTransaction] = transactionsToList(mintTransactionsCsv, new MintTransactionsReader)
//    val uploadedFuture: Future[Integer] = mintUploadService.apply(mintTransactions)
    val count: Int = writeTransactions(ESIndex.MINT, mintTransactionsCsv, new MintTransactionsReader)

    Response.ok(new UploadResponse(count)).build
  }

  @PUT
  @Path("/ynab")
  @Consumes(Array("text/csv"))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @throws(classOf[IOException])
  def uploadYnab(ynabTransactions: String): Response = {
    esAdmin.clearIndex(ESIndex.YNAB)
    val count: Int = writeTransactions(ESIndex.YNAB, ynabTransactions, new YnabTransactionsReader)
    val uploadResponse: UploadResponse = new UploadResponse(count)
    Response.ok(uploadResponse).build
  }

  @GET
  @Path("/diff")
  @Produces(Array(MediaType.APPLICATION_JSON))
  @throws(classOf[IOException])
  def getDiff: Response = {
    val mintTransactions: Seq[MintTransaction] = esDao.allThisMonth(ESIndex.MINT, classOf[MintTransaction])
    val ynabTransactions: Seq[YnabTransaction] = esDao.allThisMonth(ESIndex.YNAB, classOf[YnabTransaction])
    Response.noContent.build
  }

//  @throws(classOf[IOException])
//  private def transactionsToList[T <: Transaction](csv: String, transactionsReader: TransactionsReader[T]): List[T] = {
//    val listHandler: ListTransactionHandler[T] = new ListTransactionHandler[T]
//    try {
//      val in: Reader = new StringReader(csv)
//      try {
//        transactionsReader.read(in, listHandler)
//      } finally {
//        if (in != null) in.close()
//      }
//    }
//
//    listHandler.getTransactions
//  }

  @throws(classOf[IOException])
  private def writeTransactions[T <: Transaction](index: ESIndex, transactions: String, transactionsReader: TransactionsReader[T]): Int = {
    val esHandler: ESTransactionHandler[T] = new ESTransactionHandler[T](index, esDao)
    val countHandler: CountTransactionHandler[T] = new CountTransactionHandler[T]
    val transactionHandler: ChainTransactionHandler[T] = new ChainTransactionHandler[T](esHandler, countHandler)
    try {
      val in: Reader = new StringReader(transactions)
      try {
        transactionsReader.read(in, transactionHandler)
      } finally {
        if (in != null) in.close()
      }
    }

    countHandler.getCount
  }
}
