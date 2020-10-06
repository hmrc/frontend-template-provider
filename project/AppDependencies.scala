import sbt._
import play.sbt.PlayImport._
import play.core.PlayVersion

object AppDependencies {

  private val playVersion = "play-26"

  val compile: Seq[ModuleID] = Seq(
    ws,
    "uk.gov.hmrc" %% s"bootstrap-$playVersion" % "1.16.0",
    "uk.gov.hmrc" %% "play-ui"                 % s"8.2.0-$playVersion",
    "uk.gov.hmrc" %% "domain"                  % s"5.10.0-$playVersion",
    "uk.gov.hmrc" %% "play-whitelist-filter"   % s"3.4.0-$playVersion",
    "uk.gov.hmrc" %% "govuk-template"          % s"5.57.0-$playVersion"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"                       %% "hmrctest"              % s"3.9.0-$playVersion" ,
    "org.scalatest"                     %% "scalatest"             % "3.0.8",
    "org.pegdown"                       %  "pegdown"               % "1.6.0",
    "com.typesafe.play"                 %% "play-test"             % PlayVersion.current,
    "org.scalatestplus.play"            %% "scalatestplus-play"    % "3.1.0",
    "com.github.spullara.mustache.java" %  "compiler"              % "0.9.6",
    "com.github.spullara.mustache.java" %  "scala-extensions-2.11" % "0.9.6"
  ).map(_ % "test")

  val all: Seq[ModuleID] = compile ++ test
}
