package com.vatbox.money

import java.math.MathContext

import scala.math.BigDecimal.RoundingMode
import scala.math.BigDecimal.RoundingMode.RoundingMode
import java.math.{RoundingMode ⇒ JavaRoundingMode}

trait MoneyContext {
  def defaultCurrency: Currency {type Key <: Currency.Key} = USD

  def scale: Int = 4

  def roundingMode: RoundingMode = RoundingMode.HALF_EVEN

  def precision: Int = 34

  def mathContext: MathContext = new MathContext(precision, JavaRoundingMode.valueOf(roundingMode.id))

  def currencyDefaultExponent: Int = 2

  def parseNotFound(currencyCode: String): Currency {type Key <: Currency.Key} = throw NoSuchCurrencyException("Currency not found", currencyCode)
}

object MoneyContext {
  implicit val defaultMoneyContext = new MoneyContext {}
}