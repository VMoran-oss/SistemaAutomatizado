package org.esfe.seguridad.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRegistrar {
    private String nombre;
    private String apellido;
    private String telefono;
    private String login;
    private String clave;
    private Integer rolId;
}