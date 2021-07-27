import play.sbt.routes.RoutesKeys.routesGenerator
import sbt._
import uk.gov.hmrc.DefaultBuildSettings._
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin._
import de.heikoseeberger.sbtheader.{CommentBlockCreator, CommentStyle, FileType}
import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport.HeaderPattern.commentBetween

val appName: String = "frontend-template-provider"

lazy val plugins : Seq[Plugins] = Seq.empty
lazy val playSettings : Seq[Setting[_]] = Seq.empty

lazy val microservice = Project(appName, file("."))
  .enablePlugins(Seq(play.sbt.PlayScala,SbtAutoBuildPlugin, SbtDistributablesPlugin) ++ plugins : _*)
  .settings(playSettings,
    scalaSettings,
    publishingSettings,
    defaultSettings(),
    scalaVersion := "2.12.13",
    PlayKeys.playDefaultPort := 9310,
    libraryDependencies ++= AppDependencies.all,
    retrieveManaged := true,
    evictionWarningOptions in update := EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
    routesGenerator := InjectedRoutesGenerator,
    majorVersion := 0,
    headerMappings := headerMappings.value + (FileType("html") ->
      CommentStyle(new CommentBlockCreator("{{!", "  ", "}}"), commentBetween("\\{\\{!", "  ", "\\}\\}"))),
    unmanagedResourceDirectories in sbt.Compile += baseDirectory.value / "resources",
    resolvers ++= Seq(
      Resolver.jcenterRepo
     )
  )
