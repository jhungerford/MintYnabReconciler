package dev.budget.reconciler.model

import dev.budget.reconciler.model.TransactionDifferenceType.TransactionDifferenceType

case class TransactionDifference(
  mintId: String,
  mintDate: String,
  mintTransaction: String,
  mintCents: Long,
  ynabId: String,
  ynabDate: String,
  ynabTransaction: String,
  ynabCents: Long,
  differenceType: TransactionDifferenceType)
