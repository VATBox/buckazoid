import Dependencies._


lazy val root = (project in file(".")).
  enablePlugins(GitVersioning, GitBranchPrompt).
  settings(
    inThisBuild(List(
      organization := "com.vatbox",
      scalaVersion := "2.12.4",
//      version      := "0.1.0-SNAPSHOT",
      crossScalaVersions := Seq(scalaVersion.value, "2.11.11"),//, "2.10.6")
      git.useGitDescribe := true,
      coverageEnabled := true,
      coverageMinimum := 80,
      coverageFailOnMinimum := true,
      coverageHighlighting := true,
      licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
    ) ++ bintray),
//    AmmoniteReplPlugin.loadSources,
    name := "Buckazoid",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      scalactic % Test,
      scalacheck % Test,
    )
  )



val bintray = List(
  publishMavenStyle := true,
  bintrayRepository := "buckazoid",
  bintrayOrganization := Option("vatbox-oss"),
  bintrayVcsUrl := Option("""git@github.com:VATBox/buckazoid.git"""),
  bintrayPackageLabels := Seq("scala", "money", "currency")
)
