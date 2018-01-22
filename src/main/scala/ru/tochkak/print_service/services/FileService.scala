package ru.tochkak.print_service.services

import java.io.{File, FileOutputStream}
import java.nio.file.{FileSystems, Files, Path, Paths}
import java.util.function.Consumer

import ru.tochkak.print_service.models.Error
import ru.tochkak.print_service.models.Error.DirectoryError
import ru.tochkak.print_service.utils.ImplicitConversions.optional2Option

import scala.util.Try

class FileService {

  import FileService._

  def getOutputFile: Either[Error, FileOutputStream] = {
    val directory = FileSystems.getDefault.getPath(ConfigService.pdfDirectory)
    val path = directory.toString + File.separator + ConfigService.pdfName

    (for {
      _ <- Try(Files.createDirectories(directory))
      _ <- Try(Files.list(directory)).map(_.forEach(new CustomConsumer))
      _ <- Try(Files.createFile(Paths.get(path)))
      output <- Try(new FileOutputStream(path))
    } yield output).fold(_ => Left(DirectoryError), output => Right(output))
  }

  def getPathToPdf: Option[String] = {
    val directory = FileSystems.getDefault.getPath(ConfigService.pdfDirectory)
    val pathO: Option[Path] = if (Files.isDirectory(directory)) Files.list(directory).findFirst else None
    pathO.map(_.toString)
  }

  def getPathToFont: Option[String] = {
    val font = FileSystems.getDefault.getPath(ConfigService.font)
    if (Files.exists(font)) Some(font.toString) else None
  }
}

object FileService {

  class CustomConsumer extends Consumer[Path] {
    override def accept(path: Path): Unit = path.toFile.delete
  }
}
