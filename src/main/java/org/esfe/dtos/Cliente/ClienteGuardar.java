package org.esfe.dtos.Cliente;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ClienteGuardar implements Serializable {
    private String Nombre;
    private Integer Telefono;
    private String Correo;
}