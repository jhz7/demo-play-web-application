name := """employee-api"""
organization := "jhz7"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  guice,
  "org.postgresql"          % "postgresql"         % "42.2.2",
  "com.h2database"          % "h2"                 % "1.4.198" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1"   % Test
)
