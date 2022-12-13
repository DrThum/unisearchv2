package net.drthum.unisearch.models

import java.util.UUID
import io.circe.Encoder
import io.circe.generic.semiauto._

sealed trait Entity {
  def id: UUID
  def name: String
}

case class Mediaplan(id: UUID, name: String) extends Entity

object Mediaplan {
  given encoder: Encoder[Mediaplan] = deriveEncoder
}
