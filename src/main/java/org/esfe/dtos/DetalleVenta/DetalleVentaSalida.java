package org.esfe.dtos.DetalleVenta;

import lombok.Getter;
import lombok.Setter;
import org.esfe.dtos.Venta.VentaSalida;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class DetalleVentaSalida implements Serializable {
    private Integer idDetalleVenta;
    private Integer cantidad;
    private BigDecimal subTotal;
    private VentaSalida venta;
}
