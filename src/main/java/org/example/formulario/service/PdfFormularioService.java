package org.example.formulario.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.example.formulario.entity.Formulario;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PdfFormularioService implements IPdfFormularioService {
    @Override
    public ByteArrayOutputStream generarPDF(Formulario formu) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            Document docu = new Document();
            PdfWriter.getInstance(docu, byteArrayOutputStream);
            docu.open();

            //estilos
            Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font nombre = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);
            Font parrafo = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Font fecha = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10);

            //contenido del pdf
            docu.add(new Paragraph("Formulario de viáticos", titulo));
            docu.add(new Paragraph(" "));
            docu.add(new Paragraph("Por medio del presente documento se certifica que:", parrafo));
            docu.add(new Paragraph(" "));
            docu.add(new Paragraph(formu.getNombre(), nombre));
            docu.add(new Paragraph(" "));
            docu.add(new Paragraph("ha solicitado un viático de " + formu.getMonto() + " destinado a " +
                    "cubrir los costos del viaje a la localidad de " + formu.getLocalidad() + " a realizarse " +
                    "el día " + formu.getFecha(), parrafo));
            docu.add(new Paragraph(" "));
            docu.add(new Paragraph("Fecha y hora: " +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), fecha));

            docu.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream;
    }
}
