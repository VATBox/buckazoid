import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
  lazy val scalactic = "org.scalactic" %% "scalactic" % "3.0.5"
  lazy val scalacheck = "org.scalacheck" %% "scalacheck" % "1.14.0"
//  lazy val scalaMock = "org.scalamock" %% "scalamock" % "4.1.0"
  lazy val fakeWsClient = "org.f100ded.play" %% "play-fake-ws-standalone" % "1.1.0"

  val playWsStandaloneVersion = "1.1.9"
  lazy val wsClient = "com.typesafe.play" %% "play-ahc-ws-standalone" % playWsStandaloneVersion % Optional
  lazy val wsClientJson = "com.typesafe.play" %% "play-ws-standalone-json" % playWsStandaloneVersion % Optional
}
