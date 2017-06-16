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

import org.scalatest.{Matchers, WordSpec}
import play.api.mvc.Results
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.frontendtemplateprovider.controllers.GovUkTemplateRendererController
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}


class GovUkTemplateRendererControllerControllerSpec extends WordSpec with Matchers  with WithFakeApplication with Results   {


  val fakeRequest = FakeRequest("GET", "/")


  "GET /serve-template" should {
    "return 200 with the template rendered correctly" in {
      val result = GovUkTemplateRendererController.serveMustacheTemplate()(fakeRequest)
      val bodyText = contentAsString(result)
      status(result) shouldBe OK
      bodyText should not contain "@resolveUrl"
      bodyText should include("html")
    }
  }
}
