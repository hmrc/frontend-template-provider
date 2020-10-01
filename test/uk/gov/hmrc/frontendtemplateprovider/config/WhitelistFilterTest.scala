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

package uk.gov.hmrc.frontendtemplateprovider.config

import org.scalatest.{Matchers, WordSpec}
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import uk.gov.hmrc.play.test.WithFakeApplication

class WhitelistFilterTest extends WordSpec with Matchers with WithFakeApplication {

  val ip = "127.0.0.1"

  override lazy val fakeApplication: Application = new GuiceApplicationBuilder()
    .configure(Map("whitelistIps" -> Seq(ip), "shouldWhitelist" -> true))
    .bindings(bindModules:_*)
    .build()

  def sut: WhitelistFilter = fakeApplication.injector.instanceOf[WhitelistFilter]

  "WhitelistFilter" should {
    "retrieve the whitelist IP addresses correctly" in {
      sut.whitelist should be(Seq(ip))
    }
  }

}
