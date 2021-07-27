import sbt._
import play.sbt.PlayImport._
import play.core.PlayVersion

object AppDependencies {
  
  private val playVersion = "play-28"

  val compile: Seq[ModuleID] = Seq(
    ws,
    "uk.gov.hmrc" %% s"bootstrap-frontend-$playVersion" % "5.7.0",
    "uk.gov.hmrc" %% "play-ui"                 % s"9.6.0-$playVersion",
    "uk.gov.hmrc" %% "domain"                  % s"6.1.0-$playVersion",
    "uk.gov.hmrc" %% "govuk-template"          % s"5.69.0-$playVersion"
  )

  val test: Seq[ModuleID] = Seq(
    "org.scalatest"                     %% "scalatest"             % "3.2.8",
    "org.pegdown"                       %  "pegdown"               % "1.6.0",
    "com.typesafe.play"                 %% "play-test"             % PlayVersion.current,
    "org.scalatestplus.play"            %% "scalatestplus-play"    % "5.1.0",
    "com.github.spullara.mustache.java" %  "compiler"              % "0.9.6",
    "com.github.spullara.mustache.java" %  "scala-extensions-2.11" % "0.9.6",
    "com.vladsch.flexmark"              % "flexmark-all"           % "0.35.10"
  ).map(_ % "test")

  val all: Seq[ModuleID] = compile ++ test
}
