package org.esfe.dtos.DetalleVenta;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class DetalleVentaGuardar implements Serializable {
    private Integer cantidad;
    private BigDecimal subTotal;
    private Integer idVenta; // Referencia para asociarlo a la venta
}
