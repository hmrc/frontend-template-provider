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

import play.api.mvc._
import uk.gov.hmrc.play.config.ServicesConfig
import uk.gov.hmrc.play.microservice.controller.BaseController

import scala.concurrent.Future

object GovUkTemplateRenderer extends GovUkTemplateRenderer

trait GovUkTemplateRenderer extends BaseController with ServicesConfig {


	def serveMustacheTemplate(): Action[AnyContent] = Action.async { implicit request =>
		val resolveUrl: (String) => String = a => s"${baseUrl("frontend-template-provider")}/template/assets/$a"
		Future.successful(Ok(views.txt.Application.gov_main_mustache(resolveUrl)))
	}

}
