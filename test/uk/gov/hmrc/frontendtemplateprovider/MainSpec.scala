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

import org.scalatestplus.play.OneAppPerSuite
import uk.gov.hmrc.play.test.UnitSpec

class MainSpec extends UnitSpec with OneAppPerSuite with CommonSetup {

  "Main" should {
    "not add a main class for main tag if non specified SDT 571" in new CommonSetup {
      mainTagRegex.findFirstIn(outputText).get should not include("class")
    }

    "allow main tag to have it's mainClass SDT 571" in new CommonSetup {
      override lazy val inputMap = Map(
        "mainClass" -> "clazz"
      )
      mainTagRegex.findFirstIn(outputText).get should include("""class="clazz"""")
    }

    "allow main attributes to be specified in main tag SDT 572" in new CommonSetup {
      override lazy val inputMap = Map(
        "mainAttributes" -> """id="main""""
      )
      mainTagRegex.findFirstIn(outputText).get should include("""id="main"""")
    }

    "not show beta banner if there is no service name SDT 476" in new CommonSetup {
      outputText should not include("""<div class="beta-banner">""")
    }

    "show beta banner when you specify a service name SDT 476" in new CommonSetup {
      override lazy val inputMap = Map(
        "betaBanner" -> Map("feedbackIdentifier" -> "PTA")
      )
      outputText should include("""<div class="beta-banner beta-banner--borderless">""")
      outputText should include("""href="http://localhost:9250/contact/beta-feedback-unauthenticated?service=PTA"""")
    }

    "show beta banner with no feedback link if you don't specify a feedbackIdentifier SDT 476" in new CommonSetup {
      override lazy val inputMap = Map(
        "betaBanner" -> true
      )
      outputText should include("""<div class="beta-banner beta-banner--borderless">""")
      outputText should not include("""href="http://localhost:9250/contact/beta-feedback-unauthenticated?service=PTA"""")
      outputText should include("This is a new service.")
    }

    "Show article when passed in" in new CommonSetup {
      override lazy val inputMap = Map(
        "article" -> "<p>hello world</p>"
      )
      outputText should include("<p>hello world</p>")
    }

    "Show account-menu when hideAccountMenu is not true" in new CommonSetup {
      outputText should include("""<nav id="secondary-nav" class="account-menu" role="navigation">""")
    }

    "Not show account-menu when hideAccountMenu is true" in new CommonSetup {
      override lazy val inputMap = Map(
        "hideAccountMenu" -> true
      )
      outputText should not include("""<nav id="secondary-nav" class="account-menu" role="navigation">""")
    }
  }
}
