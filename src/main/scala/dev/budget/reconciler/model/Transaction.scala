package dev.budget.reconciler.model

import java.io.IOException

import dev.budget.reconciler.util.DateUtil
import org.elasticsearch.common.xcontent.{XContentFactory, XContentBuilder}
import org.joda.time.LocalDate
import org.slf4j.Logger
import org.slf4j.LoggerFactory._

sealed trait Transaction {
  def id: Option[String]
  def esJson: Option[XContentBuilder]
}

case class YnabTransaction(
                            account: String,
                            date: LocalDate,
                            payee: String,
                            masterCategory: String,
                            subCategory: String,
                            outflowCents: Long,
                            inflowCents: Long,
                            override val id: Option[String] = None) extends Transaction {

  private val log: Logger = getLogger(getClass)

  lazy val amountCents: Long = if (outflowCents > 0) outflowCents else inflowCents

  override def esJson: Option[XContentBuilder] = {
    try {
      Some(XContentFactory.jsonBuilder()
        .startObject
        .field("account", account)
        .field("date", DateUtil.format(date))
        .field("payee", payee)
        .field("masterCategory", masterCategory)
        .field("subCategory", subCategory)
        .field("outflowCents", outflowCents)
        .field("inflowCents", inflowCents)
        .endObject
      )
    } catch {
      case e: IOException =>
        log.error(s"Error converting ynab transaction $this to json")
        None
    }
  }
}

case class MintTransaction(
                            date: LocalDate,
                            description: String,
                            originalDescription: String,
                            amountCents: Long,
                            transactionType: String,
                            category: String,
                            account: String,
                            override val id: Option[String] = None) extends Transaction {

  private val log: Logger = getLogger(getClass)

  override def esJson: Option[XContentBuilder] = {
    try {
      Some(XContentFactory.jsonBuilder()
        .startObject
        .field("date", DateUtil.format(date))
        .field("description", description)
        .field("originalDescription", originalDescription)
        .field("amountCents", amountCents)
        .field("transactionType", transactionType)
        .field("category", category)
        .field("account", account)
        .endObject
      )
    } catch {
      case e: IOException =>
        log.error(s"Error converting transaction $this to json")
        None
    }
  }
}
