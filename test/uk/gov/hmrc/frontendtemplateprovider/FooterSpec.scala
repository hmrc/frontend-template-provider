/*
 * Copyright 2018 HM Revenue & Customs
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

class FooterSpec extends UnitSpec with OneAppPerSuite {

  "Footer" should {
    "contain links to assets-frontend JS" in new CommonSetup {
      override lazy val inputMap = Map[String, Any]()
      outputText should include ("""<script src="http://localhost:9032/assets/3.2.3/javascripts/application.min.js" type="text/javascript"></script>""")
    }

    "contain links to a specified version of assets-frontend JS" in new CommonSetup {
      override lazy val inputMap = Map(
        "assetsPath" -> "www.example.com/2/"
      )

      outputText should include ("""<script src="www.example.com/2/javascripts/application.min.js" type="text/javascript"></script>""")
    }

    "allow additional footer links to be specified SDT 477" in new CommonSetup {
      override lazy val inputMap = Map(
        "additionalFooterLinks" -> Map("url" -> "www.example.com", "text" -> "something")
      )

      outputText should include("""<li><a href="www.example.com">something</a></li>""")
    }

    "allow multiple additional footer links to be specified SDT 477" in new CommonSetup {
      override lazy val inputMap = Map(
        "additionalFooterLinks" -> Seq(
          Map("url" -> "www.example.com", "text" -> "something"),
          Map("url" -> "www.other.com", "text" -> "anything")
        )
      )

      outputText should include("""<li><a href="www.example.com">something</a></li>""")
      outputText should include("""<li><a href="www.other.com">anything</a></li>""")
    }

    "show modified terms and condition link if 'termsAndConditionFooterLink' has value" in new CommonSetup {
      override lazy val inputMap = Map(
        "termsAndConditionFooterLink" -> Map("url" -> "www.example.com", "text" -> "something")
      )

      outputText should include("""<li><a href="www.example.com" target="_blank" data-sso="false" data-journey-click="footer:Click:Terms and conditions">something</a></li>""")
    }

    "show default terms and condition link if 'termsAndConditionFooterLink' has no value" in new CommonSetup {
      override lazy val inputMap = Map(
        "termsAndConditionFooterLink" -> None)

      outputText should include("""<a href="/help/terms-and-conditions" target="_blank" data-sso="false" data-journey-click="footer:Click:Terms and conditions">""")
      outputText should include("""Terms and conditions""")
    }

    "support additional script elements in the footer of the page SDT 578" in new CommonSetup {
      override lazy val inputMap = Map(
        "scriptElems" -> Map("url" -> "www.example.com")
      )

      outputText should include("""<script src="www.example.com" type="text/javascript"></script>""")
    }

    "support multiple additional script elements in the footer of the page SDT 578" in new CommonSetup {
      override lazy val inputMap = Map(
        "scriptElems" -> Seq(
          Map("url" -> "www.example.com"),
          Map("url" -> "www.another.com")
        )
      )

      outputText should include("""<script src="www.example.com" type="text/javascript"></script>""")
      outputText should include("""<script src="www.another.com" type="text/javascript"></script>""")
    }

    "support SSO Url SDT-473" in new CommonSetup {
      override lazy val inputMap = Map(
        "ssoUrl" -> "www.example.com"
      )

      outputText should include("""<script type="text/javascript">var ssoUrl = "www.example.com";</script>""")
    }

    "no ssoUrl script when no ssoUrl given SDT-473" in new CommonSetup {
      override lazy val inputMap = Map[String, Any]()
      outputText should not include("""<script type="text/javascript">var ssoUrl = """)
    }

    "not show Google Analytics snippet when no googleAnalytics given SDT-475" in new CommonSetup {
      override lazy val inputMap = Map[String, Any]()
      outputText should not include("""(window,document,'script','//www.google-analytics.com/analytics.js','ga')""")
    }

    "show Google Analytics snippet when googleAnalytics trackingId given SDT-475" in new CommonSetup {
      override lazy val inputMap = Map(
        "googleAnalytics" -> Map("trackingId" -> "UA-XXXX-Y")
      )

      outputText should include("""ga('create', 'UA-XXXX-Y', 'auto');""")
    }

    "do not add ga set if no gaSetParams given" in new CommonSetup {
      override lazy val inputMap = Map[String, Any]()
      outputText should not include("""ga('set'""")
    }

    "set the correct dimensions in ga" in new CommonSetup {
      override lazy val inputMap = Map(
        "googleAnalytics" -> Map(
          "trackingId" -> "random",
          "authProvider" -> "IDA",
          "confidenceLevel" -> "200"
        )
      )

      gaSetRegex.findAllIn(outputText).length shouldBe 2

      outputText should include("'dimension38', 'IDA'")
      outputText should include("'dimension39', '200'")
    }

    "not set the dimensions in ga when the data is blank" in new CommonSetup {
      val authProvider = "IDA"
      val confidenceLevel = "200"

      override lazy val inputMap = Map(
        "googleAnalytics" -> Map(
          "trackingId" -> "random",
          "authProvider" -> "IDA",
          "confidenceLevel" -> null
        )
      )

      gaSetRegex.findAllIn(outputText).length shouldBe 1

      outputText should include("'dimension38', 'IDA'")
    }

    "support custom cookie domain for Google Analytics snippet SDT-475" in new CommonSetup {
      override lazy val inputMap = Map(
        "googleAnalytics" -> Map(
          "trackingId" -> "UA-XXXX-Y",
          "cookieDomain" -> "example.com"
        )
      )

      outputText should include("""ga('create', 'UA-XXXX-Y', 'example.com');""")
    }

    "support inline script elements in the footer of the page" in new CommonSetup {
      override lazy val inputMap = Map(
        "inlineScript" -> "<script>console.log('hello world');</script>"
      )

      outputText should include("""<script>console.log('hello world');</script>""")
    }
  }
}
