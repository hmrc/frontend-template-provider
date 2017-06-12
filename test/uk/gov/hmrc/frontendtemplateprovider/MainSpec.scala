/*
 * Copyright 2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.frontendtemplateprovider

import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.{Result, Results}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.twirl.api.Html
import uk.gov.hmrc.frontendtemplateprovider.controllers.GovUkTemplateRendererController
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.play.http.ws.WSGet
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}
import uk.gov.hmrc.renderer.MustacheRendererTrait

import scala.concurrent.Future

class MainSpec extends UnitSpec with Results with WithFakeApplication {

  implicit val hc = HeaderCarrier()

  override lazy val fakeApplication: Application = new GuiceApplicationBuilder()
    .configure(Map("assets.url" -> "www.example.com/"))
    .bindings(bindModules:_*)
    .build()

  val fakeRequest = FakeRequest("GET", "/")

  "Main" should {
    "not add a main class for main tag if non specified SDT 571" in new Setup {
      val renderedHtml: String = localTemplateRenderer.parseTemplate(Html(""), Map()).body
      val main = mainTagRegex.findFirstIn(renderedHtml).get
      main should not include("class")
    }

    "allow main tag to have it's mainClass SDT 571" in new Setup {
      val renderedHtml: String = localTemplateRenderer.parseTemplate(Html(""), Map(
        "mainClass" -> "clazz"
      )).body
      val main = mainTagRegex.findFirstIn(renderedHtml).get
      main should include("""class="clazz"""")
    }

    "allow main attributes to be specified in main tag SDT 572" in new Setup {
      val attribute = "id=\"main\""
      val renderedHtml: String = localTemplateRenderer.parseTemplate(Html(""), Map(
        "mainAttributes" -> attribute
      )).body
      val main = mainTagRegex.findFirstIn(renderedHtml).get
      main should include(attribute)
    }

    "not show beta banner if there is no service name SDT 476" in new Setup {
      val renderedHtml: String = localTemplateRenderer.parseTemplate(Html(""), Map()).body
      renderedHtml should not include("""<div class="beta-banner">""")
    }

    "show beta banner when you specify a service name SDT 476" in new Setup {
      val renderedHtml: String = localTemplateRenderer.parseTemplate(Html(""), Map(
        "betaBanner" -> Map("feedbackIdentifier" -> "PTA")
      )).body
      renderedHtml should include("""<div class="beta-banner">""")
      renderedHtml should include("""href="beta-feedback-unauthenticated?service=PTA"""")
    }

    "show beta banner with no feedback link if you don't specify a feedbackIdentifier SDT 476" in new Setup {
      val renderedHtml: String = localTemplateRenderer.parseTemplate(Html(""), Map(
        "betaBanner" -> true
      )).body
      renderedHtml should include("""<div class="beta-banner">""")
      renderedHtml should not include("""href="beta-feedback-unauthenticated?service=PTA"""")
      renderedHtml should include("This is a new service.")
    }
  }


  trait Setup {

    val result: Future[Result] = GovUkTemplateRendererController.serveMustacheTemplate()(fakeRequest)
    val bodyText: String = contentAsString(result)
    status(result) shouldBe OK

    val mainTagRegex = "<main\\b[^>]*>".r

    val localTemplateRenderer = new MustacheRendererTrait {
      override lazy val templateServiceAddress: String = ???
      override lazy val connection: WSGet = ???
      override lazy val mustacheTemplateString: String = bodyText
    }
  }
}
