package org.esfe.dtos.Producto;

import lombok.Getter;
import lombok.Setter;
import org.esfe.dtos.Inventario.InventarioSalida;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductoSalida implements Serializable {
    private Integer idProducto;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
}
