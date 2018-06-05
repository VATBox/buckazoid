package com.vatbox.money

import com.vatbox.money.Generators._

class ImplicitSpec extends UnitSpec {
  "Implicits" should {
    "Money math with Numbers" should {
      "work for BigDecimal" in {
        forAll { (m1: Money[_ <: Currency.Key], a: BigDecimal) ⇒
          (a * m1) should equal(m1 * a)
        }
      }

//      "work for BigInt" in {
//        forAll { (m1: Money[_ <: Currency.Key], a: BigInt) ⇒
//          a * m1 should equal(m1 * a)
//        }
//      }

      "work for Int" in {
        forAll { (m1: Money[_ <: Currency.Key], a: Int) ⇒
          a * m1 should equal(m1 * a)
        }
      }

      "work for Long" in {
        forAll { (m1: Money[_ <: Currency.Key], a: Long) ⇒
          a * m1 should equal(m1 * a)
        }
      }

//      "work for Float" in {
//        forAll { (m1: Money[_ <: Currency.Key], a: Float) ⇒
//          a * m1 should equal(m1 * a)
//        }
//      }

      "work for Double" in {
        forAll { (m1: Money[_ <: Currency.Key], a: Double) ⇒
          a * m1 should equal(m1 * a)
        }
      }
    }

//    "DateTimeUtils" should {
//      import DateTimeUtils._
//      "Convert LocalDate to Instant" in {
//        forAll { (dateSec: Long) ⇒
//          val instant = Instant.ofEpochSecond(dateSec)
//
//          date.toInstance should be (Instant.ofEpochSecond()
//        }
//      }
//    }
  }
}
