package org.esfe.Servicios.Interfaces;

import org.esfe.dtos.Cliente.ClienteGuardar;
import org.esfe.dtos.Cliente.ClienteModificar;
import org.esfe.dtos.Cliente.ClienteSalida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClienteServicio {

    List<ClienteSalida> obtenerTodos();

    Page<ClienteSalida> obtenerTodosPaginados(Pageable pageable);

    ClienteSalida obtenerPorId(Integer IdCliente);

    ClienteSalida crear(ClienteGuardar clienteGuardar);

    ClienteSalida modificar(ClienteModificar clienteModificar);

    void eliminarPorId(Integer IdCliente);
}