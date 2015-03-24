package dev.budget.reconciler.model

import org.elasticsearch.common.xcontent.XContentBuilder

trait Transaction {
  def id: Option[String]
  def esJson: Option[XContentBuilder]
}
