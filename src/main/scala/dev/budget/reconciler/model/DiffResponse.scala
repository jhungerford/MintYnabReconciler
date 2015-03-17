package dev.budget.reconciler.model

import org.joda.time.LocalDate

case class DiffResponse(
  earliestDate: LocalDate,
  latestDate: LocalDate)
