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

package uk.gov.hmrc.frontendtemplateprovider.model

import play.api.libs.json._
import play.twirl.api.Html

case class TemplateRequest(title: Option[String],
                           bodyClasses: Option[String],
                           head: Html,
                           bodyEnd: Html,
                           insideHeader: Html,
                           afterHeader: Html,
                           footerTop: Html,
                           footerLinks: Option[Html],
                           nav: Boolean = false,
                           content: Html)

object TemplateRequest {
  implicit val htmlReads: Reads[Html] = new Reads[Html] {
    override def reads(json: JsValue): JsResult[Html] = {
      JsSuccess(Html(json.as[String]))
    }
  }
  implicit val templateRequestReads: Reads[TemplateRequest] = Json.reads[TemplateRequest]
}