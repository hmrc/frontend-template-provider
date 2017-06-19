package uk.gov.hmrc.frontendtemplateprovider.config

import org.scalatest.{FunSuite, Matchers, WordSpec}
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.Results
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.play.test.WithFakeApplication

class WhitelistFilterTest extends WordSpec with Matchers with WithFakeApplication {

  val ip = "127.0.0.1"

  override lazy val fakeApplication: Application = new GuiceApplicationBuilder()
    .configure(Map("whitelistIps" -> Seq(ip), "shouldWhitelist" -> true))
    .bindings(bindModules:_*)
    .build()

  "WhitelistFilter" should {
    "retrieve the whitelist IP addresses correctly" in {
      WhitelistFilter.whitelist should be(Seq(ip))
    }
  }

}
