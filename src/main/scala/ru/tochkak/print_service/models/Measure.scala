package ru.tochkak.print_service.models

import javax.print.attribute.standard.MediaPrintableArea
import scala.language.implicitConversions

case class Measure(
  name: String,
  value: Int
)

object Measure {
  final case object Millimeter extends Measure("mm", MediaPrintableArea.MM)
  final case object Inch extends Measure("inch", MediaPrintableArea.INCH)

  implicit def string2Measure(name: String): Measure = name.toLowerCase match {
    case Millimeter.name => Millimeter
    case _ => Inch
  }
}
