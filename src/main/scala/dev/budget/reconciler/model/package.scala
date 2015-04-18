package dev.budget.reconciler

import org.joda.time.LocalDate

package object model {
  case class DiffRangeResponse(
    earliestDate: LocalDate,
    latestDate: LocalDate)

  case class UploadResponse(numRecords: Int)
}


