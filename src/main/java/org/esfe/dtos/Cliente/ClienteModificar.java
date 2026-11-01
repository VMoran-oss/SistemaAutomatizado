package org.esfe.dtos.Cliente;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ClienteModificar implements Serializable {
    private Integer idCliente;
    private String nombre;
    private Integer telefono;
    private String correo;
}