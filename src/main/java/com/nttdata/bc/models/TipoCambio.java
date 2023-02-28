package com.nttdata.bc.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoCambio {
    private ObjectId id;
    private BigDecimal compra;
    private BigDecimal venta;
    private LocalDate fechaTipoCambio;
    private Boolean esActivo;
}
