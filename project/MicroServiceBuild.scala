import sbt._
import play.sbt.PlayImport._
import play.core.PlayVersion

object MicroServiceBuild extends Build with MicroService {

  val appName = "frontend-template-provider"

  override lazy val appDependencies: Seq[ModuleID] = compile ++ test()

  val compile = Seq(
    ws,
    "uk.gov.hmrc" %% "microservice-bootstrap" % "10.6.0",
    "uk.gov.hmrc" %% "frontend-bootstrap"     % "12.7.0",
    "uk.gov.hmrc" %% "play-ui"                % "7.31.0-play-25",
    "uk.gov.hmrc" %% "domain"                 % "5.3.0",
    "uk.gov.hmrc" %% "play-whitelist-filter"  % "2.0.0"
  )

  def test(scope: String = "test,it") = Seq(
    "uk.gov.hmrc"                       %% "hmrctest"              % "3.4.0-play-25"             % scope,
    "org.scalatest"                     %% "scalatest"             % "3.0.5"             % scope,
    "org.pegdown"                       %  "pegdown"               % "1.6.0"             % scope,
    "com.typesafe.play"                 %% "play-test"             % PlayVersion.current % scope,
    "org.scalatestplus.play"            %% "scalatestplus-play"    % "2.0.1"             % scope,
    "com.github.spullara.mustache.java" %  "compiler"              % "0.9.6"             % scope,
    "com.github.spullara.mustache.java" %  "scala-extensions-2.11" % "0.9.6"             % scope
  )
}
