package dev.budget.reconciler.model

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.module.scala.JsonScalaEnumeration
import dev.budget.reconciler.model.TransactionDifferenceType.TransactionDifferenceType
import org.joda.time.LocalDate

class TransactionDifferenceTypeHolder extends TypeReference[TransactionDifferenceType.type]

case class TransactionDifference(
  mintId: String,
  mintDate: LocalDate,
  mintTransaction: String,
  mintCents: Long,
  ynabId: String,
  ynabDate: LocalDate,
  ynabTransaction: String,
  ynabCents: Long,
  @JsonScalaEnumeration(classOf[TransactionDifferenceTypeHolder])
  differenceType: TransactionDifferenceType)
