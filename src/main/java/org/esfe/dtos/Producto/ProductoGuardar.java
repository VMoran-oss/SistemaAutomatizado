package org.esfe.dtos.Producto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductoGuardar implements Serializable {
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;

}
