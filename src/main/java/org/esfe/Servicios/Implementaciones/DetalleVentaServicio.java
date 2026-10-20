package org.esfe.Servicios.Implementaciones;

import org.esfe.Modelos.DetalleVenta;
import org.esfe.Modelos.Venta;
import org.esfe.Repositorios.IDetalleVentaRepositorio;
import org.esfe.Repositorios.IVentaRepositorio;
import org.esfe.Servicios.Interfaces.IDetalleVentaServicio;
import org.esfe.dtos.DetalleVenta.DetalleVentaGuardar;
import org.esfe.dtos.DetalleVenta.DetalleVentaModificar;
import org.esfe.dtos.DetalleVenta.DetalleVentaSalida;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetalleVentaServicio implements IDetalleVentaServicio {

    @Autowired
    private IDetalleVentaRepositorio detalleVentaRepositorio;

    @Autowired
    private IVentaRepositorio ventaRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<DetalleVentaSalida> obtenerTodos() {
        return detalleVentaRepositorio.findAll().stream()
                .map(detalle -> modelMapper.map(detalle, DetalleVentaSalida.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<DetalleVentaSalida> obtenerTodosPaginados(Pageable pageable) {
        Page<DetalleVenta> page = detalleVentaRepositorio.findAll(pageable);
        List<DetalleVentaSalida> dtos = page.stream()
                .map(detalle -> modelMapper.map(detalle, DetalleVentaSalida.class))
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());
    }

    @Override
    public DetalleVentaSalida obtenerPorId(Integer idDetalleVenta) {
        DetalleVenta detalle = buscarDetalleVenta(idDetalleVenta);
        return modelMapper.map(detalle, DetalleVentaSalida.class);
    }

    @Override
    public DetalleVentaSalida crear(DetalleVentaGuardar detalleVentaGuardar) {
        validar(detalleVentaGuardar.getCantidad(), detalleVentaGuardar.getSubTotal(), detalleVentaGuardar.getIdVenta());

        Venta venta = ventaRepositorio.findById(detalleVentaGuardar.getIdVenta())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));

        DetalleVenta detalleVenta = new DetalleVenta();
        detalleVenta.setCantidad(detalleVentaGuardar.getCantidad());
        detalleVenta.setSubTotal(detalleVentaGuardar.getSubTotal());
        detalleVenta.setVenta(venta);

        DetalleVenta detalleGuardado = detalleVentaRepositorio.save(detalleVenta);
        return modelMapper.map(detalleGuardado, DetalleVentaSalida.class);
    }

    @Override
    public DetalleVentaSalida modificar(DetalleVentaModificar detalleVentaModificar) {
        validar(detalleVentaModificar.getCantidad(), detalleVentaModificar.getSubTotal(), detalleVentaModificar.getIdVenta());

        DetalleVenta detalleVenta = buscarDetalleVenta(detalleVentaModificar.getIdDetalleVenta());

        Venta venta = ventaRepositorio.findById(detalleVentaModificar.getIdVenta())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));

        detalleVenta.setCantidad(detalleVentaModificar.getCantidad());
        detalleVenta.setSubTotal(detalleVentaModificar.getSubTotal());
        detalleVenta.setVenta(venta);

        DetalleVenta detalleModificado = detalleVentaRepositorio.save(detalleVenta);
        return modelMapper.map(detalleModificado, DetalleVentaSalida.class);
    }

    @Override
    public void eliminarPorId(Integer idDetalleVenta) {
        DetalleVenta detalle = buscarDetalleVenta(idDetalleVenta);
        detalleVentaRepositorio.delete(detalle);
    }

    private DetalleVenta buscarDetalleVenta(Integer idDetalleVenta) {
        return detalleVentaRepositorio.findById(idDetalleVenta)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle de venta no encontrado"));
    }

    private void validar(Integer cantidad, BigDecimal subTotal, Integer idVenta) {
        if (cantidad == null || cantidad <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad es obligatoria y debe ser mayor a cero");
        }
        if (subTotal == null || subTotal.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El subtotal es obligatorio y no puede ser negativo");
        }
        if (idVenta == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID de la venta es obligatorio");
        }
    }
}
