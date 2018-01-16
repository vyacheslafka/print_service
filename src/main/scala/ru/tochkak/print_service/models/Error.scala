package ru.tochkak.print_service.models

import spray.json.{JsNumber, JsObject, JsString}

sealed abstract class Error(
  val id: Int,
  val message: String
) {
  def toJson = JsObject("id" -> JsNumber(id), "message" -> JsString(message))
}

object Error {
  final case object PrintError extends Error(100, "Ошибка печати")
}
