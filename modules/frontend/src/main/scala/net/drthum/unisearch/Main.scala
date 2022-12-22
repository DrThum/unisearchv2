package net.drthum.unisearch

import scala.scalajs.js.annotation.JSExportTopLevel

import org.scalajs.dom
import org.scalajs.dom.document

@JSExportTopLevel("universalsearch")
object Main {

  def main(): Unit = {
    appendPar(document.body, "Hello world")
  }

  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    parNode.textContent = text
    targetNode.appendChild(parNode)
  }

}
