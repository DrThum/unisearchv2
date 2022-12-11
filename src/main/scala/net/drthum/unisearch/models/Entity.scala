package net.drthum.unisearch.models

import java.util.UUID

sealed trait Entity

case class Mediaplan(id: UUID, name: String) extends Entity
