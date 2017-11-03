resolvers += Resolver.bintrayIvyRepo("vatbox-oss", "sbt-plugins")
addSbtPlugin("com.vatbox" % "sbt-ammonite" % "0.1.1")

addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.5.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.2")
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.9.3")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")
addSbtPlugin("com.github.sbt" % "sbt-cpd" % "2.0.0")