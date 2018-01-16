package ru.tochkak.print_service.models

trait Error

object Error {
  final case object PrintError extends Error
}
