package org.esfe.Servicios.Interfaces;

import org.esfe.dtos.Venta.VentaGuardar;
import org.esfe.dtos.Venta.VentaModificar;
import org.esfe.dtos.Venta.VentaSalida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IVentaServicio {

    List<VentaSalida> obtenerTodos();

    Page<VentaSalida> obtenerTodosPaginados(Pageable pageable);

    VentaSalida obtenerPorId(Integer idVenta);

    VentaSalida crear(VentaGuardar ventaGuardar);

    VentaSalida modificar(VentaModificar ventaModificar);

    void eliminarPorId(Integer idVenta);
}