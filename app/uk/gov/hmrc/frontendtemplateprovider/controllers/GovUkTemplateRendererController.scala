/*
 * Copyright 2021 HM Revenue & Customs
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

import play.api.{Configuration, Environment}
import play.api.Mode.{Dev, Test}

import javax.inject.Inject
import play.api.mvc._
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import scala.concurrent.Future
import scala.io.Source

class GovUkTemplateRendererController @Inject()(
																								 runModeConfiguration: Configuration,
																								 mcc: MessagesControllerComponents
																							 ) extends FrontendController(mcc) {

	def serveMustacheTemplate(): Action[AnyContent] = Action.async { implicit request =>

		val tpl = if (runModeConfiguration.get[String]("taas.environment") == "dev") {
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
