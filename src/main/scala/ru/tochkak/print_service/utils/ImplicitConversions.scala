package ru.tochkak.print_service.utils

import java.util.Optional

import ru.tochkak.print_service.models.{Measure, Orientation}
import ru.tochkak.print_service.models.Measure.{Inch, Millimeter}
import ru.tochkak.print_service.models.Orientation.{Landscape, Portrait, ReverseLandscape, ReversePortrait}

import scala.language.implicitConversions

object ImplicitConversions {

  implicit def string2Orientation(name: String): Orientation = name match {
    case Portrait.name => Portrait
    case Landscape.name => Landscape
    case ReversePortrait.name => ReversePortrait
    case _ => ReverseLandscape
  }

  implicit def string2Measure(name: String): Measure = name.toLowerCase match {
    case Millimeter.name => Millimeter
    case _ => Inch
  }

  implicit def optional2Option[T](value: Optional[T]): Option[T] = if (value.isPresent) Some(value.get) else None
}
