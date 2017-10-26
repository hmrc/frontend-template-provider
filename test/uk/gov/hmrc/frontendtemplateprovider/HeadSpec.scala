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

class HeadSpec extends UnitSpec with OneAppPerSuite with CommonSetup {

  "Head" should {
    "contain IE links" in new CommonSetup {
      outputText should include("""<!--[if IE 6]><link href="http://localhost:9310/template/assets/stylesheets/govuk-template-ie6.css" media="screen" rel="stylesheet" type="text/css" /><![endif]-->""")
      outputText should include("""<!--[if IE 7]><link href="http://localhost:9310/template/assets/stylesheets/govuk-template-ie7.css" media="screen" rel="stylesheet" type="text/css" /><![endif]-->""")
      outputText should include("""<!--[if IE 8]><link href="http://localhost:9310/template/assets/stylesheets/govuk-template-ie8.css" media="screen" rel="stylesheet" type="text/css" /><![endif]-->""")
      outputText should include("""<!--[if gt IE 8]><!--><link href="http://localhost:9310/template/assets/stylesheets/govuk-template.css" media="screen" rel="stylesheet" type="text/css" /><!--<![endif]-->""")
    }

    "contain links to assets-frontend CSS" in new CommonSetup {
      outputText should include("""<link rel="stylesheet" href="http://localhost:9032/assets/2.252.0/stylesheets/application-ie7.min.css" />""")
      outputText should include("""<link rel="stylesheet" href="http://localhost:9032/assets/2.252.0/stylesheets/application-ie.min.css" />""")
      outputText should include("""<link rel="stylesheet" href="http://localhost:9032/assets/2.252.0/stylesheets/application.min.css" />""")
    }

    "contain links to a specified version of assets-frontend CSS" in new CommonSetup {
      override lazy val inputMap = Map(
        "assetsPath" -> "www.example.com/2/"
      )

      outputText should include("""<link rel="stylesheet" href="www.example.com/2/stylesheets/application-ie7.min.css" />""")
      outputText should include("""<link rel="stylesheet" href="www.example.com/2/stylesheets/application-ie.min.css" />""")
      outputText should include("""<link rel="stylesheet" href="www.example.com/2/stylesheets/application.min.css" />""")
    }

    "contain a body opening tag that does not contain a class if `bodyClass` is not specified" in new CommonSetup {
      bodyTagRegex.findFirstIn(outputText).get should not include("class")
    }

    "contain a body opening tag that contains a `bodyClass` if one is specified " in new CommonSetup {
      override lazy val inputMap = Map(
        "bodyClass" -> "clazz"
      )

      bodyTagRegex.findFirstIn(outputText).get should include("""class="clazz"""")
    }

    "contains no optimizely script SDT-471" in new CommonSetup {
      outputText should not include("optimizely")
      outputText should not include("<script src=''") // should not include a script with no src
    }

    "contain no optimizely script if only baseUrl is provided SDT-471" in new CommonSetup {
      override lazy val inputMap = Map(
        "optimizelyBaseUrl" -> "cdn.optimizely.com"
      )

      outputText should not include("cdn.optimizely.com")
    }

    "contain optimizely script if only projectId is provided SDT-471" in new CommonSetup {
      override lazy val inputMap = Map(
        "optimizelyProjectId" -> "id123"
      )

      outputText should include("""<script src="//cdn.optimizely.com/js/id123.js" type="text/javascript"></script>""")
    }

    "contain optimizely script if both url and projectId is provided SDT-470" in new CommonSetup {
      override lazy val inputMap = Map(
        "optimizelyBaseUrl" -> "cdn.optimizely.com/",
        "optimizelyProjectId" -> "id123"
      )

      outputText should include("""<script src="cdn.optimizely.com/id123.js" type="text/javascript"></script>""")
    }

    "contain default title if not specified SDT 480" in new CommonSetup {
      outputText should include("GOV.UK - The best place to find government services and information")
    }

    "contain default title if not specified in SDT 480" in new CommonSetup {
      override lazy val inputMap = Map(
        "pageTitle" -> "My very own title."
      )

      outputText should include("My very own title.")
    }

    "contain no headScripts even if you specify them SDT 472" in new CommonSetup {
      override lazy val inputMap = Map(
        "headScript" -> "<script src='www.example.com/script.js' type='text/javascript'></script>"
      )

      outputText should not include("<script src='www.example.com/script.js' type='text/javascript'></script>")
    }

    "contain link elem if url is passed through in a list of maps SDT-552" in new CommonSetup {
      override lazy val inputMap = Map(
        "linkElems" -> Map("url" -> "www.example.com/some.css")
      )

      outputText should include("""<link rel="stylesheet" type="text/css" href="www.example.com/some.css" />""")
    }

    "contain link elem for ie if IE is passed as a condition" in new CommonSetup {
      override lazy val inputMap = Map(
        "linkElems" -> Map(
          "url" -> "www.example.com/ie8.css",
          "ieVersionCondition" -> "IE 8"
        )
      )

      outputText.filterNot((x: Char) => x == '\n') .replaceAll(">[ ]*<","><") should include("""<!--[if IE 8]><link rel="stylesheet" type="text/css" href="www.example.com/ie8.css" /><![endif]-->""")
    }

    "support specifying print media type for linkElems SDT-552" in new CommonSetup {
      override lazy val inputMap = Map(
        "linkElems" -> Map("url" -> "www.example.com/some.css", "print" -> true)
      )

      outputText should include("""<link rel="stylesheet" type="text/css" href="www.example.com/some.css" media="print"/>""")
    }

    "contain multiple link elems multiple elements in the list SDT-552" in new CommonSetup {
      override lazy val inputMap = Map(
        "linkElems" -> List(
          Map("url" -> "www.example.com/some.css"),
          Map("url" -> "www.example.com/other.css", "print" -> true)
        )
      )

      outputText should include("""<link rel="stylesheet" type="text/css" href="www.example.com/some.css" />""")
      outputText should include("""<link rel="stylesheet" type="text/css" href="www.example.com/other.css" media="print"/>""")
    }

    "not contain navigation element if there is no navTitle specified SDT-474" in new CommonSetup {
      outputText should include("""<div class="header-proposition">""")
      outputText should not include("""<a href="#proposition-links" class="js-header-toggle menu">Menu</a>""")
      outputText should not include("""<ul id="proposition-links" class="header__menu__proposition-links">""")
      outputText should not include("""<span class="header__menu__proposition-name">""")
    }

    "contain correct navigation header when only navTitle is specified SDT-474" in new CommonSetup {
      override lazy val inputMap = Map(
        "navTitle" -> "My service"
      )

      outputText should include("""<div class="header-proposition">""")
      outputText should include("""<span class="header__menu__proposition-name">My service</span>""")
      outputText should not include("""<a href="#proposition-links" class="js-header-toggle menu">Menu</a>""")
      outputText should not include("""<ul id="proposition-links" class="header__menu__proposition-links">""")
    }


    "service-info included by default" in new CommonSetup {
      outputText should include("""<div class="service-info">""")
    }

    "Beta banner feedback form should be authenticated if user is authenticated" in new CommonSetup {
      override lazy val inputMap = Map(
        "betaBanner" -> true,
        "feedbackIdentifier" -> "test-service",
        "authenticatedUser" -> true
      )

      outputText should include("""<a id="feedback-link" href="http://localhost:9250/contact/beta-feedback?service=""")
    }

    "Beta banner feedback form should be authenticated if user is not authenticated" in new CommonSetup {
      override lazy val inputMap = Map(
        "betaBanner" -> true,
        "feedbackIdentifier" -> "test-service",
        "authenticatedUser" -> false
      )

      outputText should include("""<a id="feedback-link" href="http://localhost:9250/contact/beta-feedback-unauthenticated?service=""")
    }

    "Beta banner feedback form should be authenticated if user is not specified" in new CommonSetup {
      override lazy val inputMap = Map(
        "betaBanner" -> true,
        "feedbackIdentifier" -> "test-service"
      )

      outputText should include("""<a id="feedback-link" href="http://localhost:9250/contact/beta-feedback-unauthenticated?service=""")
    }
  }
}
