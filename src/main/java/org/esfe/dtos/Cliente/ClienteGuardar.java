package org.esfe.dtos.Cliente;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteGuardar implements Serializable {
    private String nombre;
    private Integer telefono;
    private String correo;
}
