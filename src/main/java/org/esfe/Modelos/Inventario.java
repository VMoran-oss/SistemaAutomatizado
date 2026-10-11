package org.esfe.Modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name= "Inventarios")

public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer IdInventario;
    private Integer Cantidad;

    @ManyToOne
    @JoinColumn(name = "idProducto")
    private Producto Producto;
}
