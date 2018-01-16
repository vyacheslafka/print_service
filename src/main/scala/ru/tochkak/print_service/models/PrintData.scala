package ru.tochkak.print_service.models

import spray.json._

case class PrintData(
  id: Option[Long],
  firstName: String,
  lastName: String,
  date: String
)

object PrintData extends DefaultJsonProtocol {
  implicit object PrintDataJson extends RootJsonFormat[PrintData] {
    override def write(obj: PrintData): JsValue = JsObject(
      "id" -> obj.id.map(JsNumber(_)).getOrElse(JsNull),
      "first_name" -> JsString(obj.firstName),
      "last_name" -> JsString(obj.lastName),
      "date" -> JsString(obj.date)
    )

    override def read(json: JsValue): PrintData = {
      json.asJsObject.getFields("id", "first_name", "last_name", "date") match {
        case Seq(JsNumber(id), JsString(firstName), JsString(lastName), JsString(date)) =>
          PrintData(Some(id.toLong), firstName, lastName, date)
        case _ => throw DeserializationException("PrintData expected")
      }
    }
  }
}
