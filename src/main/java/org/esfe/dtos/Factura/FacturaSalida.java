package org.esfe.dtos.Factura;

import lombok.Getter;
import lombok.Setter;
import org.esfe.dtos.Venta.VentaSalida;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class FacturaSalida implements Serializable {

    private Integer IdFactura;

    private String NumeroFactura;

    private LocalDate Fecha;

    private BigDecimal SubTotal;

    private BigDecimal IVA;

    private BigDecimal Total;

    private String MetodoDePago;

    private String Estado;

    private Integer Descuento;

    private VentaSalida Venta;

}