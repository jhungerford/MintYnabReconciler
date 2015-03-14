package dev.budget.reconciler.model

case class TransactionDifference(
  mintDate: String,
  mintTransaction: String,
  mintCents: Int,
  ynabDate: String,
  ynabTransaction: String,
  ynabCents: Int)
