package ru.tochkak.print_service.services

import com.typesafe.config.{Config, ConfigFactory}
import ru.tochkak.print_service.models.Orientation

object ConfigService {
  val config: Config = ConfigFactory.load("application.conf")

  val interface: String = config.getString("http.interface")
  val port: Int = config.getInt("http.port")

  val printerName: String = config.getString("printer.name")
  val orientation: Orientation = config.getString("printer.orientation")
  val template: String = config.getString("printer.template")
}
