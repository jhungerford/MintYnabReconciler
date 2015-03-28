package dev.budget.reconciler.util

import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

object DateUtil {
  private val formatter = DateTimeFormat.forPattern("yyyy-MM-dd")

  def parse(str: String): LocalDate = formatter.parseLocalDate(str)

  def format(date: LocalDate): String = formatter.print(date)
}
