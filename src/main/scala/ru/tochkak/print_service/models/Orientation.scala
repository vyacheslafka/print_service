package ru.tochkak.print_service.models

import javax.print.attribute.standard.OrientationRequested

abstract class Orientation(
  val name: String,
  val value: OrientationRequested
)

object Orientation {
  final case object Landscape extends Orientation("landscape", OrientationRequested.LANDSCAPE)
  final case object Portrait extends Orientation("portrait", OrientationRequested.PORTRAIT)
  final case object ReverseLandscape extends Orientation("reverse_landscape", OrientationRequested.REVERSE_LANDSCAPE)
  final case object ReversePortrait extends Orientation("reverse_portrait", OrientationRequested.REVERSE_PORTRAIT)

  implicit def string2Orientation(name: String): Orientation = name match {
    case Portrait.name => Portrait
    case Landscape.name => Landscape
    case ReversePortrait.name => ReversePortrait
    case _ => ReverseLandscape
  }
}
