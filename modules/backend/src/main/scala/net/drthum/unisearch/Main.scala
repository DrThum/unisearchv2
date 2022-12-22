package net.drthum.unisearch

import java.nio.charset.StandardCharsets
import java.util.UUID

import cats.effect._
import com.comcast.ip4s._
import fs2.{Pipe, Stream}
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.{Encoder, Json}
import natchez.Trace.Implicits.noop
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import skunk.Session
import skunk.codec.all._
import skunk.implicits._
import sttp.capabilities.fs2.Fs2Streams
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.server.http4s.Http4sServerInterpreter.apply

import net.drthum.unisearch.daos.EntityDAO
import net.drthum.unisearch.models.Entity
import net.drthum.unisearch.models.Mediaplan.given
import net.drthum.unisearch.services.SearchService

object Main extends IOApp {

  val sessionResource: Resource[IO, Session[IO]] = Session.single(
    host = "localhost",
    port = 5432,
    user = "unisearch",
    database = "unisearch",
    password = Some("unisearch")
  )

  object SearchQueryParamMatcher extends QueryParamDecoderMatcher[String]("q")

  val searchEndpoint = endpoint
    .in("api" / "search")
    .in(query[String]("q"))
    .out(jsonBody[List[Entity]])

  def httpApp(service: SearchService[IO]) = Router("/" -> routes(service)).orNotFound

  def routes(service: SearchService[IO]): HttpRoutes[IO] = {
    Http4sServerInterpreter[IO]().toRoutes(searchEndpoint.serverLogicSuccess[IO](q => service.search(q).compile.toList))
  }

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
    } yield ExitCode.Success).useForever
  }

}
