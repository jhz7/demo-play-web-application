
name := "common-library"

version := "0.1"

organization := "jhz7"

scalaVersion := "2.12.8"

sbtVersion := "1.2.8"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.7.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)