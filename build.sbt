import Dependencies._

//In order to use this, need to set a value for BUILD_NUMBER variable on circleci config.yaml.
//can see vatboxADTs project for reference
//lazy val buildNumber = sys.props.getOrElse("BUILD_NUMBER", default = "dev")
lazy val buildNumber = "13"

val baseSettings = Seq(
  scalaVersion := "2.12.6",
  crossScalaVersions := Seq(scalaVersion.value, "2.11.12"), //, "2.10.6")
  scalacOptions := Seq(
    "-unchecked",
    "-deprecation",
    "-feature",
    "-Xlint",
    "-Ywarn-dead-code",
    "-encoding", "utf8"),
  git.useGitDescribe := true,
  coverageMinimum := 80,
  coverageFailOnMinimum := true,
  coverageHighlighting := true,
  licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  organization := "com.vatbox",
  description := "Sensible money library",
  publishTo := Some("Artifactory Realm" at "https://vatbox.jfrog.io/vatbox/sbt-local"),
  credentials ++= Seq(
    Credentials(Path.userHome / ".ivy2" / ".credentials"),
    Credentials("Artifactory Realm", "vatbox.jfrog.io", System.getenv("ART_USER"), System.getenv("ART_KEY"))
  ),
  version := s"0.0.$buildNumber"
)

lazy val root = (project in file("."))
  .enablePlugins(GitVersioning, GitBranchPrompt)
  .settings(
    baseSettings,
    name := "Buckazoid",
    description := "Sensible money library",
    skip in publish := true
  )
  .aggregate(core, contrib)


lazy val core = (project in file("core"))
  .settings(
    baseSettings,
    name := "buckazoid",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      scalactic % Test,
      scalacheck % Test,
    )
  )

lazy val contrib = (project in file("contrib"))
  .settings(
    baseSettings,
    name := "buckazoid-contrib",
    libraryDependencies ++= Seq(
      wsClient,
      wsClientJson,
      scalaTest % Test,
      scalactic % Test,
      scalacheck % Test,
      fakeWsClient % Test
    )
  )
  .dependsOn(core % "compile->compile;test->test")
