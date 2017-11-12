package com.vatbox.money

import com.vatbox.money.Money.ToBigDecimal
import scala.math.BigDecimal
import scala.math.BigDecimal.RoundingMode.RoundingMode
import scala.util.{Failure, Try}


class Money[C <: Currency.Key] private(a: BigDecimal)(val currency: Currency {type Key = C})(implicit mc: MoneyContext) extends Serializable with Ordered[Money[C]] {

  val amount: BigDecimal = new BigDecimal(a.bigDecimal, mc.mathContext)

  def +(that: Money[C]): Money[C] =
    Money(this.amount + that.amount, this.currency)

  def -(that: Money[C]): Money[C] = this + (-that)

  def +[C2 <: Currency.Key](that: Money[C2]): MoneyExchange[C] =
    MoneyExchange(this.currency, Seq[Money[_ <: Currency.Key]](that, this))

  def -[C2 <: Currency.Key](that: Money[C2]): MoneyExchange[C] = this + (-that)

  def +[C2 <: Currency.Key](that: MoneyExchange[C2]): MoneyExchange[C] =
    MoneyExchange(currency, that.moneySeq :+ this)

  def -[C2 <: Currency.Key](that: MoneyExchange[C2]): MoneyExchange[C] =
    MoneyExchange(currency, that.moneySeq.map(-_) :+ this)

  //  def +[B: ToBigDecimal](that: B): Money[C] =
  //    Money(this.amount + that, this.currency)
  //
  //  def -[B: ToBigDecimal](that: B): Money[C] = this + (-that)

  def *[B: ToBigDecimal](that: B): Money[C] = Money(this.amount * that, this.currency): Money[C]

  def /[B: ToBigDecimal](that: B): Money[C] = Money(this.amount / that, this.currency)


  def unary_- : Money[C] = Money(-this.amount, this.currency)

  /** Returns the absolute value of this Money
    */
  def abs: Money[C] = if (amount.signum < 0) unary_- else this

  /** Returns the minimum of this and that, or this if the two are equal
    */
  def min(that: Money[C]): Money[C] = if ((this compare that) <= 0) this else that

  /** Returns the maximum of this and that, or this if the two are equal
    */
  def max(that: Money[C]): Money[C] = if ((this compare that) >= 0) this else that


  /** Returns the sign of this Money;
    * -1 if it is less than 0,
    * +1 if it is greater than 0,
    * 0  if it is equal to 0.
    */
  def signum: Int = this.amount.signum

  /** Returns the size of an ulp, a unit in the last place, of this Money.
    */
  def ulp: BigDecimal = this.amount.ulp

  /** Returns the precision of this `Money`.
    */
  def precision: Int = this.amount.precision

  /** Returns the scale of this `Money`.
    */
  def scale: Int = this.amount.scale

  def withScale(scale: Int, mode: RoundingMode): Money[C] =
    if (this.scale == scale) this
    else Money(this.amount.setScale(scale, mode), this.currency)

  def withScale(scale: Int)(implicit mc: MoneyContext): Money[C] =
    withScale(scale, mc.roundingMode)

  def scaled(implicit mc: MoneyContext): Money[C] =
    withScale(mc.scale, mc.roundingMode)


  def rounded(implicit mc: MoneyContext): Money[C] = {
    Money(this.amount.round(mc.mathContext), this.currency)
  }


  def in[C2 <: Currency.Key](unit: Currency {type Key = C2}): MoneyExchange[C2] =
    MoneyExchange(unit, Seq(this))

  override def compare(that: Money[C]): Int = that.currency match {
    case this.currency ⇒ if (this.amount > that.amount) 1 else if (this.amount < that.amount) -1 else 0
    case _ ⇒ throw new UnsupportedOperationException("Comparison between Moneys of dislike Currency is not supported")
  }

  override def toString: String = amount.toString + " " + currency.code

  def toFormattedString: String = currency.symbol + amount.setScale(currency.exponent, mc.roundingMode).toString

  def canEqual(other: Any): Boolean = other.isInstanceOf[Money[C]]

  override def equals(other: Any): Boolean = other match {
    case that: Money[C] ⇒
      (that canEqual this) &&
        amount == that.amount &&
        currency == that.currency
    case _ ⇒ false
  }

  override def hashCode(): Int = {
    val state = Seq(amount, currency)
    state.map(_.hashCode()).foldLeft(0)((a, b) ⇒ 31 * a + b)
  }
}


object Money {
  private def loadMoney(value: BigDecimal, currencyCode: String)(implicit mc: MoneyContext): Try[Money[_ <: Currency.Key]] = Try {
    val c = Currency.currencies.get(currencyCode).getOrElse(mc.parseNotFound(currencyCode))
    Money(value, c)
  }

  def apply[B: ToBigDecimal](value: B)(implicit mc: MoneyContext) = new Money(implicitly[ToBigDecimal[B]].apply(value))(mc.defaultCurrency)

  def unapply[C <: Currency.Key](money: Money[C]): Option[(BigDecimal, Currency {type Key = C})] = Some(money.amount, money.currency)

  def apply[C <: Currency.Key, B: ToBigDecimal](value: B, currency: Currency {type Key = C}) = new Money[C](implicitly[ToBigDecimal[B]].apply(value))(currency)

  def apply[C <: Currency.Key, B: ToBigDecimal](value: B, currencyCode: String) = loadMoney(implicitly[ToBigDecimal[B]].apply(value), currencyCode)

  def parse(s: String)(implicit mc: MoneyContext): Try[Money[_ <: Currency.Key]] = {
    lazy val codeRgx = "([-+]?[0-9]*\\.?[0-9]+) *([A-Z]{3})".r
    lazy val symbolRex = "([^\\d. ]) *([-+]?[0-9]*\\.?[0-9]+)".r
    s match {
      case codeRgx(value, currencyCode) ⇒ loadMoney(BigDecimal(value), currencyCode)
      case symbolRex(currencyCode, value) ⇒ loadMoney(BigDecimal(value), currencyCode)
      case _ ⇒ Failure(MoneyParseException("Unable to parse Money", s))
    }
  }

  type ToBigDecimal[A] = A ⇒ BigDecimal
}


//class MoneyNumeric[C <: Currency.Key]()(implicit mc: MoneyContext) extends Numeric[Money[C]] {
//  def plus(x: Money[C], y: Money[C]) = x + y
//  def minus(x: Money[C], y: Money[C]) = x - y
//  def times(x: Money[C], y: Money[C]) = throw new UnsupportedOperationException("Numeric.times not supported for Quantities")
//  def negate(x: Money[C]) = -x
//  def fromInt(x: Int) = throw new IllegalArgumentException
//  def toInt(x: Money[C]) = x.amount.toInt
//  def toLong(x: Money[C]) = x.amount.toLong
//  def toFloat(x: Money[C]) = x.amount.toFloat
//  def toDouble(x: Money[C]) = x.amount.toDouble
//  def compare(x: Money[C], y: Money[C]) = if (x.amount > y.amount) 1 else if (x.amount < y.amount) -1 else 0
//
//  /**
//    * Custom implementation using SortedSets to ensure consistent output
//    * @return String representation of this instance
//    */
//  override def toString: String = s"MoneyNumeric($mc)"
//}