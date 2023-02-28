package com.nttdata.bc.documents;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@MongoEntity(collection = "solicitudes")
public class Solicitud {
    private ObjectId id;

    private ObjectId idUsuarioSolicitante;

    private ObjectId idUsuarioAceptante;

    private String numeroTrasaccion;

    @Min(value = 1, message = "El campo cantidadBootCoin debe tener un valor mínimo de '1'.")
    @Digits(integer = 10, fraction = 0, message = "El campo cantidadBootCoin debe es número positivo.")
    @NotNull(message = "El campo cantidadBootCoin es requerido.")
    private int cantidadBootCoin;

    private BigDecimal monto;

    @Pattern(regexp = "^BD$|^TR$", message = "El campo modoPago debe tener uno de estos valores: [BD, TR].")
    @NotEmpty(message = "El campo modoPago es requerido.")
    private String modoPago;

    private Boolean esAceptado;

    private Boolean esActivo;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaModificacion;
}
