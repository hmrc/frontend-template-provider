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

package uk.gov.hmrc.frontendtemplateprovider.controllers

import play.api.Play
import play.api.mvc._
import uk.gov.hmrc.play.config.ServicesConfig
import uk.gov.hmrc.play.microservice.controller.BaseController

import scala.concurrent.Future
import scala.io.Source

object GovUkTemplateRendererController extends GovUkTemplateRendererController

trait AssetsConfig {
	lazy val assetsUrl = Play.current.configuration.getString("assets.url").getOrElse("")
	lazy val assetsVersion = Play.current.configuration.getString("assets.version").getOrElse("")
	lazy val assetsPrefix = assetsUrl + assetsVersion
}
object AssetsConfig extends AssetsConfig

trait GovUkTemplateRendererController extends BaseController with ServicesConfig {

	val assetsPrefix: String = AssetsConfig.assetsPrefix

	def serveMustacheTemplate(): Action[AnyContent] = Action.async { implicit request =>

		val tpl = if(env == "Test" || env == "Dev") {
			Source.fromInputStream(getClass.getResourceAsStream("/govuk-template.mustache.html")).mkString
				.replaceAll("""href="/contact""",          """href="http://localhost:9250/contact""")
				.replaceAll("""href="/template""",         """href="http://localhost:9310/template""")
				.replaceAll("""src="/template""",          """src="http://localhost:9310/template""")
				.replaceAll("""href="/assets""",           """href="http://localhost:9032/assets""")
				.replaceAll("""src="/assets""",            """src="http://localhost:9032/assets""")
				.replaceAll("""href="/personal-account""", """href="http://localhost:9232/personal-account""")
				.replaceAll("""href="/track""",            """href="http://localhost:9100/track""")
				.replaceAll("""href="/business-account""", """href="http://localhost:9020/business-account""")
				.replaceAll("""href="/trusted-helpers""",  """href="http://localhost:9231/trusted-helpers""")
				.replaceAll("""href="/contact""",          """href="http://localhost:9250/contact""")
		}
		else {
			Source.fromInputStream(getClass.getResourceAsStream("/govuk-template.mustache.html")).mkString
		}

		Future.successful(Ok(tpl).as("text/html"))
	}

}
