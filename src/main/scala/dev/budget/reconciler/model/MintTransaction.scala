package dev.budget.reconciler.model

import java.io.IOException

import org.elasticsearch.common.xcontent.{XContentBuilder, XContentFactory}
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.slf4j.Logger

case class MintTransaction(
  date: LocalDate,
  description: String,
  originalDescription: String,
  amountCents: Long,
  transactionType: String,
  category: String,
  account: String) extends Transaction {

  private val log: Logger = org.slf4j.LoggerFactory.getLogger(getClass)

  override def esJson: Option[XContentBuilder] = {
    val dateStr: String = DateTimeFormat.forPattern("yyyy-MM-dd").print(date)

    try {
      Some(XContentFactory.jsonBuilder()
        .startObject
          .field("date", dateStr)
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
