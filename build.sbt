import Dependencies._


lazy val root = (project in file(".")).
  enablePlugins(GitVersioning, GitBranchPrompt).
  settings(
    scalaVersion := "2.12.4",
    crossScalaVersions := Seq(scalaVersion.value, "2.11.11"), //, "2.10.6")
    git.useGitDescribe := true,
    coverageEnabled := true,
    coverageMinimum := 80,
    coverageFailOnMinimum := true,
    coverageHighlighting := true,
    licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
    organization := "com.vatbox",
    name := "Buckazoid",
    description := "Sensible money library",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      scalactic % Test,
      scalacheck % Test,
    ),
    bintray
  )


val bintray = List(
  publishMavenStyle := true,
  bintrayRepository := "buckazoid",
  bintrayOrganization := Option("vatbox-oss"),
  bintrayVcsUrl := Option("""git@github.com:VATBox/buckazoid.git"""),
  bintrayPackageLabels := Seq("scala", "money", "currency")
)
