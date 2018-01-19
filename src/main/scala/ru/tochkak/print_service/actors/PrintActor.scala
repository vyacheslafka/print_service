package ru.tochkak.print_service.actors

import akka.actor.Actor
import ru.tochkak.print_service.actors.PrintActor.{Print, PrintPdf}
import ru.tochkak.print_service.models.PrintData
import ru.tochkak.print_service.services.PrintService

class PrintActor extends Actor {
  private final val printService = new PrintService

  override def receive: Receive = {
    case Print(printData) =>
      sender ! printService.print(printData)
    case PrintPdf(printData) =>
      sender ! printService.printPdf(printData)
  }
}

object PrintActor {
  final case class Print(printData: PrintData)
  final case class PrintPdf(printData: PrintData)
}
