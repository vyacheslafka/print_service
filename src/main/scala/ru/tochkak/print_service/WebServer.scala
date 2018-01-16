package ru.tochkak.print_service

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import ru.tochkak.print_service.actors.PrintActor
import ru.tochkak.print_service.actors.PrintActor.Print
import ru.tochkak.print_service.models.{Error, PrintData}
import ru.tochkak.print_service.services.ConfigService

import scala.concurrent.duration._
import scala.io.StdIn

object WebServer {

  def main(args: Array[String]) = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val domain = ConfigService.domain
    val port = ConfigService.port

    val printActor = system.actorOf(Props[PrintActor], "print-actor")

    val route = path("print") {
      post {
        entity(as[PrintData]) { printData =>
          val sendToPrint = printActor.ask(Print(printData))(30.seconds).mapTo[Either[Error, Unit]]
          complete(StatusCodes.NoContent)
        }
      }
    }

    val bindingFuture = Http().bindAndHandle(route, domain, port)
    println(s"Server online at http://$domain:$port/\n")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
