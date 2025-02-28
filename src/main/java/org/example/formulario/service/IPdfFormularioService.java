package org.example.formulario.service;

import org.example.formulario.entity.Formulario;

import java.io.ByteArrayOutputStream;

public interface IPdfFormularioService {

    public ByteArrayOutputStream generarPDF(Formulario formu);
}
