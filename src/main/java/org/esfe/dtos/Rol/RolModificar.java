package org.esfe.dtos.Rol;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RolModificar implements Serializable {
    private Integer idRol;
    private String nombre;
}