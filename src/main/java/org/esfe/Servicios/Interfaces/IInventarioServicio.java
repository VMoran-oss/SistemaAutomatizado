package org.esfe.Servicios.Interfaces;

import org.esfe.dtos.Inventario.InventarioGuardar;
import org.esfe.dtos.Inventario.InventarioModificar;
import org.esfe.dtos.Inventario.InventarioSalida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IInventarioServicio {

    List<InventarioSalida> obtenerTodos();

    Page<InventarioSalida> obtenerTodosPaginados(Pageable pageable);

    InventarioSalida obtenerPorId(Integer IdInventario);

    InventarioSalida crear(InventarioGuardar inventarioGuardar);

    InventarioSalida modificar(InventarioModificar inventarioModificar);

    void eliminarPorId(Integer IdInventario);
}
