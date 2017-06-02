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

/**
  * Created by mo on 31/05/2017.
  */
class HeadSpec extends UnitSpec with Results with WithFakeApplication {

  implicit val hc = HeaderCarrier()

  override lazy val fakeApplication: Application = new GuiceApplicationBuilder()
    .configure(Map("assets.url" -> "www.example.com/"))
    .bindings(bindModules:_*)
    .build()

  val fakeRequest = FakeRequest("GET", "/")

  "Mustache Template" should {
    "contain IE links" in new Setup {
      bodyText should include("<!--[if IE 6]><link href=\"/template/assets/stylesheets/govuk-template-ie6.css\" media=\"screen\" rel=\"stylesheet\" type=\"text/css\" /><![endif]-->")
      bodyText should include("<!--[if IE 7]><link href=\"/template/assets/stylesheets/govuk-template-ie7.css\" media=\"screen\" rel=\"stylesheet\" type=\"text/css\" /><![endif]-->")
      bodyText should include("<!--[if IE 8]><link href=\"/template/assets/stylesheets/govuk-template-ie8.css\" media=\"screen\" rel=\"stylesheet\" type=\"text/css\" /><![endif]-->")
      bodyText should include("<!--[if gt IE 8]><!--><link href=\"/template/assets/stylesheets/govuk-template.css\" media=\"screen\" rel=\"stylesheet\" type=\"text/css\" /><!--<![endif]-->")
    }

    "contain link to assets-frontend application links" in new Setup {
      bodyText should include("<link rel='stylesheet' href='www.example.com/stylesheets/application-ie7.min.css' />")
      bodyText should include("<link rel='stylesheet' href='www.example.com/stylesheets/application-ie.min.css' />")
      bodyText should include("<link rel='stylesheet' href='www.example.com/stylesheets/application.min.css' />")
    }

    "contain a body opening tag that does not contain a class SBT-470" in new Setup {
      bodyText should include("<body>")
    }

    "contains no optimizely script SBT-471" in new Setup {
      val renderedHtml: String = localTemplateRenderer.parseTemplate(Html(""), Map()).body
      renderedHtml should not include("optimizely")
      renderedHtml should not include("<script src=''") // should not include a script with no src
    }

    "contains no optimizely script if only baseUrl is provided SBT-470" in new Setup {
      val renderedHtml: String = localTemplateRenderer.parseTemplate(Html(""), Map(
        "optimizelyBaseUrl" -> "cdn.optimizely.com"
      )).body
      renderedHtml should not include("cdn.optimizely.com")
    }

    "contains no optimizely script if only projectId is provided SBT-470" in new Setup {
      val renderedHtml: String = localTemplateRenderer.parseTemplate(Html(""), Map(
        "optimizelyProjectId" -> "id123"
      )).body
      renderedHtml should not include("id123")
    }

    "contains optimizely script if both url and projectId is provided SBT-470" in new Setup {
      val renderedHtml: String = localTemplateRenderer.parseTemplate(Html(""), Map(
        "optimizelyBaseUrl" -> "cdn.optimizely.com/",
        "optimizelyProjectId" -> "id123"
      )).body
      renderedHtml should include("<script src='cdn.optimizely.com/id123.js' type='text/javascript'></script>")
    }

    "contains default title if not specified SDT 480" in new Setup {
      val renderedHtml: String = localTemplateRenderer.parseTemplate(Html(""), Map()).body
      renderedHtml should include("GOV.UK - The best place to find government services and information")
    }

    "contains default title if not specified in SDT 480" in new Setup {
      val myTitle = "My very own title."
      val renderedHtml: String = localTemplateRenderer.parseTemplate(Html(""), Map(
        "pageTitle" -> myTitle
      )).body
      renderedHtml should include(myTitle)
    }
  }

  trait Setup {

    val result: Future[Result] = GovUkTemplateRendererController.serveMustacheTemplate()(fakeRequest)
    val bodyText: String = contentAsString(result)
    status(result) shouldBe OK


    val localTemplateRenderer = new MustacheRendererTrait {
      override lazy val templateServiceAddress: String = ???
      override lazy val connection: WSGet = ???
      override lazy val mustacheTemplateString: String = bodyText
    }
  }
}
