import sbt._

object Dependencies {
  object Versions {
    val skunkV = "0.3.2"
    val catsEffectV = "3.4.2"
  }

  import Versions._

  lazy val skunk = Seq(
    "org.tpolecat" %% "skunk-core" % skunkV
  )

  lazy val cats = Seq(
    "org.typelevel" %% "cats-effect" % catsEffectV
  )
}
