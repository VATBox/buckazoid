package com.vatbox.money.contrib

import java.time.{LocalDate, ZoneOffset}

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.vatbox.money._
import com.vatbox.money.contrib.exchangerate.PlayOpenExchangeRates
import org.f100ded.play.fakews._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}
import play.api.libs.json.Json
import play.api.libs.ws.JsonBodyWritables._
import play.api.libs.ws.DefaultBodyWritables.writeableOf_String

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class PlayOpenExchangeRatesSpec extends WordSpec with Matchers with ScalaFutures with BeforeAndAfterAll {
  implicit val system = ActorSystem()

  implicit val mat = ActorMaterializer()

  val queryDate = "2018-06-09"

  override protected def afterAll(): Unit = {
    Await.result(system.terminate(), Duration.Inf)
  }

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


        val ws = StandaloneFakeWSClient {
//          case GET(url"https://openexchangerates.org/api/historical/2018-06-09.json?app_id=key&base=USD&symbols=EUR") =>
//            // verify access token
//            request.headers should contain ("Authorization" -> Seq(s"Bearer $accessToken"))

            // this is here just to demonstrate how you can use URL extractor
//            date shouldBe queryDate

//            Ok(Json.parse(json))
//          case r @ FakeRequest("GET", "https://openexchangerates.org/api/historical/2018-06-09.json?app_id=key&base=USD&symbols=EUR") ⇒ ???
          case r if r == FakeRequest("GET", "https://openexchangerates.org/api/historical/2018-06-09.json?app_id=key&base=USD&symbols=EUR") ⇒
            Ok(Json.parse(json))
          case r ⇒
            throw new RuntimeException("Unexpected call")
        }

        implicit val ppenExchangeRates = PlayOpenExchangeRates(ws, "https://openexchangerates.org/api/historical", "key")

        val response = USD(100) in EUR at LocalDate.parse(queryDate).atStartOfDay().toInstant(ZoneOffset.UTC)

        whenReady(response) { r ⇒
          r should be (EUR(85))
        }
      }
    }

    "call with non exists counter currency" should {
      "fail with ExchangeRateException" in {
        val json =
          """
            |{
            |  "disclaimer": "Usage subject to terms: https://openexchangerates.org/terms",
            |  "license": "https://openexchangerates.org/license",
            |  "timestamp": 982342800,
            |  "base": "USD",
            |  "rates": {
            |  }
            |}
          """.stripMargin

        val ws = StandaloneFakeWSClient {
          case r if r == FakeRequest("GET", "https://openexchangerates.org/api/historical/2018-06-09.json?app_id=key&base=USD&symbols=BKZ") ⇒
            Ok(Json.parse(json))
          case r ⇒
            throw new RuntimeException("Unexpected call")
        }

        implicit val ppenExchangeRates = PlayOpenExchangeRates(ws, "https://openexchangerates.org/api/historical", "key")

        val response = USD(100) in BKZ at LocalDate.parse(queryDate).atStartOfDay().toInstant(ZoneOffset.UTC)

        assert(response.failed.futureValue.isInstanceOf[ExchangeRateException])
      }
    }


    "call with non exists source currency" should {
      "fail with ExchangeRateException" in {
        val errorJson =
          """
            |{
            |  "error": true,
            |  "status": 400,
            |  "message": "invalid_base",
            |  "description": "Invalid `base` currency [BKZ]. Please ensure..."
            |}
          """.stripMargin


        val ws = StandaloneFakeWSClient {
          case r if r == FakeRequest("GET", "https://openexchangerates.org/api/historical/2018-06-09.json?app_id=key&base=BKZ&symbols=EUR") ⇒
            BadRequest(Json.parse(errorJson))
          case r ⇒
            throw new RuntimeException("Unexpected call")
        }

        implicit val ppenExchangeRates = PlayOpenExchangeRates(ws, "https://openexchangerates.org/api/historical", "key")

        val response = BKZ(100) in EUR at LocalDate.parse(queryDate).atStartOfDay().toInstant(ZoneOffset.UTC)

        assert(response.failed.futureValue.isInstanceOf[ExchangeRateException])
      }
    }

    "server failure" should {
      "fail with ExchangeRateException" in {
        val ws = StandaloneFakeWSClient {
          case r if r == FakeRequest("GET", "https://openexchangerates.org/api/historical/2018-06-09.json?app_id=key&base=USD&symbols=EUR") ⇒
            InternalServerError("Server Error")
          case r ⇒
            throw new RuntimeException("Unexpected call")
        }

        implicit val ppenExchangeRates = PlayOpenExchangeRates(ws, "https://openexchangerates.org/api/historical", "key")

        val response = USD(100) in EUR at LocalDate.parse(queryDate).atStartOfDay().toInstant(ZoneOffset.UTC)

        assert(response.failed.futureValue.isInstanceOf[ExchangeRateException])
      }
    }
  }
}
