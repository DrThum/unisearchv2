package net.drthum.unisearch

import scala.scalajs.js.annotation.JSExportTopLevel

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import io.circe._
import org.scalajs.dom
import org.scalajs.dom.{HTMLElement, HTMLInputElement, KeyboardEvent, document}
import sttp.client3._
import sttp.client3.circe._
import sttp.client3.impl.cats.FetchCatsBackend
import sttp.tapir.client.sttp.SttpClientInterpreter
import sttp.tapir.json.circe._

import net.drthum.unisearch.models.Endpoints._
import net.drthum.unisearch.models.Entity
import net.drthum.unisearch.models.Mediaplan.given
import net.drthum.unisearch.models.Mediaplan

@JSExportTopLevel("universalsearch")
object Main {

  private val requestBuilder = SttpClientInterpreter().toRequest(searchEndpoint, Some(uri"http://localhost:8080"))
  private val backend = FetchCatsBackend[IO]()

  def main(): Unit = {
    val searchResultsContainer = document.getElementById("search-results")

    val inputNode = document.createElement("input")
    inputNode.setAttribute("type", "text")
    inputNode.setAttribute("placeholder", "Search")
    inputNode.addEventListener("keypress", { (ev: KeyboardEvent) =>
      if (ev.key == "Enter") {
        searchResultsContainer.textContent = "Searching..."
        requestBuilder.apply(inputNode.asInstanceOf[HTMLInputElement].value).response(asJson[List[Entity]]).send(backend).unsafeRunAsync({ resp => // FIXME: without unsafeRunAsync ?
          resp.map { r =>
            val transformed = r.body.map { entities =>
              entities.map { e =>
                val mp = e.asInstanceOf[Mediaplan]
                s"${mp.name} (${mp.id})"
              }
            }.getOrElse(Nil)

            searchResultsContainer.textContent = transformed.mkString("\n")
          }
        })
      }
    })

    document.getElementById("app-container").appendChild(inputNode)
  }

}
