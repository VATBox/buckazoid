package com.vatbox.money

import java.time.Instant

import scala.concurrent.{ExecutionContext, Future}


case class CurrencyExchange[C1 <: Currency.Key, C2 <: Currency.Key](base: Currency {type Key = C1}, counter: Currency {type Key = C2}) {
  def inverse: CurrencyExchange[C2, C1] = CurrencyExchange(counter, base)

  def at(exchangeDate: Instant): CurrencyExchangeRate[C1, C2] = CurrencyExchangeRate(base, counter, exchangeDate)

  def convert(amount: BigDecimal) = MoneyExchange(counter, Seq(Money(amount, base)))
}

case class CurrencyExchangeRate[C1 <: Currency.Key, C2 <: Currency.Key](base: Currency {type Key = C1}, counter: Currency {type Key = C2}, exchangeDate: Instant) {
  def rate(implicit er: ExchangeRate, ec: ExecutionContext): Future[BigDecimal] = er.convert(base, counter, 1, exchangeDate).map(_.toDouble)

  def inverse: CurrencyExchangeRate[C2, C1] = CurrencyExchangeRate(counter, base, exchangeDate)

  def convert(amount: BigDecimal)(implicit er: ExchangeRate, ec: ExecutionContext): Future[Money[C2]] = MoneyExchange(counter, Seq(Money(amount, base))) at exchangeDate

  def convert(money: Money[C1])(implicit er: ExchangeRate, ec: ExecutionContext): Future[Money[C2]] = MoneyExchange(counter, Seq(money)) at exchangeDate
}

case class MoneyExchange[C <: Currency.Key](baseCurrency: Currency {type Key = C}, moneySeq: Seq[Money[_<: Currency.Key]]) {
  def at(exchangeDate: Instant)(implicit er: ExchangeRate, ec: ExecutionContext): Future[Money[C]] = {
    val moneyPerCurrency = moneySeq.groupBy(_.currency)
    val sumPerCurrency = moneyPerCurrency map { case (c, mx) ⇒ Money(mx.foldLeft(BigDecimal(0)){(sum, m) ⇒ sum + m.amount}, c) }

    // sequential fast fail
//    val total = sumPerCurrency.foldLeft(Future.successful(BigDecimal(0))){(b,m) ⇒ b.flatMap(_⇒er.convert(m.currency, baseCurrency, m.amount, exchangeDate))}

    // Paralleled
    val valueInBaseCurrency = sumPerCurrency.map { m ⇒ er.convert(m.currency, baseCurrency, m.amount, exchangeDate)}
    val total = valueInBaseCurrency.reduceLeft{(a,b) ⇒ a.flatMap(a1 ⇒ b.map(a1+_))}

    total map { Money(_, baseCurrency) }
//    Future.reduceLeft(valueInBaseCurrency)(_+_) map { Money(_, baseCurrency)}
  }

  def +[C2 <: Currency.Key](that: Money[C2]): MoneyExchange[C] =
    MoneyExchange(baseCurrency, moneySeq :+ that)
  def -[C2 <: Currency.Key](that: Money[C2]): MoneyExchange[C] =
    MoneyExchange(baseCurrency, moneySeq :+ (-that))

  def +[C2 <: Currency.Key](that: MoneyExchange[C2]): MoneyExchange[C] =
    MoneyExchange(baseCurrency, moneySeq ++ that.moneySeq)
  def -[C2 <: Currency.Key](that: MoneyExchange[C2]): MoneyExchange[C] =
    MoneyExchange(baseCurrency, moneySeq ++ that.moneySeq.map(-_))

  def in[C2 <: Currency.Key](unit: Currency {type Key = C2}): MoneyExchange[C2] =
    MoneyExchange(unit, moneySeq)
}

trait ExchangeRate {
  def convert(base: Currency {type Key <: Currency.Key}, counter: Currency {type Key <: Currency.Key}, amount: BigDecimal, exchangeDate: Instant): Future[BigDecimal]
}


case class ExchangeRateException(message: String) extends Exception(message)

case class MoneyParseException(message: String, expression: String) extends Exception(message)

case class NoSuchCurrencyException(message: String, expression: String) extends Exception(message)

