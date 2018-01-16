package ru.tochkak.print_service.services

import com.typesafe.config.{Config, ConfigFactory}
import ru.tochkak.print_service.models.Orientation

object ConfigService {
  val config: Config = ConfigFactory.load("application.conf")

  val domain: String = config.getString("http.domain")
  val port: Int = config.getInt("http.port")

  val template: String = config.getString("template.pattern")
  val orientation: Orientation = config.getString("template.orientation")
}
