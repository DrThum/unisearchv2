package net.drthum.unisearch.models

import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.{Encoder, Json}
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._

import net.drthum.unisearch.models.Mediaplan.given

object Endpoints {

  val searchEndpoint = endpoint
    .in("api" / "search")
    .in(query[String]("q"))
    .out(jsonBody[List[Entity]])

}
