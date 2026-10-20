package org.esfe.Servicios.Interfaces;

import org.esfe.dtos.Rol.RolGuardar;
import org.esfe.dtos.Rol.RolModificar;
import org.esfe.dtos.Rol.RolSalida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRolServicio {

    List<RolSalida> obtenerTodos();

    Page<RolSalida> obtenerTodosPaginados(Pageable pageable);

    RolSalida obtenerPorId(Integer idRol);

    RolSalida crear(RolGuardar rolGuardar);

    RolSalida modificar(RolModificar rolModificar);

    void eliminarPorId(Integer idRol);
}