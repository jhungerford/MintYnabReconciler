package dev.budget.reconciler.model

import org.joda.time.LocalDate

case class DiffRangeResponse(
  earliestDate: LocalDate,
  latestDate: LocalDate)
