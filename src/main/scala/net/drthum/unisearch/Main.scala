package net.drthum.unisearch

import cats.effect._
import skunk.Session
import skunk.codec.all._
import skunk.implicits._
import natchez.Trace.Implicits.noop
import net.drthum.unisearch.daos.EntityDAO
import java.util.UUID
import net.drthum.unisearch.services.SearchService

object Main extends IOApp {

  val sessionResource: Resource[IO, Session[IO]] = Session.single(
    host = "localhost",
    port = 5432,
    user = "unisearch",
    database = "unisearch",
    password = Some("unisearch")
  )

  override def run(args: List[String]): IO[ExitCode] = {
    (for {
      session <- sessionResource
      dao = new EntityDAO[IO](session)
      service = new SearchService[IO](dao)
      // _ <- service.search("11111111-1111-1111-1111-111111111111")
    } yield {
      println(service.search("424b0a68-6407-421f-bd59-dd4d72161794").unsafeRunSync()(cats.effect.unsafe.IORuntime.global))
      ExitCode.Success
    }).useForever
    /* session.use { s =>
      for {
        d <- s.unique(sql"select current_date".query(date))
        _ <- IO.println(s"The current date is $d.")
      } yield ExitCode.Success
    } */
  }

}
