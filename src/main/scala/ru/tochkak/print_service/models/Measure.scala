package ru.tochkak.print_service.models

import javax.print.attribute.standard.MediaPrintableArea

abstract class Measure(
  val name: String,
  val value: Int
)

object Measure {
  final case object Millimeter extends Measure("mm", MediaPrintableArea.MM)
  final case object Inch extends Measure("inch", MediaPrintableArea.INCH)
}
