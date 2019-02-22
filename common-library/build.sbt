
name := "common-library"

version := "0.0.1"

organization := "jhz7"

scalaVersion := "2.12.8"

sbtVersion := "1.2.8"

val playVersion = "2.7.0"

libraryDependencies ++=  Seq(
  "com.typesafe.play" %% "play"      % playVersion,
  "com.typesafe.play" %% "play-ws"   % playVersion,
  "com.typesafe.play" %% "play-json" % playVersion
)