package com.vatbox.money

import java.math.MathContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

import com.vatbox.money.Generators.currencyArb

class CurrencySpec extends UnitSpec {
  "Currency" when {
    "Instances" should {
      "Be equal to itself" in {
        object TC1 extends Currency("TC", "TestCurrency 1", "%", 2, None, None, None)
        TC1 should equal (TC1)

        val tc2 = Currency("TC2", "TestCurrency 1", "%", 2)
        tc2 should equal (tc2)

        val tc3 = Currency("TC3")
        tc3 should equal (tc3)

        forAll { c: Currency ⇒
          c should equal (c)
        }
      }

      "Be equal to other instances for same type" in {
        object TC1 extends Currency("TC", "TestCurrency 1", "%", 2, None, None, None)
        val tc2 = Currency("TC", "TestCurrency 1", "%", 2)
        TC1 should equal (tc2)
      }

      "Be Equal to 'dynamic' created currency" in {
        object TC1 extends Currency("TC", "TestCurrency 1", "%", 2, None, None, None)
        Currency("TC") should equal (TC1)
      }

      "NOT be Equal when code is different" in {
        forAll { (c1: Currency, c2: Currency) ⇒
          whenever(c1.code != c2.code) {
            c1 shouldNot equal (c2)
          }
        }
      }

      "Have same hashcode for equal instances" in {
        object TC1 extends Currency("TC", "TestCurrency 1", "%", 2, None, None, None)
        TC1.## === Currency("TC", "TestCurrency 1", "%", 2).##
      }

      "Available in currencies list" in {
        object TC4 extends Currency("TC4", "TestCurrency 4", "%", 2, None, None, None)
        TC4 // defined and loaded
        Currency.getCurrencies should contain key ("TC4")
        Currency.getCurrencies("TC4") should be (TC4)

        forAll { c: Currency ⇒
          Currency.getCurrencies should contain key (c.code)
          Currency.getCurrencies(c.code) should be (c)
        }
      }
    }

    "converting to other currency" should {
      import java.time.Instant

      import scala.concurrent.Future

      object TC6 extends Currency("TC6", "TestCurrency 6", "%", 2, None, None, None)
      object TC7 extends Currency("TC7", "TestCurrency 7", "%", 2, None, None, None)

      val testDate = Instant.parse("2017-10-05T00:00:00.000Z")

      implicit object ExchangeRateDemo extends ExchangeRate {
        override def convert(base: Currency {type Key <: Currency.Key}, counter: Currency {type Key <: Currency.Key}, amount: BigDecimal, exchangeDate: Instant): Future[BigDecimal] = {
          (base, counter) match {
            case (TC6, TC7) if exchangeDate == testDate ⇒ Future.successful { 2.55 * amount }
            case (TC7, TC6) if exchangeDate == testDate ⇒ Future.successful { 1/2.55 * amount }
            case _ ⇒ Future.failed {
              ExchangeRateException(s"Fail to convert $base to $counter")
            }
          }
        }
      }

      "have the correct rate" in {
        val tcConvert = TC6 to TC7 at testDate
//        tcConvert.rate should be (2.55)
        whenReady(tcConvert.rate) { _ should be (2.55) }
      }

      "inverse have the correct rate" in {
        val tcConvert = TC6 to TC7 at testDate
        whenReady(tcConvert.inverse.rate) { _ should be (1/2.55) }

        val tcConvert2 = (TC6 to TC7 inverse) at testDate
        whenReady(tcConvert2.rate) { _ should be (1/2.55) }
      }

      "fail to convert fails" in {
        object TC8 extends Currency("TC8", "TestCurrency 8", "%", 2, None, None, None)
        val tcConvert = TC6 to TC8 at testDate
        assert(tcConvert.rate.failed.futureValue.isInstanceOf[ExchangeRateException])
      }

      "inverse inverse is the original" in {
        forAll { (c1: Currency{type Key <: Currency.Key}, c2: Currency{type Key <: Currency.Key}) ⇒
          whenever(c1.code != c2.code) {
            val c1Toc2 = c1 to c2

            c1Toc2.inverse.inverse should be(c1Toc2)

            val c1Toc2WithDate = c1 to c2 at testDate
            c1Toc2WithDate.inverse.inverse should be(c1Toc2WithDate)
          }
        }
      }

      "calculate convert amounts" in {
        val fakeRatio = BigDecimal(3.123, MathContext.UNLIMITED)
        implicit object ExchangeRateDemo extends ExchangeRate {
          override def convert(base: Currency {type Key <: Currency.Key}, counter: Currency {type Key <: Currency.Key}, amount: BigDecimal, exchangeDate: Instant): Future[BigDecimal] = {
            (base, counter) match {
              case (_, _)  ⇒ Future.successful { fakeRatio * amount }
            }
          }
        }
        forAll { (c1: Currency{type Key <: Currency.Key}, c2: Currency{type Key <: Currency.Key}, amount: BigDecimal) ⇒
          whenever(c1.code != c2.code) {
            val c1Toc2 = c1 to c2 at testDate
            val expected = Money(fakeRatio * amount, c2)

            whenReady(c1Toc2 convert amount) { _ should be (expected) }
          }
        }
      }

      "calculate convert money" in {
        val fakeRatio = BigDecimal(3.123, MathContext.UNLIMITED)
        implicit object ExchangeRateDemo extends ExchangeRate {
          override def convert(base: Currency {type Key <: Currency.Key}, counter: Currency {type Key <: Currency.Key}, amount: BigDecimal, exchangeDate: Instant): Future[BigDecimal] = {
            (base, counter) match {
              case (_, _)  ⇒ Future.successful { fakeRatio * amount }
            }
          }
        }
        forAll { (c1: Currency{type Key <: Currency.Key}, c2: Currency{type Key <: Currency.Key}, amount: BigDecimal) ⇒
          whenever(c1.code != c2.code) {
            val c1Toc2 = c1 to c2 at testDate
            val sourceMoney: Money[c1.Key] = Money(amount, c1)
            val expected = Money(fakeRatio * amount, c2)

            whenReady(c1Toc2 convert sourceMoney) { _ should be (expected) }
          }
        }
      }
    }
  }
}
