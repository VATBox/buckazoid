package com.vatbox.money

import java.time.{Instant, LocalDate, ZoneOffset}


object DateTimeUtils {
  implicit class LocalDateToInstance(val d: LocalDate) extends AnyVal {
    def toInstance: Instant = d.atStartOfDay().toInstant(ZoneOffset.UTC)
  }

  def now: Instant = Instant.now
}
