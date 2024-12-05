package com.swpgavf.back.dto;

import lombok.Data;

@Data
public class MovimientoKardexDTO {
    private Long idProducto;
    private int cantidad; // Positivo para entrada, negativo para salida
}
