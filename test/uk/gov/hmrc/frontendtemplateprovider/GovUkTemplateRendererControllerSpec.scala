/*
 * Copyright 2022 HM Revenue & Customs
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

import config.ApplicationConfig
import org.mockito.Mockito.when
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.Results
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Injecting}
import uk.gov.hmrc.frontendtemplateprovider.controllers.GovUkTemplateRendererController

class GovUkTemplateRendererControllerSpec  extends AnyWordSpec with Matchers with GuiceOneAppPerSuite with Results with MockitoSugar with Injecting {

  val fakeRequest = FakeRequest("GET", "/")

  lazy val sut = inject[GovUkTemplateRendererController]

  override def fakeApplication() = new GuiceApplicationBuilder()
    .overrides(
      api.inject.bind[ApplicationConfig].toInstance(config)
    ).build()

  lazy val config = mock[ApplicationConfig]

  "GET /serve-template" must {
    "Return with an HTTP 200 status" in {
      val result = sut.serveMustacheTemplate()(fakeRequest)

      status(result) mustBe OK
    }

    "Render the template correctly" in {
      val result = sut.serveMustacheTemplate()(fakeRequest)
      val bodyText = contentAsString(result)

      bodyText must not contain "@resolveUrl"
      bodyText must include("html")
    }

    "Serve the template with a \"text/html\" Content-type header" in {
      val result = sut.serveMustacheTemplate()(fakeRequest)
      val mimetype = contentType(result).mkString

      mimetype must include("text/html")
    }

    "contain the dev template in the result" in {

      when(config.environment).thenReturn("dev")

      val result = sut.serveMustacheTemplate()(fakeRequest)
      val bodyText = contentAsString(result)

      bodyText must include("http://localhost:9250")
    }

    "contain the base template in the result" in {

      when(config.environment).thenReturn("")

      val result = sut.serveMustacheTemplate()(fakeRequest)
      val bodyText = contentAsString(result)

      bodyText must not include("http://localhost:9250")
    }
  }
}

