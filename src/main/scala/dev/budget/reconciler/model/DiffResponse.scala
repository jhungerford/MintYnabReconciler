package dev.budget.reconciler.model

import org.joda.time.LocalDate

case class DiffResponse(mintOnly: Array[MintDiffResponse] = Array.empty,
                         ynabOnly: Array[YnabDiffResponse] = Array.empty,
                         incorrect: Array[IncorrectDiffResponse] = Array.empty,
                         correct: Array[CorrectDiffResponse] = Array.empty)

case class MintDiffResponse(id: Option[String],
                             date: LocalDate,
                             description: String,
                             cents: Long,
                             transactionType: String,
                             account: String)

case class YnabDiffResponse(id: Option[String],
                             date: LocalDate,
                             payee: String,
                             cents: Long)

case class IncorrectDiffResponse(ynab: YnabDiffResponse,
                                  closestMints: Seq[MintDiffResponse])

case class CorrectDiffResponse(ynab: YnabDiffResponse,
                                mint: MintDiffResponse)

object YnabAsDiff {
  def apply(ynab: YnabTransaction): YnabDiffResponse = {
    YnabDiffResponse(
      ynab.id,
      ynab.date,
      ynab.payee,
      ynab.amountCents
    )
  }
}

object MintAsDiff {
  def apply(mint: MintTransaction): MintDiffResponse = {
    MintDiffResponse(
      mint.id,
      mint.date,
      mint.description,
      mint.amountCents,
      mint.transactionType,
      mint.account
    )
  }
}
