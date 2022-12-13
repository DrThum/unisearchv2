package net.drthum.unisearch.services

import net.drthum.unisearch.daos.EntityDAO
import net.drthum.unisearch.models.Entity
import java.util.UUID
import scala.util.Try
import cats.Functor
import cats.syntax.functor._
import cats.effect.kernel.Sync
import fs2.Stream

class SearchService[F[_]: Functor: Sync](dao: EntityDAO[F]) {

  def search(search: String): Stream[F, Entity] = {
    Try(UUID.fromString(search))
      .toOption
      .fold(searchByName(search))(searchById)
  }

  private def searchById(id: UUID): Stream[F, Entity] = {
    dao.getMediaplans(id)
  }

  private def searchByName(name: String): Stream[F, Entity] = ???

}
