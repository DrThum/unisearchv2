package net.drthum.unisearch.models

import java.util.UUID

sealed trait Entity {
  def id: UUID
  def name: String
}

case class Mediaplan(id: UUID, name: String) extends Entity
