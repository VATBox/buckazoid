package com.vatbox.money.contrib

import java.time.{LocalDate, ZoneOffset}

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.vatbox.money._
import com.vatbox.money.contrib.exchangerate.PlayOpenExchangeRates
import org.f100ded.play.fakews.{Ok, _}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json
import play.api.libs.ws.JsonBodyWritables._
import scala.concurrent.ExecutionContext.Implicits.global

class PlayOpenExchangeRatesSpec extends WordSpec with Matchers with ScalaFutures {
  "PlayOpenExchangeRates" when {
    "call with valid input" should {
      "return value" in {
        val json =
          """
            |{
            |  "disclaimer": "Usage subject to terms: https://openexchangerates.org/terms",
            |  "license": "https://openexchangerates.org/license",
            |  "timestamp": 982342800,
            |  "base": "USD",
            |  "rates": {
            |    "EUR": 0.85
            |  }
            |}
          """.stripMargin

        implicit val system = ActorSystem()

        implicit val mat = ActorMaterializer()

        val queryDate = "2018-06-09"

        val ws = StandaloneFakeWSClient {
          case request @ GET(url"https://openexchangerates.org/api/historical/2018-06-09.json") =>
//            // verify access token
//            request.headers should contain ("Authorization" -> Seq(s"Bearer $accessToken"))

            // this is here just to demonstrate how you can use URL extractor
//            date shouldBe queryDate

            Ok(Json.parse(json))
          case r ⇒
            println(s"r=$r")
            Ok(Json.parse(json))
        }

        implicit val ppenExchangeRates = PlayOpenExchangeRates(ws, "https://openexchangerates.org/api/historical", "key")

        val response = USD(100) in EUR at LocalDate.parse(queryDate).atStartOfDay().toInstant(ZoneOffset.UTC)

        whenReady(response) { r ⇒
          println(s"response=$r")
          r should be (EUR(85))
        }
      }
    }
  }
}
