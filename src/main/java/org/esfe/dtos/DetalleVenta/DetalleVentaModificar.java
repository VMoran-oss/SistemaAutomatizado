package org.esfe.dtos.DetalleVenta;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class DetalleVentaModificar implements Serializable {
    private Integer idDetalleVenta;
    private Integer cantidad;
    private BigDecimal subTotal;
    private Integer idVenta;
}
