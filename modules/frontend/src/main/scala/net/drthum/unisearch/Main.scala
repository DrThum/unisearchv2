package net.drthum.unisearch

import scala.scalajs.js.annotation.JSExportTopLevel

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import io.circe._
import org.scalajs.dom
import org.scalajs.dom.{KeyboardEvent, document}
import sttp.client3._
import sttp.client3.impl.cats.FetchCatsBackend
import sttp.tapir.client.sttp.SttpClientInterpreter
import sttp.tapir.json.circe._

import net.drthum.unisearch.models.Endpoints._

@JSExportTopLevel("universalsearch")
object Main {

  private val requestBuilder = SttpClientInterpreter().toRequest(searchEndpoint, Some(uri"http://localhost:8080"))
  private val backend = FetchCatsBackend[IO]()

  def main(): Unit = {
    val inputNode = document.createElement("input")
    inputNode.setAttribute("type", "text")
    inputNode.setAttribute("placeholder", "Search")
    inputNode.addEventListener("keypress", { (ev: KeyboardEvent) =>
      if (ev.key == "Enter") {
        println("pom1")
        val r = requestBuilder.apply("mediaplan").response(asStringAlways).send(backend).unsafeRunAsync({ resp => // FIXME: without unsafeRunAsync ?
          println(s"resp + $resp")
        })
        println(s"pom2 + $r")
      }
    })

    document.getElementById("app-container").appendChild(inputNode)
  }

}
