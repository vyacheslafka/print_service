name := "print_service"
organization := "ru.tochkak"
version := "0.0.1_dev"
scalaVersion := "2.12.4"

val akkaHttpVersion = "10.0.11"
val catsVersion = "1.0.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "org.typelevel" %% "cats-core" % catsVersion
)
