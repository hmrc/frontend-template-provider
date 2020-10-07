/*
 * Copyright 2020 HM Revenue & Customs
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

import org.scalatest.{Matchers, WordSpec}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.mvc.Results
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.frontendtemplateprovider.controllers.GovUkTemplateRendererController

class GovUkTemplateRendererControllerSpec extends WordSpec with Matchers with GuiceOneAppPerSuite with Results {

  val fakeRequest = FakeRequest("GET", "/")

  lazy val sut = app.injector.instanceOf[GovUkTemplateRendererController]


  "GET /serve-template" should {
    "Return with an HTTP 200 status" in {
      val result = sut.serveMustacheTemplate()(fakeRequest)

      status(result) shouldBe OK
    }

    "Render the template correctly" in {
      val result = sut.serveMustacheTemplate()(fakeRequest)
      val bodyText = contentAsString(result)

      bodyText should not contain "@resolveUrl"
      bodyText should include("html")
    }

    "Serve the template with a \"text/html\" Content-type header" in {
      val result = sut.serveMustacheTemplate()(fakeRequest)
      val mimetype = contentType(result).mkString

      mimetype should include("text/html")
    }

  }
}

