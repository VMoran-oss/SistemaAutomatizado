package org.esfe.dtos.Cliente;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ClienteModificar implements Serializable {
    private Integer IdCliente;
    private String Nombre;
    private Integer Telefono;
    private String Correo;
}