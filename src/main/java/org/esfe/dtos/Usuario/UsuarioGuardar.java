package org.esfe.dtos.Usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioGuardar implements Serializable {
    private String nombre;
    private String correo;
    private String contrasena;

    private Integer idRol;
}
