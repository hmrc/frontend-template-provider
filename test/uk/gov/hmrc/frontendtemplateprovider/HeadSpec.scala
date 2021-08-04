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

class HeadSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite {

  "Head" must {
    "contain IE links" in new CommonSetup {
      override lazy val inputMap = Map[String, Any]()
      outputText must include("""<!--[if IE 6]><link href="http://localhost:9310/template/assets/stylesheets/govuk-template-ie6.css" media="screen" rel="stylesheet" type="text/css" /><![endif]-->""")
      outputText must include("""<!--[if IE 7]><link href="http://localhost:9310/template/assets/stylesheets/govuk-template-ie7.css" media="screen" rel="stylesheet" type="text/css" /><![endif]-->""")
      outputText must include("""<!--[if IE 8]><link href="http://localhost:9310/template/assets/stylesheets/govuk-template-ie8.css" media="screen" rel="stylesheet" type="text/css" /><![endif]-->""")
      outputText must include("""<!--[if gt IE 8]><!--><link href="http://localhost:9310/template/assets/stylesheets/govuk-template.css" media="screen" rel="stylesheet" type="text/css" /><!--<![endif]-->""")
    }

    "contain links to assets-frontend CSS" in new CommonSetup {
      override lazy val inputMap = Map[String, Any]()
      outputText must include("""<link rel="stylesheet" href="http://localhost:9032/assets/3.19.0/stylesheets/application-ie7.min.css" />""")
      outputText must include("""<link rel="stylesheet" href="http://localhost:9032/assets/3.19.0/stylesheets/application-ie.min.css" />""")
      outputText must include("""<link rel="stylesheet" href="http://localhost:9032/assets/3.19.0/stylesheets/application.min.css" />""")
    }

    "contain links to a specified version of assets-frontend CSS" in new CommonSetup {
      override lazy val inputMap = Map(
        "assetsPath" -> "www.example.com/2/"
      )

      outputText must include("""<link rel="stylesheet" href="www.example.com/2/stylesheets/application-ie7.min.css" />""")
      outputText must include("""<link rel="stylesheet" href="www.example.com/2/stylesheets/application-ie.min.css" />""")
      outputText must include("""<link rel="stylesheet" href="www.example.com/2/stylesheets/application.min.css" />""")
    }

    "contain a body opening tag that does not contain a class if `bodyClass` is not specified" in new CommonSetup {
      override lazy val inputMap = Map[String, Any]()

      bodyTagRegex.findFirstIn(outputText).get must not include("class")
    }

    "contain a body opening tag that contains a `bodyClass` if one is specified " in new CommonSetup {
      override lazy val inputMap = Map(
        "bodyClass" -> "clazz"
      )

      bodyTagRegex.findFirstIn(outputText).get must include("""class="clazz"""")
    }

    "contains no optimizely script" in new CommonSetup {
      override lazy val inputMap = Map[String, Any]()

      outputText must not include("optimizely")
      outputText must not include("<script src=''") // must not include a script with no src
    }

    "contain optimizely audience variable if provided" in new CommonSetup {
      override lazy val inputMap = Map(
        "optimizely" -> Map(
          "audience" -> "userGroup",
          "projectId" -> "id123"
        )
      )

      outputText must include("var audience = \"userGroup\"")
    }

    "not contain optimizely audience variable if not provided" in new CommonSetup {
      override lazy val inputMap = Map(
        "optimizelyProjectId" -> "id123"
      )

      outputText must not include("var audience")
    }

    "contain the default title no custom one is supplied" in new CommonSetup {
      override lazy val inputMap = Map[String, Any]()

      outputText must include("GOV.UK - The best place to find government services and information")
    }

    "contain custom title if one is supplied" in new CommonSetup {
      override lazy val inputMap = Map(
        "pageTitle" -> "My very own title."
      )

      outputText must include("My very own title.")
    }

    "contain link elements pointing to urls supplied in linkElems" in new CommonSetup {
      override lazy val inputMap = Map(
        "linkElems" -> List(
          Map("url" -> "www.example.com/some.css"),
          Map("url" -> "www.example.com/other.css")
        )
      )

      outputText must include("""<link rel="stylesheet" type="text/css" href="www.example.com/some.css" />""")
      outputText must include("""<link rel="stylesheet" type="text/css" href="www.example.com/other.css" />""")
    }

    "contain link element for IE 8 if 'IE 8' is passed as a condition in linkElems" in new CommonSetup {
      override lazy val inputMap = Map(
        "linkElems" -> Map(
          "url" -> "www.example.com/ie8.css",
          "ieVersionCondition" -> "IE 8"
        )
      )

      outputText.filterNot(_=='\n').replaceAll(">[ ]*<","><") must include("""<!--[if IE 8]><link rel="stylesheet" type="text/css" href="www.example.com/ie8.css" /><![endif]-->""")
    }

    "support specifying print media type for linkElems SDT-552" in new CommonSetup {
      override lazy val inputMap = Map(
        "linkElems" -> Map("url" -> "www.example.com/some.css", "print" -> true)
      )

      outputText must include("""<link rel="stylesheet" type="text/css" href="www.example.com/some.css" media="print"/>""")
    }

    "not contain navigation element if there is no navTitle specified SDT-474" in new CommonSetup {
      override lazy val inputMap = Map[String, Any]()

      outputText must include("""<div class="header-proposition">""")
      outputText must not include("""<a href="#proposition-links" class="js-header-toggle menu">Menu</a>""")
      outputText must not include("""<ul id="proposition-links" class="header__menu__proposition-links">""")
      outputText must not include("""<span class="header__menu__proposition-name">""")
    }

    "contain correct navigation header when only navTitle is specified SDT-474" in new CommonSetup {
      override lazy val inputMap = Map(
        "navTitle" -> "My service"
      )

      outputText must include("""<div class="header-proposition">""")
      outputText must include("""<span class="header__menu__proposition-name">My service</span>""")
      outputText must not include("""<a href="#proposition-links" class="js-header-toggle menu">Menu</a>""")
      outputText must not include("""<ul id="proposition-links" class="header__menu__proposition-links">""")
    }

    "not show proposition links element if showPropositionLinks not specified MTA-2897" in new CommonSetup {
      override lazy val inputMap = Map[String, Any]()

      outputText must not include("""header__menu__proposition-links"""")
    }

    "not show proposition links element if showPropositionLinks set to false MTA-2897" in new CommonSetup {
      override lazy val inputMap = Map(
        "showPropositionLinks" -> false
      )

      outputText must not include("""header__menu__proposition-links"""")
    }

    "show proposition links element if showPropositionLinks set to true MTA-2897" in new CommonSetup {
      override lazy val inputMap = Map(
        "showPropositionLinks" -> true
      )

      outputText must include("""<ul id="proposition-links" class="header__menu__proposition-links">""")
    }

    "not show signout link in proposition menu if the account menu isn't hidden even if signout url is supplied" in new CommonSetup {
      override def inputMap =  Map(
        "showPropositionLinks" -> true,
        "signOutUrl" -> "/signout/url",
        "hideAccountMenu" -> false
      )

      outputText must not include("""<a id="logOutNavHref"""")
    }

    "show signout link in proposition menu if the account menu is hidden and signout url is supplied" in new CommonSetup {
      override def inputMap =  Map(
        "showPropositionLinks" -> true,
        "signOutUrl" -> "/signout/url",
        "hideAccountMenu" -> true
      )
      outputText must include("""<a id="logOutNavHref" href="/signout/url"""")
    }

    "not show the organisation logo if showOrganisationLogo is not supplied" in new CommonSetup {
      override lazy val inputMap = Map[String, Any]()

      outputText must not include("""organisation-logo""")
    }

    "not show the organisation logo showOrganisationLogo is false" in new CommonSetup {
      override def inputMap = Map(
        "showOrganisationLogo" -> false
      )

      outputText must not include("""organisation-logo""")
    }

    "show the organisation logo showOrganisationLogo is true" in new CommonSetup {
      override def inputMap = Map(
        "showOrganisationLogo" -> true
      )

      outputText must include("""<span class="organisation-logo organisation-logo-medium">HM Revenue &amp; Customs</span>""")
    }

    "Beta banner feedback form must be authenticated if user is authenticated" in new CommonSetup {
      override lazy val inputMap = Map(
        "betaBanner" -> true,
        "feedbackIdentifier" -> "test-service",
        "authenticatedUser" -> true
      )

      outputText must include("""<a id="feedback-link" href="http://localhost:9250/contact/beta-feedback?service=""")
    }

    "Beta banner feedback form must be authenticated if user is not authenticated" in new CommonSetup {
      override lazy val inputMap = Map(
        "betaBanner" -> true,
        "feedbackIdentifier" -> "test-service",
        "authenticatedUser" -> false
      )

      outputText must include("""<a id="feedback-link" href="http://localhost:9250/contact/beta-feedback-unauthenticated?service=""")
    }

    "Beta banner feedback form must be authenticated if user is not specified" in new CommonSetup {
      override lazy val inputMap = Map(
        "betaBanner" -> true,
        "feedbackIdentifier" -> "test-service"
      )

      outputText must include("""<a id="feedback-link" href="http://localhost:9250/contact/beta-feedback-unauthenticated?service=""")
    }

    "not contain language selection element if there is no langSelector specified MTA-2897" in new CommonSetup {
      override lazy val inputMap = Map(
        "showPropositionLinks" -> true
      )

      outputText must not include("""class="language-select"""")
    }

    "not contain language selection element if there is langSelector specified but no Welsh url MTA-2897" in new CommonSetup {
      override lazy val inputMap = Map(
        "showPropositionLinks" -> true,
        "langSelector" -> Map(
          "enUrl" -> "english-language-url",
          "cyUrl" -> None
        )
      )

      outputText must not include("""class="language-select"""")
    }

    "not contain language selection element if there is langSelector specified but no English url MTA-2897" in new CommonSetup {
      override lazy val inputMap = Map(
        "showPropositionLinks" -> true,
        "langSelector" -> Map(
          "enUrl" -> None,
          "cyUrl" -> "welsh-language-url"
        )
      )

      outputText must not include("""class="language-select"""")
    }

    "contain correct language selection element if there are both English and Welsh Urls specified and the language is English MTA-2897" in new CommonSetup {
      override lazy val inputMap = Map(
        "showPropositionLinks" -> true,
        "langSelector" -> Map(
          "enUrl" -> "english-language-url",
          "cyUrl" -> "welsh-language-url"
        ),
        "isWelsh" -> false
      )

      outputText must include("""<span class="faded-text--small bold" aria-hidden="true">English |</span>""")
      outputText must include("""<a id="switchToWelsh" lang="cy" href="welsh-language-url" data-journey-click="link - click:lang-select:Cymraeg" aria-label="Dewiswch Cymraeg">Cymraeg</a>""")
    }

    "contain correct language selection element if there are both English and Welsh Urls specified and the language is Welsh MTA-2897" in new CommonSetup {

      override lazy val inputMap = Map(
        "showPropositionLinks" -> true,
        "langSelector" -> Map(
          "enUrl" -> "english-language-url",
          "cyUrl" -> "welsh-language-url"
          ),
        "isWelsh" -> true
      )

      outputText must include("""<a id="switchToEnglish" lang="en" href="english-language-url" data-journey-click="link - click:lang-select:English" aria-label="Select English">English</a>""")
      outputText must include("""<span class="faded-text--small bold" aria-hidden="true">| Cymraeg</span>""")
    }
  }
}
