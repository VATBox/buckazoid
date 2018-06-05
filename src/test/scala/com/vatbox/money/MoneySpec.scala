package com.vatbox.money

import java.time.Instant

import scala.concurrent.ExecutionContext.Implicits.global

import com.vatbox.money.Generators._

class MoneySpec extends UnitSpec {
  "Money" when {
    "Instances" should {
      "Be equal to itself" in {
        val $100 = USD(100)
        $100 should equal($100)

        val $50 = 50 (USD)
        $50 should equal($50)

        val $20 = Money(20, USD)
        $20 should equal($20)

        val $10 = Money(10, "USD")
        $10 should equal($10)

        forAll { c: Money[_ <: Currency.Key] ⇒
          c should equal(c)
        }
      }

      "Be equal to other instances for same type" in {
        val m1 = Money(20, USD)
        val m2 = Money(20, USD)
        m1 should equal(m2)
      }

      "Be Equal to 'parsed' created money" in {
        val m1 = Money(100, USD)
        Money.parse("100 USD").get should equal(m1)
        Money.parse("$100").get should equal(m1)
      }

      "Be NOT Equal when currency is different" in {
        forAll { (c1: Currency {type Key <: Currency.Key}, c2: Currency {type Key <: Currency.Key}, amount: BigDecimal) ⇒
          whenever(c1.code != c2.code) {
            val m1 = Money(amount, c1)
            val m2 = Money(amount, c2)
            m1 shouldNot equal(m2)
          }
        }
      }

      "Be created with default currency"  in {
        Money.apply(1) should equal(Money(1, USD))
      }

      "Be NOT Equal when amount is different" in {
        forAll { (c1: Currency {type Key <: Currency.Key}, amount1: BigDecimal, amount2: BigDecimal) ⇒
          whenever(amount1 != amount2) {
            val m1 = Money(amount1, c1)
            val m2 = Money(amount2, c1)
            m1 shouldNot equal(m2)
          }
        }
      }

      "Have same hashcode for equal instances" in {
        val m1 = Money(100, USD)
        m1.## === Money.parse("$100").get.##
      }

      "Have right order for the same currency" in {
        forAll { (c1: Currency {type Key <: Currency.Key}, amount1: BigDecimal, amount2: BigDecimal) ⇒
          val m1 = Money(amount1, c1)
          val m2 = Money(amount2, c1)
          (m1 > m2) should equal(amount1 > amount2)
          (m1 == m2) should equal(amount1 == amount2)
        }
      }

      "Fail for invalid parse string" in {
        Money.parse("100 11").failure.exception should have message "Unable to parse Money"
      }
      "Fail for invalid currency in parse string" in {
        Money.parse("100 BLA").failure.exception should have message "Currency not found"
      }

    }

    "value equality" should {
      "equal for same currency" in {
        forAll { (c1: Currency {type Key <: Currency.Key}, amount1: BigDecimal, amount2: BigDecimal) ⇒
          val m1 = Money(amount1, c1)
          val m2 = Money(amount2, c1)
          (m1 === m2) should equal(amount1 == amount2)
        }
      }
      "equal for different money" when {
        "converting to same currency" in {
          forAll { (m1: Money[_ <: Currency.Key], m2: Money[_ <: Currency.Key]) ⇒
            whenever(m1.currency != m2.currency) {
              whenReady(m1 === m2 at Instant.now) {
                _ should equal (m1.amount == fakeRatio*m2.amount)//(Money(m1.amount+(fakeRatio*m2.amount), m1.currency))
              }
            }
          }
        }
        "used in math operations" in {
          forAll { (m1: Money[_ <: Currency.Key], m2: Money[_ <: Currency.Key]) ⇒
            whenever(m1.currency != m2.currency) {
              val expected = Money(m1.amount+(fakeRatio*m2.amount), m1.currency)
              whenReady(m1 + m2 === expected at Instant.now) {
                _ should equal (true)
              }
            }
          }
        }
      }
    }

    "converting to other money" should {
      import java.time.Instant

      import scala.concurrent.ExecutionContext.Implicits.global
      import scala.concurrent.Future

      val testDate = Instant.parse("2017-10-05T00:00:00.000Z")

      "calculate convert money" in {
        forAll { (m1: Money[_ <: Currency.Key], c1: Currency {type Key <: Currency.Key}, ratio: BigDecimal) ⇒
          whenever(m1.currency != c1) {
            implicit object ExchangeRateDemo extends ExchangeRate {
              override def convert(base: Currency {type Key <: Currency.Key}, counter: Currency {type Key <: Currency.Key}, amount: BigDecimal, exchangeDate: Instant): Future[BigDecimal] = {
                (base, counter) match {
                  case (_, _) ⇒ Future.successful {
                    ratio * amount
                  }
                }
              }
            }
            val m1Inc2 = m1 in c1 at testDate
            val expected = Money(ratio * m1.amount, c1)

            whenReady(m1Inc2) {
              _ should be(expected)
            }
          }
        }
      }
    }

    "Money math" should {
      "Add Money with same currency" in {
        forAll { (c1: Currency {type Key <: Currency.Key}, amount1: BigDecimal, amount2: BigDecimal, amount3: BigDecimal) ⇒
          val m1 = Money(amount1, c1)
          val m2 = Money(amount2, c1)
          val m3 = Money(amount3, c1)
          val m4 = Money(amount1 + amount2 + amount3, c1)
          m1 + m2 + m3 should equal(m4)
        }
      }
      "Subtract Money with same currency" in {
        forAll { (c1: Currency {type Key <: Currency.Key}, amount1: BigDecimal, amount2: BigDecimal, amount3: BigDecimal) ⇒
          val m1 = Money(amount1, c1)
          val m2 = Money(amount2, c1)
          val m3 = Money(amount3, c1)
          val m4 = Money(amount1 - amount2 - amount3, c1)
          m1 - m2 - m3 should equal(m4)
        }
      }
      "fail compile adding money to scalar" in {
        "22 + USD(100)" shouldNot compile
        "USD(100) + 22" shouldNot compile
      }
      "fail compile subtracting money to scalar" in {
        "22 - USD(100)" shouldNot compile
        "USD(100) - 22" shouldNot compile
      }

      "Multiple money with scalar" in {
        forAll { (m1: Money[_ <: Currency.Key], scalar: BigDecimal) ⇒
          (m1 * scalar).amount should equal(m1.amount * scalar)
          (m1 * scalar).currency should equal(m1.currency)
          m1 * scalar should equal(scalar * m1)
        }
      }
      "Dividing money with scalar" in {
        forAll { (m1: Money[_ <: Currency.Key], scalar: BigDecimal) ⇒
          whenever(scalar != BigDecimal(0)) { //TODO: Checkout why BigDecimal("0E+19") != 0
            (m1 / scalar).amount should equal(m1.amount / scalar)
//            (m1 / scalar).currency should equal(m1.currency)
          }
        }
      }
      "fail compile dividing scalar with money" in  {
        "22 / USD(100)" shouldNot compile
      }
      "fail compile multiple money with money" in {
        "USD(100) * USD(100)" shouldNot compile
        "USD(100) * ILS(10)" shouldNot compile
      }
      "fail compile dividing money with money" in {
        "USD(100) / USD(100)" shouldNot compile
        "USD(100) / ILS(10)" shouldNot compile
      }

      "money with different currency" should {

        "be converted when adding money with different currency" in {
          forAll { (m1: Money[_ <: Currency.Key], m2: Money[_ <: Currency.Key]) ⇒
            whenever(m1.currency != m2.currency) {
              whenReady(m1 + m2 at Instant.now) {
                _ should equal(Money(m1.amount+(fakeRatio*m2.amount), m1.currency))
              }
            }
          }
          forAll { (m1: Money[_ <: Currency.Key], m2: Money[_ <: Currency.Key], m3: Money[_ <: Currency.Key]) ⇒
            whenever(m1.currency != m2.currency && m1.currency != m3.currency) {
              whenReady(m1 + m2 + m3 at Instant.now) {
                _ should equal(Money(m1.amount+(fakeRatio*m2.amount)+(fakeRatio*m3.amount), m1.currency))
              }
            }
          }
        }
        "be converted when subtracting money with different currency" in {
          forAll { (m1: Money[_ <: Currency.Key], m2: Money[_ <: Currency.Key]) ⇒
            whenever(m1.currency != m2.currency) {
              whenReady(m1 - m2 at Instant.now) {
                _ should equal(Money(m1.amount-(fakeRatio*m2.amount), m1.currency))
              }
            }
          }
          forAll { (m1: Money[_ <: Currency.Key], m2: Money[_ <: Currency.Key], m3: Money[_ <: Currency.Key]) ⇒
            whenever(m1.currency != m2.currency && m1.currency != m3.currency) {
              whenReady(m1 - m2 - m3 at Instant.now) {
                _ should equal(Money(m1.amount-(fakeRatio*m2.amount)-(fakeRatio*m3.amount), m1.currency))
              }
            }
          }
        }
      }

      "abs should return possitive values" in {
        forAll { (m1: Money[_ <: Currency.Key]) ⇒
          m1.abs.amount >= 0
        }
      }

      "min should return the low amount" in {
        forAll { (c1: Currency {type Key <: Currency.Key}, amount1: BigDecimal, amount2: BigDecimal) ⇒
          val m1 = Money(amount1, c1)
          val m2 = Money(amount2, c1)
          (m1 min m2).amount should equal (m1.amount min m2.amount)
        }
      }
      "max should return the high amount" in {
        forAll { (c1: Currency {type Key <: Currency.Key}, amount1: BigDecimal, amount2: BigDecimal) ⇒
          val m1 = Money(amount1, c1)
          val m2 = Money(amount2, c1)
          (m1 max m2).amount should equal (m1.amount max m2.amount)
        }
      }
      "signum should return the sign of the value" in {
        forAll { (m1: Money[_ <: Currency.Key]) ⇒
          if (m1.amount == 0) m1.signum == 0
          else if (m1.amount > 0) 1 else -1
        }
      }
    }

//    "scaled with default scale" should {
//      ""
//    }
  }
}
