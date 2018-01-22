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

    override def read(json: JsValue): PrintData = json match {
      case JsObject(fields) => PrintData(
        fields.get("id").map(_.toString.toLong),
        fields("first_name").convertTo[String],
        fields("last_name").convertTo[String],
        fields("date").convertTo[String]
      )
      case _ => throw DeserializationException("PrintData expected")
    }
  }
}
