package org.example.formulario.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Formulario {

    private String nombre;
    private double monto;
    private String localidad;
    private String fecha;
}
