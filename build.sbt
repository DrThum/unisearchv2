import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
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
    libraryDependencies ++= cats ++ skunk ++ http4s ++ circe ++ logging ++ tapir,
    // Allow to read the generated JS on client
    Compile / resources += (frontend / Compile / fastOptJS).value.data,
    // Let the backend read the .map file for JS
    Compile / resources += (frontend / Compile / fastOptJS).value
      .map((x: sbt.File) => new File(x.getAbsolutePath + ".map"))
      .data,
    // Let the server read the jsdeps file
    Compile / managedResources += (frontend / Compile / packageJSDependencies / artifactPath).value,
    // Do a fastOptJS on reStart
    reStart := (reStart dependsOn (frontend / Compile / fastOptJS)).evaluated,
    // This settings makes reStart rebuild if a scala.js file changes in the frontend
    watchSources ++= (frontend / watchSources).value
  )
  .dependsOn(common.jvm)

lazy val frontend = (project in file("modules/frontend"))
  .settings(
    name := "universal-search-frontend",
    libraryDependencies ++= Seq( // TODO: move this to Dependencies.scala if possible ("value %%% can only be used within a task")
      "org.scala-js" %%% "scalajs-dom" % Versions.scalajsDomV
    ),
    // Build a js dependencies file
    packageJSDependencies/ skip := false,
    jsEnv := new org.scalajs.jsenv.nodejs.NodeJSEnv(),
    // Put the jsdeps file on a place reachable for the server
    Compile / packageJSDependencies / crossTarget := (Compile / resourceManaged).value,
  )
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(JSDependenciesPlugin)
  .dependsOn(common.js)

  // https://github.com/ChristopherDavenport/http4s-scalajsexample/blob/master/build.sbt
  // https://www.scala-js.org/doc/tutorial/basic/index.html
