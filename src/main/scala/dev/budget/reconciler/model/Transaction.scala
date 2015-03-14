package dev.budget.reconciler.model

import org.elasticsearch.common.xcontent.XContentBuilder

trait Transaction {
  def esJson: Option[XContentBuilder]
}
