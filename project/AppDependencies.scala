import sbt._
import play.sbt.PlayImport._
import play.core.PlayVersion

object AppDependencies {
  
  private val playVersion = "play-27"

  val compile: Seq[ModuleID] = Seq(
    ws,
    "uk.gov.hmrc" %% s"bootstrap-frontend-$playVersion" % "4.3.0",
    "uk.gov.hmrc" %% "play-ui"                 % s"9.2.0-$playVersion",
    "uk.gov.hmrc" %% "domain"                  % s"5.11.0-$playVersion",
    "uk.gov.hmrc" %% "govuk-template"          % s"5.66.0-$playVersion"
  )

  val test: Seq[ModuleID] = Seq(
    "org.pegdown"                       %  "pegdown"               % "1.6.0",
    "org.scalatestplus.play"            %% "scalatestplus-play"    % "3.1.3",
    "com.github.spullara.mustache.java" %  "compiler"              % "0.9.6",
    "com.github.spullara.mustache.java" %  "scala-extensions-2.11" % "0.9.6"
  ).map(_ % "test")

  val all: Seq[ModuleID] = compile ++ test
}
