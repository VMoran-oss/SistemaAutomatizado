package org.esfe.Servicios.Interfaces;

import org.esfe.dtos.DetalleVenta.DetalleVentaGuardar;
import org.esfe.dtos.DetalleVenta.DetalleVentaModificar;
import org.esfe.dtos.DetalleVenta.DetalleVentaSalida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDetalleVentaServicio {
    List<DetalleVentaSalida> obtenerTodos();

    Page<DetalleVentaSalida> obtenerTodosPaginados(Pageable pageable);

    DetalleVentaSalida obtenerPorId(Integer idDetalleVenta);

    DetalleVentaSalida crear(DetalleVentaGuardar detalleVentaGuardar);

    DetalleVentaSalida modificar(DetalleVentaModificar detalleVentaModificar);

    void eliminarPorId(Integer idDetalleVenta);
}
