package ru.tochkak.print_service.actors

import akka.actor.Actor
import ru.tochkak.print_service.actors.PrintActor.Print
import ru.tochkak.print_service.models.PrintData
import ru.tochkak.print_service.services.PrintService

class PrintActor extends Actor {
  private final val printService = new PrintService

  override def receive: Receive = {
    case Print(printData) =>
      printService.print(printData)
  }
}

object PrintActor {
  final case class Print(printData: PrintData)
}
