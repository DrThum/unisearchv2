import sbt._

object Dependencies {
  object Versions {
    val skunkV = "0.3.2"
    val catsEffectV = "3.4.2"
    val http4sV = "0.23.16"
    val circeV = "0.14.3"
    val logbackV = "1.4.5"
  }

  import Versions._

  lazy val skunk = Seq(
    "org.tpolecat" %% "skunk-core" % skunkV
  )

  lazy val cats = Seq(
    "org.typelevel" %% "cats-effect" % catsEffectV
  )

  lazy val http4s = Seq(
    "org.http4s" %% "http4s-core" % http4sV,
    "org.http4s" %% "http4s-dsl" % http4sV,
    "org.http4s" %% "http4s-ember-server" % http4sV,
    "org.http4s" %% "http4s-circe" % http4sV
  )

  lazy val circe = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic"
  ).map(_ % circeV)

  lazy val logging = Seq(
    "ch.qos.logback" % "logback-classic" % logbackV
  )
}
