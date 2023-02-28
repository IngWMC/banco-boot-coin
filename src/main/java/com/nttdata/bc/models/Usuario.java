package com.nttdata.bc.models;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    private ObjectId id;
    private String nombre;
    private String tipoDocumentoIdentidad;
    private String documentoIdentidad;
    private String celular;
    private String correo;
    private int cantidadBootCoin;
    private Boolean esActivo;
}
