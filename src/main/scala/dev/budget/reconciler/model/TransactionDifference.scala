package dev.budget.reconciler.model

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.module.scala.JsonScalaEnumeration
import dev.budget.reconciler.model.TransactionDifferenceType.TransactionDifferenceType

class TransactionDifferenceTypeHolder extends TypeReference[TransactionDifferenceType.type]

case class TransactionDifference(
  mintId: String,
  mintDate: String,
  mintTransaction: String,
  mintCents: Long,
  ynabId: String,
  ynabDate: String,
  ynabTransaction: String,
  ynabCents: Long,
  @JsonScalaEnumeration(classOf[TransactionDifferenceTypeHolder])
  differenceType: TransactionDifferenceType)
