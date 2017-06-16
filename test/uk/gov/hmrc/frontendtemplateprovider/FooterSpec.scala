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

import akka.actor.{ActorSystem, Cancellable}
import org.scalatest.{Matchers, WordSpec}
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

import scala.concurrent.{ExecutionContext, Future}

class FooterSpec extends WordSpec with Matchers  with Results with WithFakeApplication {

  implicit val hc = HeaderCarrier()

  override lazy val fakeApplication: Application = new GuiceApplicationBuilder()
    .configure(Map("assets.url" -> "www.example.com/"))
    .bindings(bindModules:_*)
    .build()

  val fakeRequest = FakeRequest("GET", "/")

  "Footer" should {
    "allow additional footer links to be specified SDT 477" in new Setup {
      val href = "www.example.com"
      val text = "something"
      val renderedHtml: String = localTemplateRenderer.parseTemplate(Html(""), Map(
        "additionalFooterLinks" -> Map("url" -> href, "text" -> text)
      )).body
      renderedHtml should include(s"""<li><a href="$href">$text</a></li>""")
    }
    "allow multiple additional footer links to be specified SDT 477" in new Setup {
      val href1 = "www.example.com"
      val href2 = "www.other.com"
      val text1 = "something"
      val text2 = "anything"
      val renderedHtml: String = localTemplateRenderer.parseTemplate(Html(""), Map(
        "additionalFooterLinks" -> Seq(
          Map("url" -> href1, "text" -> text1),
          Map("url" -> href2, "text" -> text2)
        )
      )).body
      renderedHtml should include(s"""<li><a href="$href1">$text1</a></li>""")
      renderedHtml should include(s"""<li><a href="$href2">$text2</a></li>""")
    }

    "support additional script elements in the footer of the page SDT 578" in new Setup {
      val src = "www.example.com"
      val renderedHtml: String = localTemplateRenderer.parseTemplate(Html(""), Map(
        "scriptElems" -> Map("url" -> src)
      )).body
      renderedHtml should include(s"""<script src="$src" type="text/javascript"></script>""")
    }

    "support multiple additional script elements in the footer of the page SDT 578" in new Setup {
      val src1 = "www.example.com"
      val src2 = "www.another.com"
      val renderedHtml: String = localTemplateRenderer.parseTemplate(Html(""), Map(
        "scriptElems" -> Seq(
          Map("url" -> src1),
          Map("url" -> src2)
        )
      )).body
      renderedHtml should include(s"""<script src="$src1" type="text/javascript"></script>""")
      renderedHtml should include(s"""<script src="$src2" type="text/javascript"></script>""")
    }

    "support SSO Url SDT-473" in new Setup {
      val ssoUrl = "www.example.com"
      val renderedHtml: String = localTemplateRenderer.parseTemplate(Html(""), Map(
        "ssoUrl" -> ssoUrl
      )).body
      renderedHtml should include(s"""<script type="text/javascript">var ssoUrl = "$ssoUrl";</script>""")
    }

    "no ssoUrl script when no ssoUrl given SDT-473" in new Setup {
      val renderedHtml: String = localTemplateRenderer.parseTemplate(Html(""), Map()).body
      renderedHtml should not include(s"""<script type="text/javascript">var ssoUrl = """)
    }
  }

  trait Setup {

    val result: Future[Result] = GovUkTemplateRendererController.serveMustacheTemplate()(fakeRequest)
    val bodyText: String = contentAsString(result)
    status(result) shouldBe OK

    val localTemplateRenderer = new MustacheRendererTrait {
      override lazy val templateServiceAddress: String = ???
      override lazy val connection: WSGet = ???
      override def scheduleGrabbingTemplate()(implicit ec: ExecutionContext): Cancellable = ???
      override lazy val akkaSystem: ActorSystem = ???

      override protected def getTemplate: String = bodyText
    }
  }
}
