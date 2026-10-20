package org.esfe.seguridad.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioLogin {
    private String login;
    private String clave;
}