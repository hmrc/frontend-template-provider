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

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite

class FooterSpec extends PlaySpec with GuiceOneAppPerSuite {

  "Footer" must {
    "contain links to assets-frontend JS" in new CommonSetup {
      override lazy val inputMap = Map[String, Any]()
      outputText must include ("""<script src="http://localhost:9032/assets/3.16.0/javascripts/application.min.js" type="text/javascript"></script>""")
    }

    "contain links to a specified version of assets-frontend JS" in new CommonSetup {
      override lazy val inputMap = Map(
        "assetsPath" -> "www.example.com/2/"
      )

      outputText must include ("""<script src="www.example.com/2/javascripts/application.min.js" type="text/javascript"></script>""")
    }

    "allow additional footer links to be specified SDT 477" in new CommonSetup {
      override lazy val inputMap = Map(
        "additionalFooterLinks" -> Map("url" -> "www.example.com", "text" -> "something")
      )

      outputText must include("""<li><a href="www.example.com">something</a></li>""")
    }

    "allow multiple additional footer links to be specified SDT 477" in new CommonSetup {
      override lazy val inputMap = Map(
        "additionalFooterLinks" -> Seq(
          Map("url" -> "www.example.com", "text" -> "something"),
          Map("url" -> "www.other.com", "text" -> "anything")
        )
      )

      outputText must include("""<li><a href="www.example.com">something</a></li>""")
      outputText must include("""<li><a href="www.other.com">anything</a></li>""")
    }

    "show modified terms and condition link if 'termsAndConditionFooterLink' has value" in new CommonSetup {
      override lazy val inputMap = Map(
        "termsAndConditionFooterLink" -> Map("url" -> "www.example.com", "text" -> "something")
      )

      outputText must include("""<li><a href="www.example.com" data-sso="false" data-journey-click="footer:Click:Terms and conditions">something</a></li>""")
    }

    "show default terms and condition link if 'termsAndConditionFooterLink' has no value" in new CommonSetup {
      override lazy val inputMap = Map(
        "termsAndConditionFooterLink" -> None)

      outputText must include("""<a href="/help/terms-and-conditions" data-sso="false" data-journey-click="footer:Click:Terms and conditions">""")
      outputText must include("""Terms and conditions""")
    }

    "support additional script elements in the footer of the page SDT 578" in new CommonSetup {
      override lazy val inputMap = Map(
        "scriptElems" -> Map("url" -> "www.example.com")
      )

      outputText must include("""<script src="www.example.com" type="text/javascript"></script>""")
    }

    "support multiple additional script elements in the footer of the page SDT 578" in new CommonSetup {
      override lazy val inputMap = Map(
        "scriptElems" -> Seq(
          Map("url" -> "www.example.com"),
          Map("url" -> "www.another.com")
        )
      )

      outputText must include("""<script src="www.example.com" type="text/javascript"></script>""")
      outputText must include("""<script src="www.another.com" type="text/javascript"></script>""")
    }

    "support SSO Url SDT-473" in new CommonSetup {
      override lazy val inputMap = Map(
        "ssoUrl" -> "www.example.com"
      )

      outputText must include("""<script type="text/javascript">var ssoUrl = "www.example.com";</script>""")
    }

    "no ssoUrl script when no ssoUrl given SDT-473" in new CommonSetup {
      override lazy val inputMap = Map[String, Any]()
      outputText must not include("""<script type="text/javascript">var ssoUrl = """)
    }

    "not show Google Analytics snippet" in new CommonSetup {
      override lazy val inputMap = Map[String, Any]()
      outputText must not include("""(window,document,'script','//www.google-analytics.com/analytics.js','ga')""")
    }

    "support inline script elements in the footer of the page" in new CommonSetup {
      override lazy val inputMap = Map(
        "inlineScript" -> "<script>console.log('hello world');</script>"
      )

      outputText must include("""<script>console.log('hello world');</script>""")
    }

    "must include session timeout if set to true" in new CommonSetup {
      override lazy val inputMap = Map(
        "sessionTimeout" -> Map(
          "keepAliveUrl" -> "/keepAliveUrl/",
          "logoutUrl" -> "/logoutUrl/"
        )
      )
      outputText must include("""$.timeoutDialog({timeout: 900, countdown: 60, keep_alive_url: '/keepAliveUrl/', logout_url: '/logoutUrl/'});""")
    }

    "must not include session timeout if set to true" in new CommonSetup {
      override lazy val inputMap = Map[String, Any]()

      outputText must not include("""$.timeoutDialog({timeout: 900, countdown: 60, keep_alive_url: '/keepAliveUrl/', logout_url: '/logoutUrl/'});""")
    }
  }
}
