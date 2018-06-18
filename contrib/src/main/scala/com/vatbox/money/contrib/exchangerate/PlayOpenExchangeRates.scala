package com.vatbox.money.contrib.exchangerate

import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneOffset}

import com.vatbox.money._
import play.api.libs.json.JsValue
import play.api.libs.ws.JsonBodyReadables._
import play.api.libs.ws.StandaloneWSClient

import scala.concurrent.{ExecutionContext, Future}

trait PlayOpenExchangeRates extends ExchangeRate {
  protected def wsClient: StandaloneWSClient

  protected def openExchangeUrl: String

  protected def openExchangeAppId: String

  implicit protected def ec: ExecutionContext

  private val instantFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd").withZone(ZoneOffset.UTC)

  override def convert(base: Currency {type Key <: Currency.Key},
                       counter: Currency {type Key <: Currency.Key},
                       amount: BigDecimal, exchangeDate: Instant): Future[BigDecimal] = {
    //    if (base == counter) Future.successful(amount)
    //    else
    getRate(base.code, counter.code, exchangeDate) map { exchangeRate ⇒
      amount * exchangeRate
    }
  }

  private def getRate(sourceCurrency: String, targetCurrency: String, date: Instant): Future[BigDecimal] = {
    val dateTime = instantFormatter.format(date)
    println(s"url=${openExchangeUrl}/${dateTime}.json")
    wsClient.url(s"${openExchangeUrl}/${dateTime}.json").withQueryStringParameters(
      "app_id" -> openExchangeAppId,
      "base" -> sourceCurrency,
      "symbols" → targetCurrency
    ).get().map { response =>
      response.status match {
        case 200 =>
          val jsonResult = response.body[JsValue]
          val rates = (jsonResult \ "rates")
          val amount = (rates \ targetCurrency).asOpt[BigDecimal]
          amount getOrElse (throw ExchangeRateException(s"Invalid `counter` currency [$targetCurrency]"))
        case 400 =>
          val jsonResult = response.body[JsValue]
          val message = (jsonResult \ "message").as[String]
          if (message contains "invalid_base") throw new ExchangeRateException(s"Invalid `base` currency [$sourceCurrency]")
          else throw new ExchangeRateException(s"Fail to convert $sourceCurrency to $targetCurrency")
        case badStatus =>
          println(s"$badStatus -> ${response.body[JsValue]}")
          throw new ExchangeRateException(s"Fail to convert $sourceCurrency to $targetCurrency")
      }
    }
  }
}

object PlayOpenExchangeRates {
  def apply(_wsClient: StandaloneWSClient, _openExchangeUrl: String, _openExchangeAppId: String)(implicit _ec: ExecutionContext): PlayOpenExchangeRates = {
    new PlayOpenExchangeRates {
      protected lazy val wsClient = _wsClient
      protected lazy val openExchangeUrl = _openExchangeUrl
      protected lazy val openExchangeAppId = _openExchangeAppId
      implicit protected lazy val ec = _ec
    }
  }
}