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

import java.io.{StringReader, StringWriter}

import com.github.mustachejava.DefaultMustacheFactory
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.frontendtemplateprovider.controllers.{AssetsConfig, GovUkTemplateRendererController}
import uk.gov.hmrc.http.HeaderCarrier

trait FrontendTemplateSetup {

  implicit val hc = HeaderCarrier()

  lazy val inputMap = Map[String, Any]()

  val mainTagRegex = "<main\\b[^>]*>".r
  val bodyTagRegex = "<body\\b[^>]*>".r
  val gaSetRegex = """ga\(\'set\'\, \'dimension""".r

  object TestMustacheFactory extends DefaultMustacheFactory {
    setObjectHandler(new com.twitter.mustache.ScalaObjectHandler)
  }

  lazy val c = new GovUkTemplateRendererController {
    override val assetsPrefix: String = new AssetsConfig {}.assetsPrefix // this is to avoid race condition
  }

  lazy val r = c.serveMustacheTemplate()(FakeRequest("GET", "/"))

  lazy val outputText = {
    val sw = new StringWriter()
    val m = TestMustacheFactory.compile(new StringReader(contentAsString(r)), "template")
    m.execute(sw, inputMap)
    sw.flush()
    sw.toString
  }
}
