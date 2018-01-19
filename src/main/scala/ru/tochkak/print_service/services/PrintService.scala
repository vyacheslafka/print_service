package ru.tochkak.print_service.services

import javax.print.attribute.HashPrintRequestAttributeSet
import javax.print.attribute.standard.MediaPrintableArea
import javax.print.{PrintServiceLookup, PrintService => PrintServiceJ}

import org.slf4j.LoggerFactory
import ru.tochkak.print_service.models.Error.{DirectoryError, PrintError, PrintFindError}
import ru.tochkak.print_service.models.{Error, PrintData}

import scala.sys.process.Process
import scala.util.Try

class PrintService {

  import PrintService._

  private val fileService = new FileService
  private val renderService = new RenderService

  def printPdf(printData: PrintData): Either[Error, Int] = {
    for {
      printer <- findPrinter(ConfigService.printerName)
      _ <- renderService.renderAsPdf(printData)
      path <- fileService.getPathToPdf.toRight(DirectoryError)
    } yield Process(s"lp -o fit-to-page -t ${printer.getName} -n 1 $path").run.exitValue
  }

  def print(printData: PrintData): Either[Error, Unit] = {
    val attributes = new HashPrintRequestAttributeSet
    val jEditorPane = renderService.renderAsPane(printData)

    attributes.add(ConfigService.orientation.value)
    attributes.add(
      new MediaPrintableArea(
        0f,
        0f,
        ConfigService.width,
        ConfigService.height,
        ConfigService.measure.value)
    )

    for {
      printer <- findPrinter(ConfigService.printerName)
      _ <- Try(jEditorPane.print(null, null, false, printer, attributes, false)).fold(
        _ => Left(PrintError),
        _ => Right(())
      )
    } yield ()
  }

  private def findPrinter(printerName: String): Either[Error, PrintServiceJ] = {
    val printServices = PrintServiceLookup.lookupPrintServices(null, null)
    logger.trace(s"Found ${printServices.length} printers")

    printServices.find(_.getName.contains(ConfigService.printerName)).map { printer =>
      logger.debug(s"Printer name: ${printer.getName}")
      Right(printer)
    } getOrElse {
      logger.info("Printer not found")
      Left(PrintFindError)
    }
  }
}

object PrintService {
  private final val logger = LoggerFactory.getLogger(this.getClass)
}
