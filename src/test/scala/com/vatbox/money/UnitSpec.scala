package com.vatbox.money

import java.math.MathContext
import java.time.Instant

import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.Future

//With Inspectors
trait UnitSpec extends WordSpec
  with Matchers
  with GeneratorDrivenPropertyChecks
  with ScalaFutures
  with OptionValues
  with TryValues
  with Inside
  with FakeConvertor {

}


trait FakeConvertor {

  import scala.concurrent.ExecutionContext.Implicits.global

  val fakeRatio = BigDecimal(3.123, MathContext.UNLIMITED)

  implicit object ExchangeRateDemo extends ExchangeRate {
    override def convert(base: Currency, counter: Currency, amount: BigDecimal, exchangeDate: Instant): Future[BigDecimal] = {
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