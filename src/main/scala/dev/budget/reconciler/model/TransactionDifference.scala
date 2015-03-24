package dev.budget.reconciler.model

import dev.budget.reconciler.model.TransactionDifferenceType.TransactionDifferenceType

case class TransactionDifference(
  mintDate: String,
  mintTransaction: String,
  mintCents: Int,
  ynabDate: String,
  ynabTransaction: String,
  ynabCents: Int,
  differenceType: TransactionDifferenceType)
