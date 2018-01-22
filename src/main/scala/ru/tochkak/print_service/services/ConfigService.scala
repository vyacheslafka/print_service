package ru.tochkak.print_service.services

import com.typesafe.config.{Config, ConfigFactory}
import ru.tochkak.print_service.models.{Measure, Orientation}
import ru.tochkak.print_service.utils.ImplicitConversions._

object ConfigService {
  val config: Config = ConfigFactory.load()

  val interface: String = config.getString("http.interface")
  val port: Int = config.getInt("http.port")

  val measure: Measure = config.getString("render.measure")
  val width: Float = config.getDouble("render.width").toFloat
  val height: Float = config.getDouble("render.height").toFloat
  val template: String = config.getString("render.template")
  val font: String = config.getString("render.font")

  val printerName: String = config.getString("printer.name")
  val orientation: Orientation = config.getString("printer.orientation")

  val pdfDirectory: String = config.getString("pdf.directory")
  val pdfName: String = config.getString("pdf.name")
  val pdfMargins: Int = config.getInt("pdf.margins")
}
