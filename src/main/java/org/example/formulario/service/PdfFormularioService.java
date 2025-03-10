package org.example.formulario.service;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.lowagie.text.Image;

import java.awt.Color;

import org.example.formulario.entity.Formulario;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class PdfFormularioService implements IPdfFormularioService {

    // Colores para el diseño
    private static final Color COLOR_PRINCIPAL = new Color(0, 102, 204);
    private static final Color COLOR_SECUNDARIO = new Color(245, 245, 245);
    private static final Color COLOR_TEXTO = new Color(50, 50, 50);

    @Override
    public ByteArrayOutputStream generarPDF(Formulario formu) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            // Configuración del documento
            Document docu = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(docu, byteArrayOutputStream);
            docu.open();

            // Definición de estilos
            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, COLOR_PRINCIPAL);
            Font subtituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, COLOR_PRINCIPAL);
            Font nombreFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15, COLOR_TEXTO);
            Font parrafoFont = FontFactory.getFont(FontFactory.HELVETICA, 12, COLOR_TEXTO);
            Font fechaFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, COLOR_TEXTO);

            // Encabezado
            Paragraph titulo = new Paragraph("Formulario de Viáticos", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(10);
            docu.add(titulo);

            // Línea separadora
            LineSeparator linea = new LineSeparator();
            linea.setLineColor(COLOR_PRINCIPAL);
            linea.setLineWidth(2);
            docu.add(linea);
            docu.add(new Paragraph(" "));

            // Subtítulo
            Paragraph certificacion = new Paragraph("CERTIFICACIÓN", subtituloFont);
            certificacion.setAlignment(Element.ALIGN_CENTER);
            certificacion.setSpacingAfter(15);
            docu.add(certificacion);

            // Texto introductorio
            Paragraph intro = new Paragraph("Por medio del presente documento se certifica que:", parrafoFont);
            intro.setAlignment(Element.ALIGN_JUSTIFIED);
            intro.setSpacingAfter(10);
            docu.add(intro);

            // Nombre destacado
            PdfPTable tablaNombre = new PdfPTable(1);
            tablaNombre.setWidthPercentage(100);
            tablaNombre.setSpacingAfter(15);

            PdfPCell celdaNombre = new PdfPCell(new Phrase(formu.getNombre(), nombreFont));
            celdaNombre.setBackgroundColor(COLOR_SECUNDARIO);
            celdaNombre.setPadding(10);
            celdaNombre.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaNombre.setBorderColor(COLOR_PRINCIPAL);
            tablaNombre.addCell(celdaNombre);
            docu.add(tablaNombre);

            // Detalles del viático
            Paragraph detalles = new Paragraph();
            detalles.add(new Chunk("ha solicitado un viático de ", parrafoFont));
            detalles.add(new Chunk("$" + formu.getMonto(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, COLOR_PRINCIPAL)));
            detalles.add(new Chunk(" destinado a cubrir los costos del viaje a la localidad de ", parrafoFont));
            detalles.add(new Chunk(formu.getLocalidad(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, COLOR_PRINCIPAL)));
            detalles.add(new Chunk(" a realizarse el día ", parrafoFont));
            detalles.add(new Chunk(formu.getFecha(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, COLOR_PRINCIPAL)));
            detalles.setAlignment(Element.ALIGN_JUSTIFIED);
            detalles.setSpacingAfter(25);
            docu.add(detalles);

            // Tabla de resumen
            PdfPTable tablaResumen = new PdfPTable(2);
            tablaResumen.setWidthPercentage(80);
            tablaResumen.setSpacingBefore(10);
            tablaResumen.setSpacingAfter(20);
            tablaResumen.setHorizontalAlignment(Element.ALIGN_CENTER);

            // Estilos para la tabla
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);

            // Encabezados
            PdfPCell headerCell1 = new PdfPCell(new Phrase("Concepto", headerFont));
            headerCell1.setBackgroundColor(COLOR_PRINCIPAL);
            headerCell1.setPadding(7);
            headerCell1.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell headerCell2 = new PdfPCell(new Phrase("Detalle", headerFont));
            headerCell2.setBackgroundColor(COLOR_PRINCIPAL);
            headerCell2.setPadding(7);
            headerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);

            tablaResumen.addCell(headerCell1);
            tablaResumen.addCell(headerCell2);

            // Añadir filas a la tabla
            addRowToTable(tablaResumen, "Solicitante", formu.getNombre());
            addRowToTable(tablaResumen, "Monto", "$" + formu.getMonto());
            addRowToTable(tablaResumen, "Destino", formu.getLocalidad());
            addRowToTable(tablaResumen, "Fecha del viaje", formu.getFecha());

            docu.add(tablaResumen);

            // Línea separadora
            LineSeparator lineaFinal = new LineSeparator();
            lineaFinal.setLineColor(COLOR_PRINCIPAL);
            lineaFinal.setLineWidth(1);
            lineaFinal.setPercentage(70);
            docu.add(lineaFinal);

            // Fecha y pie de página
            String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            Paragraph fechaParrafo = new Paragraph("Documento generado el: " + fechaActual, fechaFont);
            fechaParrafo.setAlignment(Element.ALIGN_RIGHT);
            fechaParrafo.setSpacingBefore(20);
            docu.add(fechaParrafo);

            docu.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream;
    }

    // Método auxiliar para añadir filas a la tabla
    private void addRowToTable(PdfPTable table, String label, String value) {
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBackgroundColor(COLOR_SECUNDARIO);
        labelCell.setPadding(5);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setPadding(5);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}