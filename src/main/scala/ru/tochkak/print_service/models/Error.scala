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
  final case object BadParams extends Error(101, "Неверные параметры")
  final case object PrintFindError extends Error(200, "Принтер не найден")
  final case object PrintError extends Error(201, "Ошибка печати")
  final case object DirectoryError extends Error(300, "Ошибка директории")
  final case object RenderPdfError extends Error(301, "Ошибка рендеринга PDF")
  final case object FontError extends Error(302, "Ошибка шрифтов")
}
