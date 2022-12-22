import sbtcrossproject.{crossProject, CrossType}

import Dependencies._

ThisBuild / scalaVersion := "3.2.0"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "net.drthum"

lazy val common = (crossProject(JSPlatform, JVMPlatform).crossType(CrossType.Pure) in file("modules/common"))
  .settings(
    name := "universal-search-common",
    libraryDependencies ++= circe
  )

lazy val backend = (project in file("modules/backend"))
  .settings(
    name := "universal-search-backend",
    libraryDependencies ++= cats ++ skunk ++ http4s ++ circe ++ logging ++ tapir
  )
  .dependsOn(common.jvm)

lazy val frontend = (project in file("modules/frontend"))
  .settings(
    name := "universal-search-frontend"
  )
  .dependsOn(common.js)
