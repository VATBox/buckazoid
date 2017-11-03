package com.vatbox

import com.vatbox.money.Money.ToBigDecimal


package object money {

  implicit class NumberWithMoney[B: ToBigDecimal](val x: B) {
    def apply[C <: Currency.Key](c: Currency {type Key = C}): Money[C] = Money[C, B](x, c)
//    def plus[C <: Currency.Key](m: Money[C]): Money[C] = Money(m.amount + x, m.currency)
  }

//  implicit def FloatToBigDecima(x: Float): BigDecimal = BigDecimal(x)
//  implicit def BigIntToBigDecima(x: BigInt): BigDecimal = BigDecimal(x)


  implicit class IntWithMoney(val x: Int) extends AnyVal {
//    def +[C <: Currency.Key](m: Money[C]): Money[C] = Money(x + m.amount, m.currency)
//    def -[C <: Currency.Key](m: Money[C]): Money[C] = Money(x - m.amount, m.currency)
    def *[C <: Currency.Key](m: Money[C]): Money[C] = m * x
  }

  implicit class LongWithMoney(val x: Long) extends AnyVal {
//    def +[C <: Currency.Key](m: Money[C]): Money[C] = Money(x + m.amount, m.currency)
//    def -[C <: Currency.Key](m: Money[C]): Money[C] = Money(x - m.amount, m.currency)
    def *[C <: Currency.Key](m: Money[C]): Money[C] = m * x
  }

//  implicit class FloatWithMoney(val x: Float) extends AnyVal {
//    def +[C <: Currency.Key](m: Money[C]): Money[C] = Money(x + m.amount, m.currency)
//    def -[C <: Currency.Key](m: Money[C]): Money[C] = Money(x - m.amount, m.currency)
//    def *[C <: Currency.Key](m: Money[C]): Money[C] = Money(x * m.amount, m.currency)
//  }

  implicit class DoubleWithMoney(val x: Double) extends AnyVal {
//    def +[C <: Currency.Key](m: Money[C]): Money[C] = Money(x + m.amount, m.currency)
//    def -[C <: Currency.Key](m: Money[C]): Money[C] = Money(x - m.amount, m.currency)
    def *[C <: Currency.Key](m: Money[C]): Money[C] = m * x
  }

  implicit class BigDecimalWithMoney(val x: BigDecimal) extends AnyVal {
//    def +[C <: Currency.Key](m: Money[C]): Money[C] = Money(x + m.amount, m.currency)
//    def -[C <: Currency.Key](m: Money[C]): Money[C] = Money(x - m.amount, m.currency)
    def *[C <: Currency.Key](m: Money[C]): Money[C] = m * x
  }

//  implicit class BigIntWithMoney(val x: BigInt) extends AnyVal {
//    def +[C <: Currency.Key](m: Money[C]): Money[C] = Money(BigDecimal(x) + m.amount, m.currency)
//    def -[C <: Currency.Key](m: Money[C]): Money[C] = Money(BigDecimal(x) - m.amount, m.currency)
//    def *[C <: Currency.Key](m: Money[C]): Money[C] = Money(BigDecimal(x) * m.amount, m.currency)
//  }

//  implicit def IntWithMoney(x: Int) = new NumericWithMoney[Int](x)
//  implicit def LongWithMoney(x: Long) = new NumericWithMoney[Long](x)
//  implicit def FloatWithMoney(x: Float) = new NumericWithMoney[Float](x)
//  implicit def DoubleWithMoney(x: Double) = new NumericWithMoney[Double](x)
//
//  implicit class NumericWithMoney[N: Numeric](val x: N) {
//    def +[C <: Currency.Key](m: Money[C]): Money[C] = Money(implicitly[Numeric[N]].toDouble(x) + m.amount, m.currency)
//    def -[C <: Currency.Key](m: Money[C]): Money[C] = Money(implicitly[Numeric[N]].toDouble(x) - m.amount, m.currency)
//    def *[C <: Currency.Key](m: Money[C]): Money[C] = Money(implicitly[Numeric[N]].toDouble(x) * m.amount, m.currency)
//  }


}
