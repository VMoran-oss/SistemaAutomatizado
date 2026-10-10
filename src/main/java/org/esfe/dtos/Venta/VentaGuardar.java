package org.esfe.dtos.Venta;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class VentaGuardar implements Serializable {
    private LocalDate fecha;
    private BigDecimal total;
    private String cliente;
    private String usuario;
}