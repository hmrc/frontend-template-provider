import sbt._
import play.sbt.PlayImport._
import play.core.PlayVersion

object MicroServiceBuild extends Build with MicroService {

  val appName = "frontend-template-provider"

  override lazy val appDependencies: Seq[ModuleID] = compile ++ test()

  val compile = Seq(
    ws,
    "uk.gov.hmrc" %% "microservice-bootstrap" % "8.5.0",
    "uk.gov.hmrc" %% "frontend-bootstrap"     % "10.7.0",
    "uk.gov.hmrc" %% "play-url-binders"       % "2.1.0",
    "uk.gov.hmrc" %% "domain"                 % "5.2.0",
    "uk.gov.hmrc" %% "play-whitelist-filter"  % "2.0.0"
  )

  def test(scope: String = "test,it") = Seq(
    "uk.gov.hmrc"                       %% "hmrctest"              % "3.2.0"             % scope,
    "org.scalatest"                     %% "scalatest"             % "3.0.0"             % scope,
    "org.pegdown"                       %  "pegdown"               % "1.6.0"             % scope,
    "com.typesafe.play"                 %% "play-test"             % PlayVersion.current % scope,
    "org.scalatestplus.play"            %% "scalatestplus-play"    % "2.0.0"             % scope,
    "com.github.spullara.mustache.java" %  "compiler"              % "0.9.5"             % scope,
    "com.github.spullara.mustache.java" %  "scala-extensions-2.11" % "0.9.5"             % scope
  )
}
