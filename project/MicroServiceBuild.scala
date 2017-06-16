import sbt._
import play.sbt.PlayImport._
import play.core.PlayVersion

object MicroServiceBuild extends Build with MicroService {

  val appName = "frontend-template-provider"

  override lazy val appDependencies: Seq[ModuleID] = compile ++ test()

  val compile = Seq(
    ws,
    "uk.gov.hmrc" %% "microservice-bootstrap" % "5.15.0",
    "uk.gov.hmrc" %% "play-authorisation"     % "4.3.0",
    "uk.gov.hmrc" %% "play-health"            % "2.1.0",
    "uk.gov.hmrc" %% "play-url-binders"       % "2.1.0",
    "uk.gov.hmrc" %% "play-config"            % "4.3.0",
    "uk.gov.hmrc" %% "logback-json-logger"    % "3.1.0",
    "uk.gov.hmrc" %% "domain"                 % "4.1.0",
    "uk.gov.hmrc" %% "play-ui"                % "7.2.0",
    "uk.gov.hmrc" %% "govuk-template"         % "5.2.0",
    "uk.gov.hmrc" %% "play-whitelist-filter"  % "1.2.0"
  )

  def test(scope: String = "test,it") = Seq(
    "uk.gov.hmrc"            %% "hmrctest"                % "2.3.0"             % scope,
    "org.scalatest"          %% "scalatest"               % "2.2.6"             % scope,
    "org.pegdown"            %  "pegdown"                 % "1.6.0"             % scope,
    "com.typesafe.play"      %% "play-test"               % PlayVersion.current % scope,
    "uk.gov.hmrc"            %% "local-template-renderer" % "0.7.0"             % scope,
    "org.scalatestplus.play" %% "scalatestplus-play"      % "2.0.0"             % scope
  )


}
