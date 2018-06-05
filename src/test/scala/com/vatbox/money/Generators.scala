package com.vatbox.money

import java.time.LocalDate

import org.scalacheck.{Arbitrary, Gen}

object Generators {

  def currencyFactory(c: String, n: String, s: String, f: Int) = Currency(c,n,s,f)
  implicit val currencyArb: Arbitrary[Currency {type Key <: Currency.Key}] = Arbitrary(Gen.resultOf(currencyFactory(_,_,_,_)))

  def moneyFactory(a: BigDecimal, c:  Currency {type Key <: Currency.Key}) = Money(a, c)
  implicit val moneyArb: Arbitrary[Money[_ <: Currency.Key]] = Arbitrary(Gen.resultOf(moneyFactory(_,_)))

//  def localDateFactory(year: Int, month: Int, dayOfMonth: Int) = LocalDate.of(year, month, dayOfMonth)
  def localDateGen: Gen[LocalDate] = {
    val rangeStart = LocalDate.of(1800, 1, 1).toEpochDay
    val rangeEnd = LocalDate.of(2500, 1, 1).toEpochDay
    Gen.choose(rangeStart, rangeEnd).map(i => LocalDate.ofEpochDay(i))
  }
  implicit val localDateAtb: Arbitrary[LocalDate] = Arbitrary(localDateGen)
}
