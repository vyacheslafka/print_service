name := "print_service"
organization := "ru.tochkak"
version := "0.0.1"
scalaVersion := "2.12.4"

val akkaHttpVersion = "10.0.11"
val catsVersion = "1.0.1"
val logbackVersion = "1.2.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "org.typelevel" %% "cats-core" % catsVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
)

mainClass in (Compile, run) := Some("ru.tochkak.print_service.WebServer")
scalacOptions ++= Seq("-deprecation", "-feature")

mappings in Universal += ((resourceDirectory in Compile).value / "application.conf") -> "conf/application.conf"
enablePlugins(JavaServerAppPackaging)
