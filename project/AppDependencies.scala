import sbt._
import play.sbt.PlayImport._
import play.core.PlayVersion
import PlayCrossCompilation._

object AppDependencies {

  val compile: Seq[ModuleID] = dependencies(
      shared = Seq(ws),
    play26 = Seq("uk.gov.hmrc" %% s"bootstrap-frontend-play-26" % "4.3.0",
      "uk.gov.hmrc" %% "play-ui"                 % s"9.2.0-play-26",
      "uk.gov.hmrc" %% "domain"                  % s"5.11.0-play-26",
      "uk.gov.hmrc" %% "govuk-template"          % s"5.66.0-play-26" ),
    play27 = Seq("uk.gov.hmrc" %% s"bootstrap-frontend-play-27" % "4.3.0",
      "uk.gov.hmrc" %% "play-ui"                 % s"9.2.0-play-27",
      "uk.gov.hmrc" %% "domain"                  % s"5.11.0-play-27",
      "uk.gov.hmrc" %% "govuk-template"          % s"5.66.0-play-27" )
  )

  val test: Seq[ModuleID] = dependencies(
      shared = Seq(
        "org.pegdown"                       %  "pegdown"               % "1.6.0",
        "com.typesafe.play"                 %% "play-test"             % PlayVersion.current,
        "com.github.spullara.mustache.java" %  "compiler"              % "0.9.6",
        "com.github.spullara.mustache.java" %  "scala-extensions-2.11" % "0.9.6"),
      play26 = Seq(        "org.scalatestplus.play"            %% "scalatestplus-play"    % "3.1.0",
        "org.scalatest"                     %% "scalatest"             % "3.0.8"),
    play27 = Seq("org.scalatestplus.play"            %% "scalatestplus-play"    % "4.0.3",
      "org.scalatest"                     %% "scalatest"             % "3.0.8")
  ).map(_ % "test")

  val all: Seq[ModuleID] = compile ++ test
}
