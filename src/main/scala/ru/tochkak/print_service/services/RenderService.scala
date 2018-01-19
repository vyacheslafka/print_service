package ru.tochkak.print_service.services

import java.io.ByteArrayInputStream
import javax.swing.JEditorPane
import javax.swing.text.{Element, View, ViewFactory}
import javax.swing.text.html.{HTMLEditorKit, ImageView}

import com.itextpdf.text.{Document, Rectangle}
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.tool.xml.XMLWorkerHelper
import org.slf4j.LoggerFactory
import ru.tochkak.print_service.models.Error.RenderPdfError
import ru.tochkak.print_service.models.{Error, PrintData}

import scala.util.Try

class RenderService {

  import RenderService._

  private val fileService = new FileService

  def renderAsPane(printData: PrintData): JEditorPane = {
    val template = replace(printData)
    val jep = new JEditorPane
    jep.setEditorKit(new CustomHTMLEditor)
    jep.setContentType(CONTENT_TYPE)
    jep.setText(template)
    jep
  }

  def renderAsPdf(printData: PrintData): Either[Error, Unit] = {
    val template = replace(printData)
    val inputStream = new ByteArrayInputStream(template.getBytes)
    val document = new Document()

    document.setPageSize(new Rectangle(ConfigService.width, ConfigService.height))
    document.setMargins(
      ConfigService.pdfMargins,
      ConfigService.pdfMargins,
      ConfigService.pdfMargins,
      ConfigService.pdfMargins
    )

    for {
      output <- fileService.getOutputFile
      writer <- Try(PdfWriter.getInstance(document, output))
        .fold(_ => Left(RenderPdfError), pdfWriter => Right(pdfWriter))
      _ = document.open()
      _ <- Try(XMLWorkerHelper.getInstance.parseXHtml(writer, document, inputStream))
        .fold(_ => Left(RenderPdfError), pdfWriter => Right(pdfWriter))
      _ = document.close()
      _ = output.close()
    } yield ()
  }

  private def replace(printData: PrintData) = {
    val template = ConfigService.template
      .replaceAll(FIRST_NAME_PLACE, printData.firstName)
      .replaceAll(LAST_NAME_PLACE, printData.lastName)
      .replaceAll(DATE_PLACE, printData.date)

    logger.trace(s"Template for print: \n$template")
    template
  }
}

object RenderService {
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
