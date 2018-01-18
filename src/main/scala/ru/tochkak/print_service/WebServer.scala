package ru.tochkak.print_service

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import org.slf4j.LoggerFactory
import ru.tochkak.print_service.actors.PrintActor
import ru.tochkak.print_service.actors.PrintActor.Print
import ru.tochkak.print_service.models.{Error, PrintData, Success => SuccessRes}
import ru.tochkak.print_service.services.ConfigService

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._
import scala.io.StdIn
import scala.util.{Failure, Success}

object WebServer {

  val logger = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]) = {
    implicit val system: ActorSystem = ActorSystem()
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val domain = ConfigService.interface
    val port = ConfigService.port

    val printActor = system.actorOf(Props[PrintActor], "print-actor")

    val route = path("api" / "print") {
      post {
        entity(as[PrintData]) { printData =>
          onComplete(printActor.ask(Print(printData))(1.minutes).mapTo[Either[Error, Unit]]) {
            case Success(result) =>
              result.fold(
                error => complete(error.toJson),
                _ => complete(SuccessRes.toJson)
              )
            case Failure(_) =>
              logger.warn("Timeout")
              complete(Error.TimeoutError.toJson)
          }
        }
      }
    }

    val bindingFuture = Http().bindAndHandle(route, domain, port)
    logger.info(s"The server is running on http://$domain:$port")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
