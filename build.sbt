organization := "com.github.gphat"

name := "censorinus"

scalaVersion := "2.12.2"
crossScalaVersions := Seq("2.11.11", "2.12.2")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

libraryDependencies += "com.google.protobuf" % "protobuf-java" % "3.2.0"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.3" % "test"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.3" % "test"

releasePublishArtifactsAction := PgpKeys.publishSigned.value

Publish.settings

scalastyleFailOnError := true
