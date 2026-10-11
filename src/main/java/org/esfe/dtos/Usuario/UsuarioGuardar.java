package org.esfe.dtos.Usuario;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UsuarioGuardar implements Serializable {
    private String nombre;
    private String correo;
    private String contrasena;

    private Integer idRol;
}
