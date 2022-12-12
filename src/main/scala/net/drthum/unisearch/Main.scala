package net.drthum.unisearch

import cats.effect._
import skunk.Session
import skunk.codec.all._
import skunk.implicits._
import natchez.Trace.Implicits.noop
import net.drthum.unisearch.daos.EntityDAO
import java.util.UUID
import net.drthum.unisearch.services.SearchService
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.server.Router
import org.http4s.ember.server.EmberServerBuilder
import com.comcast.ip4s._

object Main extends IOApp {

  val sessionResource: Resource[IO, Session[IO]] = Session.single(
    host = "localhost",
    port = 5432,
    user = "unisearch",
    database = "unisearch",
    password = Some("unisearch")
  )

  def routes(service: SearchService[IO]) = HttpRoutes.of[IO] {
    case GET -> Root / "search" => service.search("04cbdc0d-468a-4318-a3de-90c971fef646").flatMap(l => Ok(l.head.name))
  }

  def httpApp(service: SearchService[IO]) = Router("/" -> routes(service)).orNotFound

  override def run(args: List[String]): IO[ExitCode] = {
    (for {
      session <- sessionResource
      dao = new EntityDAO[IO](session)
      service = new SearchService[IO](dao)
      server <- EmberServerBuilder
        .default[IO]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(httpApp(service))
        .build
      // _ <- service.search("11111111-1111-1111-1111-111111111111")
    } yield {
      println(service.search("424b0a68-6407-421f-bd59-dd4d72161794").unsafeRunSync()(cats.effect.unsafe.IORuntime.global))
      ExitCode.Success
    }).useForever
  }

}
