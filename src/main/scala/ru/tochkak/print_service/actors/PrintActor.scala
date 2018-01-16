package ru.tochkak.print_service.actors

import akka.actor.Actor
import cats.implicits._
import ru.tochkak.print_service.actors.PrintActor.Print
import ru.tochkak.print_service.models.{PrintData, Error}

class PrintActor extends Actor {
  override def receive: Receive = {
    case Print(printData) => ().asRight[Error]
  }
}

object PrintActor {
  final case class Print(printData: PrintData)
}
