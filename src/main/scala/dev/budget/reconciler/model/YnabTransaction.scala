package dev.budget.reconciler.model

import java.io.IOException

import org.elasticsearch.common.xcontent.{XContentFactory, XContentBuilder}
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

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

  override def esJson: Option[XContentBuilder] = {
    val dateStr: String = DateTimeFormat.forPattern("yyyy-MM-dd").print(date)

    try {
      Some(XContentFactory.jsonBuilder()
        .startObject
          .field("account", account)
          .field("date", dateStr)
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
