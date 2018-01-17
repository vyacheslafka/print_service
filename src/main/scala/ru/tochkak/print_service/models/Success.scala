package ru.tochkak.print_service.models

import spray.json.{JsNumber, JsObject}

case object Success {
  def toJson = JsObject("code" -> JsNumber(0))
}


