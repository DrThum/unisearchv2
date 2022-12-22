package net.drthum.unisearch.models

import java.util.UUID
import io.circe.{Codec, Encoder}
import io.circe.generic.semiauto._
import io.circe.syntax._

sealed trait Entity {
  def id: UUID
  def name: String
}

case class Mediaplan(id: UUID, name: String) extends Entity

object Mediaplan {
  val mediaplanCodec: Codec[Mediaplan] = deriveCodec

  given Encoder[Entity] = Encoder.instance {
    case m: Mediaplan => m.asJson(mediaplanCodec)
  }
}
