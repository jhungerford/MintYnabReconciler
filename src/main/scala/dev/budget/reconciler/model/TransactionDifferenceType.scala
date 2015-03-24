package dev.budget.reconciler.model

object TransactionDifferenceType extends Enumeration {
  type TransactionDifferenceType = Value
  val Correct, Incorrect, MintOnly, YnabOnly = Value
}
