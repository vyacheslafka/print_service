import com.typesafe.sbt.SbtNativePackager.autoImport.NativePackagerHelper._

name := "print_service"
organization := "ru.tochkak"
version := "0.1.1"
scalaVersion := "2.12.4"

val akkaHttpVersion = "10.0.11"
val catsVersion = "1.0.1"
val logbackVersion = "1.2.3"
val iTextVersion = "5.5.12"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "com.itextpdf" % "itextpdf" % iTextVersion,
  "com.itextpdf.tool" % "xmlworker" % iTextVersion
)

mainClass in(Compile, run) := Some("ru.tochkak.print_service.WebServer")
scalacOptions ++= Seq("-deprecation", "-feature")

resourceDirectory in Compile := baseDirectory.value / "conf"
mappings in Universal ++= directory(baseDirectory.value / "conf")

enablePlugins(JavaServerAppPackaging)
