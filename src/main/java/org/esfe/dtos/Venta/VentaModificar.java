package org.esfe.dtos.Venta;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

    @Getter
    @Setter
    public class VentaModificar implements Serializable {
        private Integer idVenta;
        private BigDecimal total;
        private String cliente;
        private String usuario;
    }
