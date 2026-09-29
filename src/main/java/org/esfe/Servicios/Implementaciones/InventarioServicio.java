package org.esfe.Servicios.Implementaciones;

import org.esfe.Repositorios.IInventarioRepositorio;
import org.esfe.Servicios.Interfaces.IInventarioServicio;
import org.esfe.dtos.Inventario.InventarioGuardar;
import org.esfe.dtos.Inventario.InventarioModificar;
import org.esfe.dtos.Inventario.InventarioSalida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioServicio implements IInventarioServicio {

    @Autowired
    private IInventarioRepositorio inventarioRepositorio;

    @Override
    public List<InventarioSalida> obtenerTodos() {
        return List.of();
    }

    @Override
    public Page<InventarioSalida> obtenerTodosPaginados(Pageable pageable) {
        return null;
    }

    @Override
    public InventarioSalida obtenerPorId(Integer IdInventario) {
        return null;
    }

    @Override
    public InventarioSalida crear(InventarioGuardar inventarioGuardar) {
        return null;
    }

    @Override
    public InventarioSalida modificar(InventarioModificar inventarioModificar) {
        return null;
    }
}
