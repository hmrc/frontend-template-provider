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


import akka.stream.Materializer
import play.api.Play
import play.api.mvc.Call
import uk.gov.hmrc.whitelist.AkamaiWhitelistFilter

object WhitelistFilter extends AkamaiWhitelistFilter {
  override lazy val whitelist: Seq[String] = Play.current.configuration.getStringSeq("whitelistIps").getOrElse(Seq())
  override val destination: Call = Call("GET", "https://www.gov.uk")

  override implicit def mat: Materializer = Play.current.materializer
}