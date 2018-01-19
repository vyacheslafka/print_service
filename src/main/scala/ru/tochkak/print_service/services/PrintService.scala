package ru.tochkak.print_service.services

import javax.print.PrintServiceLookup
import javax.print.attribute.HashPrintRequestAttributeSet
import javax.print.attribute.standard.MediaPrintableArea
import javax.swing.JEditorPane
import javax.swing.text.html.{HTMLEditorKit, ImageView}
import javax.swing.text.{Element, View, ViewFactory}

import org.slf4j.LoggerFactory
import ru.tochkak.print_service.models.Error.{PrintError, PrintFindError}
import ru.tochkak.print_service.models.{Error, PrintData}

import scala.util.Try

class PrintService {

  import PrintService._

  def print(printData: PrintData): Either[Error, Unit] = {
    val printServices = PrintServiceLookup.lookupPrintServices(null, null)
    logger.trace(s"Found ${printServices.length} printers")

    printServices.find(_.getName.contains(ConfigService.printerName)).map { printer =>
      val attributes = new HashPrintRequestAttributeSet
      val jEditorPane = render(printData)

      attributes.add(ConfigService.orientation.value)
      attributes.add(
        new MediaPrintableArea(
          0f,
          0f,
          ConfigService.width,
          ConfigService.height,
          ConfigService.measure.value)
      )

      logger.debug(s"Printer name: ${printer.getName}")

      Try(jEditorPane.print(null, null, false, printer, attributes, false)).fold(
        _ => Left(PrintError),
        _ => Right(())
      )
    } getOrElse {
      logger.debug("Printer not found")
      Left(PrintFindError)
    }
  }

  private def render(printData: PrintData) = {
    val template = ConfigService.template
      .replaceAll(FIRST_NAME_PLACE, printData.firstName)
      .replaceAll(LAST_NAME_PLACE, printData.lastName)
      .replaceAll(DATE_PLACE, printData.date)
    logger.trace(s"Template for print: \n$template")

    val jep = new JEditorPane
    jep.setEditorKit(new CustomHTMLEditor)
    jep.setContentType(CONTENT_TYPE)
    jep.setText(template)
    jep
  }
}

object PrintService {
  private final val logger = LoggerFactory.getLogger(this.getClass)

  private final val FIRST_NAME_PLACE = "%FIRST_NAME%"
  private final val LAST_NAME_PLACE = "%LAST_NAME%"
  private final val DATE_PLACE = "%DATE%"
  private final val CONTENT_TYPE = "text/html"

  class SyncViewFactor extends HTMLEditorKit.HTMLFactory {
    override def create(element: Element): View = {
      val view = super.create(element)
      view match {
        case imageView: ImageView =>
          imageView.setLoadsSynchronously(true)
          view
        case _ => view
      }
    }
  }

  class CustomHTMLEditor extends HTMLEditorKit {
    override def getViewFactory: ViewFactory = new SyncViewFactor
  }
}
