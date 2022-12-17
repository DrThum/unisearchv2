package net.drthum.unisearch.daos

import java.util.UUID

import cats.effect._
import fs2.Stream
import skunk._
import skunk.codec.all._
import skunk.implicits._

import _root_.net.drthum.unisearch.models.Mediaplan
import cats.effect.kernel.MonadCancel

class EntityDAO[F[_]](session: Session[F])(using mc: MonadCancel[F, Throwable]) {

  import EntityDAO.Queries._

  def getMediaplans(id: UUID): Stream[F, Mediaplan] = {
    for {
      ps <- Stream.resource(session.prepare(mediaplanByIdQuery))
      mediaplans <- ps.stream(id, 1)
    } yield mediaplans
  }

  def getMediaplans(namePart: String): Stream[F, Mediaplan] = {
    for {
      ps <- Stream.resource(session.prepare(mediaplanByNamePartQuery))
      mediaplans <- ps.stream(s"%$namePart%", 10)
    } yield mediaplans
  }

}

object EntityDAO {

  object Queries {

    val mediaplanByIdQuery: Query[UUID, Mediaplan] = {
      sql"""
        SELECT id, name
        FROM mediaplans
        WHERE id = $uuid
      """.query(uuid ~ varchar)
         .gmap[Mediaplan]
    }

    val mediaplanByNamePartQuery: Query[String, Mediaplan] = {
      sql"""
        SELECT id, name
        FROM mediaplans
        WHERE name ILIKE $varchar
      """.query(uuid ~ varchar)
         .gmap[Mediaplan]
    }

  }

}
