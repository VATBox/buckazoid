package com.vatbox.money

import java.time.Instant
import scala.concurrent.ExecutionContext.Implicits.global

import com.vatbox.money.Generators._


class DslSpec extends UnitSpec {
  "MoneyExchange" should {
    "adding money" in {
      forAll { (m1: Money[_ <: Currency.Key], m2: Money[_ <: Currency.Key], m3: Money[_ <: Currency.Key], m4: Money[_ <: Currency.Key]) ⇒
        whenever(m1.currency != m2.currency && m1.currency != m3.currency && m1.currency != m4.currency) {
          val me1 = m1 + m2
          val me2 = m3 + m4
          whenReady(me1 + me2 at Instant.now) {
            _ should equal(Money(m1.amount + (fakeRatio * m2.amount) + (fakeRatio * m3.amount) + (fakeRatio * m4.amount), m1.currency))
          }
        }
      }
    }

    "substracting money" in {
      forAll { (m1: Money[_ <: Currency.Key], m2: Money[_ <: Currency.Key], m3: Money[_ <: Currency.Key], m4: Money[_ <: Currency.Key]) ⇒
        whenever(m1.currency != m2.currency && m1.currency != m3.currency && m1.currency != m4.currency) {
          val me1 = m1 + m2
          val me2 = m3 + m4
          whenReady(me1 - me2 at Instant.now) {
            _ should equal(Money(m1.amount + (fakeRatio * m2.amount) - (fakeRatio * m3.amount) - (fakeRatio * m4.amount), m1.currency))
          }
        }
      }
    }

    "added to Money" in {
      forAll { (m1: Money[_ <: Currency.Key], m2: Money[_ <: Currency.Key], m3: Money[_ <: Currency.Key]) ⇒
        whenever(m1.currency != m2.currency && m1.currency != m3.currency) {
          val me = m2 + m3
          whenReady(m1 + me at Instant.now) {
            _ should equal(Money(m1.amount + (fakeRatio * m2.amount) + (fakeRatio * m3.amount), m1.currency))
          }
        }
      }
    }
    "substracted from Money" in {
      forAll { (m1: Money[_ <: Currency.Key], m2: Money[_ <: Currency.Key], m3: Money[_ <: Currency.Key]) ⇒
        whenever(m1.currency != m2.currency && m1.currency != m3.currency) {
          val me = m2 + m3
          whenReady(m1 - me at Instant.now) {
            _ should equal(Money(m1.amount - (fakeRatio * m2.amount) - (fakeRatio * m3.amount), m1.currency))
          }
        }
      }
    }

    "allow convert to other currency" in {
      forAll { (m1: Money[_ <: Currency.Key], m2: Money[_ <: Currency.Key], c1: Currency {type Key <: Currency.Key}) ⇒
        whenever(m1.currency != m2.currency && m1.currency != c1) {
          val me1 = m1 + m2
          whenReady(me1 in c1 at Instant.now) {
            _ should equal(Money((fakeRatio * m1.amount) + (fakeRatio * m2.amount), c1))
          }
        }
      }
    }

    "change sign for unary minus" in {
      forAll { (m1: Money[_ <: Currency.Key], m2: Money[_ <: Currency.Key]) ⇒
        val me = m1 + m2
        val meNeg = -me
        whenReady(me at Instant.now) { meRes ⇒
          whenReady(meNeg at Instant.now) { meNegRes ⇒
            ((meRes * -1)) should be (meNegRes * 1) // meNegRes * 1 to be align with precision
          }
        }
        whenReady((me + meNeg) at Instant.now) { _ should be (Money(0, m1.currency)) }
      }
    }
  }

  "CurrencyExchange" should {
    "convert amount between currencies" in {
      forAll { (c1: Currency {type Key <: Currency.Key}, c2: Currency {type Key <: Currency.Key}, amount: BigDecimal) ⇒
        whenever(c1 != c2) {
          val ce = c1 to c2
          whenReady(ce convert (amount) at Instant.now) {
            _ should equal(Money((fakeRatio * amount), c2))
          }
        }
      }
    }
  }

  "MoneyTolerance" should {
    "ignore amount different within the tolerance" when {
      "check for equal" in {
        forAll { (c1: Currency {type Key <: Currency.Key}, amount1: BigDecimal, amount2: BigDecimal, tolerance : BigDecimal) ⇒
          val m1 = Money(amount1, c1)
          val m2 = Money(amount2, c1)
          (m1 +- tolerance ==~ m2) should be (m1 ==~ m2 +- tolerance)
        }
      }

      "check for smaller" in {
        forAll(maxDiscardedFactor(10)) { (c1: Currency {type Key <: Currency.Key}, amount1: BigDecimal, amount2: BigDecimal, tolerance : BigDecimal) ⇒
          whenever(amount1 + tolerance < amount2 && tolerance >= 0) {
            val m1 = Money(amount1, c1)
            val m2 = Money(amount2, c1)
            (m1 +- tolerance <~ m2) should be (true)
          }
        }
      }
      "check for greater" in {
        forAll(maxDiscardedFactor(10)) { (c1: Currency {type Key <: Currency.Key}, amount1: BigDecimal, amount2: BigDecimal, tolerance : BigDecimal) ⇒
          whenever(amount1 - tolerance > amount2 && tolerance >= 0) {
            val m1 = Money(amount1, c1)
            val m2 = Money(amount2, c1)
            (m1 +- tolerance >~ m2) should be (true)
          }
        }
      }
    }
  }
}
