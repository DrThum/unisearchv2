ThisBuild / scalaVersion := "3.2.0"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "net.drthum"

import Dependencies._

lazy val root = (project in file("."))
  .settings(
    name := "Universal-search",
    libraryDependencies ++= cats ++ skunk ++ http4s
  )
