package org.esfe.dtos.Usuario;

import lombok.Getter;
import lombok.Setter;
import org.esfe.dtos.Rol.RolSalida;

import java.io.Serializable;

@Getter
@Setter
public class UsuarioSalida implements Serializable {
    private Integer idUsuario;
    private String nombre;
    private String correo;
    private RolSalida rol;
}