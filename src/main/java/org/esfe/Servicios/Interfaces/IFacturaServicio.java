package org.esfe.Servicios.Interfaces;

import org.esfe.dtos.Factura.FacturaGuardar;
import org.esfe.dtos.Factura.FacturaModificar;
import org.esfe.dtos.Factura.FacturaSalida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IFacturaServicio {

    List<FacturaSalida> obtenerTodos();

    Page<FacturaSalida> obtenerTodosPaginados(Pageable pageable);

    FacturaSalida obtenerPorId(Integer IdFactura);

    FacturaSalida crear(FacturaGuardar facturaGuardar);

    FacturaSalida modificar(FacturaModificar facturaModificar);

    void eliminarPorId(Integer IdFactura);
}