package org.esfe.dtos.Usuario;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UsuarioModificar implements Serializable {
    private Integer idUsuario;
    private String nombre;
    private String correo;
    private String contrasena;
    private Integer idRol;
}