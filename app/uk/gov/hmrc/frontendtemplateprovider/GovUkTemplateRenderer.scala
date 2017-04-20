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

import uk.gov.hmrc.play.microservice.controller.BaseController
import uk.gov.hmrc.play.http.logging.MdcLoggingExecutionContext._
import play.api.libs.json.{JsSuccess, Json}
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.mvc._
import play.twirl.api.Html
import uk.gov.hmrc.frontendtemplateprovider.model.TemplateRequest

import scala.concurrent.Future

object GovUkTemplateRenderer extends GovUkTemplateRenderer

trait GovUkTemplateRenderer extends BaseController {

	def hello(): Action[AnyContent] = Action.async { implicit request =>
		Future.successful(Ok(Html("Hello world")))
	}

	def renderTemplate(): Action[AnyContent] = Action.async { implicit request =>
		request.body.asJson.map { body =>
			body.validate[TemplateRequest] match {
				case maybeTemplateRequest: JsSuccess[TemplateRequest] =>
					val templateRequest = maybeTemplateRequest.get
					Future.successful(
						Ok(views.html.govMain(templateRequest.title, templateRequest.bodyClasses)(
							templateRequest.head,
							templateRequest.bodyEnd,
							templateRequest.insideHeader,
							templateRequest.afterHeader,
							templateRequest.footerTop,
							templateRequest.footerLinks,
							templateRequest.nav,
							templateRequest.content
						))
					)
				case _ => Future.successful(BadRequest)

			}

		}.getOrElse {
			Future.successful(BadRequest)
		}
	}
}
