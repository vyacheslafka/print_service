package ru.tochkak.print_service.services

import com.typesafe.config.{Config, ConfigFactory}
import ru.tochkak.print_service.models.{Measure, Orientation}

object ConfigService {
  val config: Config = ConfigFactory.load()

  val interface: String = config.getString("http.interface")
  val port: Int = config.getInt("http.port")

  val printerName: String = config.getString("printer.name")
  val orientation: Orientation = config.getString("printer.orientation")
  val measure: Measure = config.getString("printer.measure")
  val width: Float = config.getDouble("printer.width").toFloat
  val height: Float = config.getDouble("printer.height").toFloat
  val value: Float = config.getDouble("printer.value").toFloat
  val template: String = config.getString("printer.template")
}
