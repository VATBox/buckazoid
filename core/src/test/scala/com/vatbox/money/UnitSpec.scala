package com.vatbox.money

import java.math.MathContext
import java.time.Instant

import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import scala.concurrent.Future

//With Inspectors
trait UnitSpec extends WordSpec
  with Matchers
  with GeneratorDrivenPropertyChecks
  with ScalaFutures
  with OptionValues
  with TryValues
  with Inside
  with FakeConvertor


trait FakeConvertor {

  val fakeRatio = BigDecimal(3.123, MathContext.UNLIMITED)

  implicit object ExchangeRateDemo extends ExchangeRate {
    override def convert(base: Currency {type Key <: Currency.Key}, counter: Currency {type Key <: Currency.Key}, amount: BigDecimal, exchangeDate: Instant): Future[BigDecimal] = {
      (base, counter) match {
        case (a, b) if a == b ⇒ Future.successful {
          amount
        }
        case (_, _) ⇒ Future.successful {
          fakeRatio * amount
        }
      }
    }
  }

}