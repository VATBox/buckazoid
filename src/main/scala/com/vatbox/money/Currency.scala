package com.vatbox.money

import com.vatbox.money.Money.ToBigDecimal
import scala.collection.mutable


case class Currency(code: String, name: String, symbol: String, formatDecimals: Int) {
  type Key <: Currency.Key

  def apply[B: ToBigDecimal](amount: B): Money[Key] = Money(amount, this)

  def to[C <: Currency.Key](unit: Currency {type Key = C}): CurrencyExchange[Key, C] = CurrencyExchange(this, unit)

  Currency.currencies += (code → this)
  Currency.currencies += (symbol → this)


  def canEqual(other: Any): Boolean = other.isInstanceOf[Currency]

  override def equals(other: Any): Boolean = other match {
    case that: Currency ⇒
      (that canEqual this) &&
        code == that.code
    case _ ⇒ false
  }

  override def hashCode(): Int = {
    val state = Seq(code)
    state.map(_.hashCode()).foldLeft(0)((a, b) ⇒ 31 * a + b)
  }
}

object Currency {
//  type Aux[A0, B0] = Foo[A0] { type B = B0  }
//  type Aux[K <: Currency.Key] = Currency {type Key <: K}

  trait Key

  case class Evidence[C <: Currency.Key](currency: Currency {type Key = C})

  def apply(code: String)(implicit mc: MoneyContext): Currency {type Key <: Currency.Key} = Currency(code, "", "", mc.currencyFormatDecimals)

  private[money] lazy val currencies: mutable.Map[String, Currency {type Key <: Currency.Key}] = mutable.Map()

  def getCurrencies = currencies.toMap
}


//trait USD extends Currency.Key
object USD extends Currency("USD", "US Dollar", "$", 2) {
//  type Key = USD
//  implicit val currencyEvidence: Currency.Evidence[USD] = Currency.Evidence(this)
}

object ILS extends Currency("ILS", "Israel Shekel", "₪", 2)

object ARS extends Currency("ARS", "Argentinean Peso", "$", 2)

object AUD extends Currency("AUD", "Australian Dollar", "$", 2)

object BRL extends Currency("BRL", "Brazilian Real", "R$", 2)

object CAD extends Currency("CAD", "Canadian Dollar", "$", 2)

object CHF extends Currency("CHF", "Swiss Franc", "CHF", 2)

object CLP extends Currency("CLP", "Chilean Peso", "¥", 2)

object CNY extends Currency("CNY", "Chinese Yuan Renmimbi", "¥", 2)

object CZK extends Currency("CZK", "Czech Republic Koruny", "Kč", 2)

object DKK extends Currency("DKK", "Danish Kroner", "kr", 2)

object EUR extends Currency("EUR", "Euro", "€", 2)

object GBP extends Currency("GBP", "British Pound", "£", 2)

object HKD extends Currency("HKD", "Hong Kong Dollar", "$", 2)

object INR extends Currency("INR", "Indian Rupee", "₹", 2)

object JPY extends Currency("JPY", "Japanese Yen", "¥", 0)

object KRW extends Currency("KRW", "South Korean Won", "kr", 0)

object MXN extends Currency("MXN", "Mexican Peso", "$", 2)

object MYR extends Currency("MYR", "Malaysian Ringgit", "RM", 2)

object NOK extends Currency("NOK", "Norwegian Krone", "kr", 2)

object NZD extends Currency("NZD", "New Zealand Dollar", "$", 2)

object RUB extends Currency("RUB", "Russian Ruble", "руб", 2)

object SEK extends Currency("SEK", "Swedish Kroner", "kr", 2)

object XAG extends Currency("XAG", "Silver", "oz", 4)

object XAU extends Currency("XAU", "Gold", "oz", 4)

object BTC extends Currency("BTC", "BitCoin", "B", 15)

object BKZ extends Currency("BKZ", "SpaceQuest Buckazoid", "bz", 2)
