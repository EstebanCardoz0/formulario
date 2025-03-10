package org.example.formulario.controller;

import org.example.formulario.entity.Formulario;
import org.example.formulario.service.IPdfFormularioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;

@Controller
public class FormularioController {

    @Autowired
    private IPdfFormularioService formuServ;

    @GetMapping("/")
    public String showFormulario(Model model) {
        // Añadir un objeto formulario vacío al modelo para evitar el error de binding
        model.addAttribute("formulario", new Formulario());
        return "index";
    }

    // Versión simple que descarga directamente el PDF
    @PostMapping("/generarPDF")
    @ResponseBody
    public ResponseEntity<byte[]> generarFormu(@RequestParam String nombre, @RequestParam double monto,
                                               @RequestParam String localidad, @RequestParam String fecha,
                                               Model model) {
        Formulario formu = new Formulario();
        formu.setNombre(nombre);
        formu.setMonto(monto);
        formu.setLocalidad(localidad);
        formu.setFecha(fecha);

        ByteArrayOutputStream pdfStream = formuServ.generarPDF(formu);

        // Añadir mensaje de éxito a la sesión
        model.addAttribute("mensajeExito", "PDF generado correctamente");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "formulario-viaticos-" + nombre + ".pdf");

        return ResponseEntity.ok().headers(headers).body(pdfStream.toByteArray());
    }
}