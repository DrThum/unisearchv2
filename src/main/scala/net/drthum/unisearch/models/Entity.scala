package net.drthum.unisearch.models

import java.util.UUID
import io.circe.Encoder
import io.circe.generic.semiauto._
import io.circe.syntax._

sealed trait Entity {
  def id: UUID
  def name: String
}

case class Mediaplan(id: UUID, name: String) extends Entity

object Mediaplan {
  val mediaplanEncoder: Encoder[Mediaplan] = deriveEncoder

  given Encoder[Entity] = Encoder.instance {
    case m: Mediaplan => m.asJson(mediaplanEncoder)
  }
}
