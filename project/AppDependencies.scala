import sbt._
import play.sbt.PlayImport._
import play.core.PlayVersion

object AppDependencies {

  val compile: Seq[ModuleID] = Seq(
    ws,
    "uk.gov.hmrc" %% "microservice-bootstrap" % "10.6.0",
    "uk.gov.hmrc" %% "frontend-bootstrap"     % "12.8.0",
    "uk.gov.hmrc" %% "play-ui"                % "7.31.0-play-25",
    "uk.gov.hmrc" %% "domain"                 % "5.3.0",
    "uk.gov.hmrc" %% "play-whitelist-filter"  % "2.0.0"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"                       %% "hmrctest"              % "3.4.0-play-25" ,
    "org.scalatest"                     %% "scalatest"             % "3.0.5",
    "org.pegdown"                       %  "pegdown"               % "1.6.0",
    "com.typesafe.play"                 %% "play-test"             % PlayVersion.current,
    "org.scalatestplus.play"            %% "scalatestplus-play"    % "2.0.1",
    "com.github.spullara.mustache.java" %  "compiler"              % "0.9.6",
    "com.github.spullara.mustache.java" %  "scala-extensions-2.11" % "0.9.6"
  ).map(_ % "test")

  val all: Seq[ModuleID] = compile ++ test
}
