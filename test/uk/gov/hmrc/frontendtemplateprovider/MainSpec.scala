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

package uk.gov.hmrc.frontendtemplateprovider

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite

class MainSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite {

  "Main" must {
    "not add a main class for main tag if non specified SDT 571" in new CommonSetup {
      override lazy val inputMap = Map[String, Any]()
      mainTagRegex.findFirstIn(outputText).get must not include("class")
    }

    "allow main tag to have it's mainClass SDT 571" in new CommonSetup {
      override lazy val inputMap = Map(
        "mainClass" -> "clazz"
      )
      mainTagRegex.findFirstIn(outputText).get must include("""class="clazz"""")
    }

    "allow main attributes to be specified in main tag SDT 572" in new CommonSetup {
      override lazy val inputMap = Map(
        "mainAttributes" -> """id="main""""
      )
      mainTagRegex.findFirstIn(outputText).get must include("""id="main"""")
    }

    "not show beta banner if there is no service name SDT 476" in new CommonSetup {
      override lazy val inputMap = Map[String, Any]()
      outputText must not include("""<div class="beta-banner">""")
    }

    "show beta banner when you specify a service name SDT 476" in new CommonSetup {
      override lazy val inputMap = Map(
        "betaBanner" -> Map("feedbackIdentifier" -> "PTA")
      )
      outputText must include("""<div class="beta-banner beta-banner--borderless">""")
      outputText must include("""href="http://localhost:9250/contact/beta-feedback-unauthenticated?service=PTA"""")
    }

    "show beta banner with no feedback link if you don't specify a feedbackIdentifier SDT 476" in new CommonSetup {
      override lazy val inputMap = Map(
        "betaBanner" -> true
      )
      outputText must include("""<div class="beta-banner beta-banner--borderless">""")
      outputText must not include("""href="http://localhost:9250/contact/beta-feedback-unauthenticated?service=PTA"""")
      outputText must include("This is a new service.")
    }

    "show article when passed in" in new CommonSetup {
      override lazy val inputMap = Map(
        "article" -> "<p>hello world</p>"
      )
      outputText must include("<p>hello world</p>")
    }

    "show account-menu when hideAccountMenu is not true" in new CommonSetup {
      override lazy val inputMap = Map[String, Any]()
      outputText must include("""<nav id="secondary-nav" class="account-menu" role="navigation" aria-label="Account">""")
    }

    "not show account-menu when hideAccountMenu is true" in new CommonSetup {
      override lazy val inputMap = Map(
        "hideAccountMenu" -> true
      )
      outputText must not include("""<nav id="secondary-nav" class="account-menu" role="navigation" aria-label="Account">""")
    }

    "not show the full width banner when fullWidthBannerTitle is empty" in new CommonSetup {
      override lazy val inputMap = Map[String, Any]()
      outputText must not include("""<div id="full-width-banner" class="full-width-banner">""")
    }

    "show the full width banner with no dismiss button when fullWidthBannerTitle contains text but fullWidthBannerDismissText is empty" in new CommonSetup {
      override lazy val inputMap = Map(
        "fullWidthBannerTitle" -> "Banner Title"
      )
      outputText must include("""<div id="full-width-banner" class="full-width-banner">""")
      outputText must not include("""<a class="full-width-banner__close" href="#" role="button">""")
    }

    "show the full width banner with dismiss button when when fullWidthBannerDismissText contains text" in new CommonSetup {
      override lazy val inputMap = Map(
        "fullWidthBannerTitle" -> "Banner Title",
        "fullWidthBannerDismissText" -> "Dismiss Text"
      )
      outputText must include("""<a class="full-width-banner__close" href="#" role="button">""")
    }

    "show the full width banner with a link when fullWidthBannerText and fullWidthBannerLink are not empty" in new CommonSetup {
      override lazy val inputMap = Map(
        "fullWidthBannerTitle" -> "Banner Title",
        "fullWidthBannerText" -> "Banner Text",
        "fullWidthBannerLink" -> "Banner Url"
      )
      outputText must include("""<a id="full-width-banner-link" href="Banner Url" rel="noreferrer noopener" target="_blank" data-journey-click="link - click::Banner Text">""")
    }

    "show the full width banner text without a link when fullWidthBannerText is not empty and fullWidthBannerLink is empty" in new CommonSetup {
      override lazy val inputMap = Map(
        "fullWidthBannerTitle" -> "Banner Title",
        "fullWidthBannerText" -> "Banner Text"
      )
      outputText must include(
        """                        <p>
          |                            Banner Text
          |                        </p>""".stripMargin
      )
      outputText must not include("""<a href="" target="_blank" data-journey-click="link - click::Banner Text">""")
    }

    "not contain back link elements if there is no backlinkUrl specified MTA-2897" in new CommonSetup {
      override lazy val inputMap = Map[String, Any]()

      outputText must not include("back-link-url")
    }

    "contain back link elements if there is a backlinkUrl specified MTA-2897" in new CommonSetup {
      override lazy val inputMap = Map(
        "backlinkUrl" -> "back-link-url"
      )

      outputText must include("""<a href="back-link-url" class="link-back">""")
    }
  }
}
