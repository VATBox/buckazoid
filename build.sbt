import Dependencies._


lazy val root = (project in file("."))
  .enablePlugins(GitVersioning, GitBranchPrompt)
  .settings(
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
    name := "Buckazoid",
    description := "Sensible money library",
    skip / publish := true,
    bintray
  )
  .aggregate(core, contrib)


lazy val core = (project in file("core"))
  .settings(
    name := "buckazoid",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      scalactic % Test,
      scalacheck % Test,
    ),
  )

lazy val contrib = (project in file("contrib"))
  .settings(
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


val bintray = List(
  publishMavenStyle := true,
  bintrayRepository := "maven",
  bintrayOrganization := Option("vatbox-oss"),
  bintrayVcsUrl := Option("""git@github.com:VATBox/buckazoid.git"""),
  bintrayPackageLabels := Seq("scala", "money", "currency")
)
