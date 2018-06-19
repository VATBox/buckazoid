import Dependencies._

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
    ),
    bintray
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
    ),
    bintray
  )
  .dependsOn(core % "compile->compile;test->test")


val bintray = List(
  publishMavenStyle := true,
  bintrayRepository := "maven",
  bintrayOrganization := Option("vatbox-oss"),
  bintrayVcsUrl := Option("""git@github.com:VATBox/buckazoid.git"""),
  bintrayPackageLabels := Seq("scala", "money", "currency")
)
