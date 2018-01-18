package ru.tochkak.print_service.models

import spray.json.{JsNumber, JsObject, JsString}

sealed abstract class Error(
  val code: Int,
  val message: String
) {
  def toJson = JsObject("code" -> JsNumber(code), "message" -> JsString(message))
}

object Error {
  final case object TimeoutError extends Error(100, "Таймаут")
  final case object PrintFindError extends Error(200, "Принтер не найден")
  final case object PrintError extends Error(201, "Ошибка печати")
}
