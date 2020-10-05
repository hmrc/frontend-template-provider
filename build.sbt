import uk.gov.hmrc._
import DefaultBuildSettings._
import uk.gov.hmrc.{SbtBuildInfo, ShellPrompt, SbtAutoBuildPlugin}
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning
import uk.gov.hmrc.SbtArtifactory
import play.sbt.routes.RoutesKeys.routesGenerator
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin._
import sbt._
import sbt.Tests.{SubProcess, Group}

val appName: String = "frontend-template-provider"

lazy val plugins : Seq[Plugins] = Seq.empty
lazy val playSettings : Seq[Setting[_]] = Seq.empty

lazy val microservice = Project(appName, file("."))
  .enablePlugins(Seq(play.sbt.PlayScala,SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin, SbtArtifactory) ++ plugins : _*)
  .settings(playSettings ,
    scalaSettings,
    publishingSettings,
    defaultSettings(),
    scalaVersion := "2.11.11",
    PlayKeys.playDefaultPort := 9310,
    libraryDependencies ++= AppDependencies.all,
    retrieveManaged := true,
    evictionWarningOptions in update := EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
    routesGenerator := StaticRoutesGenerator,
    excludes := Seq(
      "resources/**"
    ),
    majorVersion := 0,
    unmanagedResourceDirectories in sbt.Compile += baseDirectory.value / "resources",
    resolvers ++= Seq(
      Resolver.bintrayRepo("hmrc", "releases"),
      Resolver.jcenterRepo
     )
  )

def oneForkedJvmPerTest(tests: Seq[TestDefinition]) =
  tests map {
    test => new Group(test.name, Seq(test), SubProcess(ForkOptions(runJVMOptions = Seq("-Dtest.name=" + test.name))))
  }
