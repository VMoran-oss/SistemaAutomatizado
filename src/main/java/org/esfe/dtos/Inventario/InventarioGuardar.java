package org.esfe.dtos.Inventario;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class InventarioGuardar implements Serializable {
    private Integer Cantidad;

    private Integer idProducto;
}
