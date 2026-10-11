package org.esfe.Servicios.Interfaces;

import org.esfe.dtos.Usuario.UsuarioGuardar;
import org.esfe.dtos.Usuario.UsuarioModificar;
import org.esfe.dtos.Usuario.UsuarioSalida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUsuarioServicio {

    List<UsuarioSalida> obtenerTodos();

    Page<UsuarioSalida> obtenerTodosPaginados(Pageable pageable);

    UsuarioSalida obtenerPorId(Integer idUsuario);

    UsuarioSalida crear(UsuarioGuardar usuarioGuardar);

    UsuarioSalida modificar(UsuarioModificar usuarioModificar);

    void eliminarPorId(Integer idUsuario);
}