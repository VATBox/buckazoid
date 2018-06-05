package com.vatbox

import com.vatbox.money.Money.ToBigDecimal

import scala.language.implicitConversions
import scala.math.BigDecimal

package object money {

  implicit class NumberWithMoney[B: ToBigDecimal](val x: B) {
    def apply[C <: Currency.Key](c: Currency {type Key = C}): Money[C] = Money[C, B](x, c)
//    def plus[C <: Currency.Key](m: Money[C]): Money[C] = Money(m.amount + x, m.currency)
  }

//  implicit def FloatToBigDecima(x: Float): BigDecimal = BigDecimal(x)
  implicit def BigIntToBigDecima(x: BigInt): BigDecimal = BigDecimal(x)


  implicit class IntWithMoney(val x: Int) extends AnyVal {
    def *[C <: Currency.Key](m: Money[C]): Money[C] = m * x
  }

  implicit class LongWithMoney(val x: Long) extends AnyVal {
    def *[C <: Currency.Key](m: Money[C]): Money[C] = m * x
  }

//  implicit class FloatWithMoney(val x: Float) extends AnyVal {
//    def +[C <: Currency.Key](m: Money[C]): Money[C] = Money(x + m.amount, m.currency)
//    def -[C <: Currency.Key](m: Money[C]): Money[C] = Money(x - m.amount, m.currency)
//    def *[C <: Currency.Key](m: Money[C]): Money[C] = Money(x * m.amount, m.currency)
//  }

  implicit class DoubleWithMoney(val x: Double) extends AnyVal {
    def *[C <: Currency.Key](m: Money[C]): Money[C] = m * x
  }

  implicit class BigDecimalWithMoney(val x: BigDecimal) extends AnyVal {
    def *[C <: Currency.Key](m: Money[C]): Money[C] = m * x
  }

  implicit class BigIntWithMoney(val x: BigInt) extends AnyVal {
    def *[C <: Currency.Key](m: Money[C]): Money[C] = Money(BigDecimal(x) * m.amount, m.currency)
  }

  implicit class MoneyToMoneyTolerance[C <: Currency.Key](val m: Money[C]) {
    def +-(tolerance: BigDecimal): MoneyTolerance[C] = MoneyTolerance(m, tolerance)
    def ±(tolerance: BigDecimal): MoneyTolerance[C] = +-(tolerance)
  }
  implicit def moneyToMoneyTolerance[C <: Currency.Key](m: Money[C]) = MoneyTolerance(m, 0)
}
