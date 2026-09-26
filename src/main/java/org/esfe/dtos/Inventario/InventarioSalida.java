package org.esfe.dtos.Inventario;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class InventarioSalida implements Serializable {
    private Integer IdInventario;
    private Integer Cantidad;
}
