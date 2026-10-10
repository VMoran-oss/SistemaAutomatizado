package org.esfe.Servicios.Implementaciones;

import org.esfe.Modelos.Venta;
import org.esfe.Repositorios.IVentaRepositorio;
import org.esfe.Servicios.Interfaces.IVentaServicio;
import org.esfe.dtos.Venta.VentaGuardar;
import org.esfe.dtos.Venta.VentaModificar;
import org.esfe.dtos.Venta.VentaSalida;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VentaServicio implements IVentaServicio {

    @Autowired
    private IVentaRepositorio ventaRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<VentaSalida> obtenerTodos() {
        return ventaRepositorio.findAll().stream()
                .map(venta -> modelMapper.map(venta, VentaSalida.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<VentaSalida> obtenerTodosPaginados(Pageable pageable) {
        return ventaRepositorio.findAll(pageable)
                .map(venta -> modelMapper.map(venta, VentaSalida.class));
    }

    @Override
    public VentaSalida obtenerPorId(Integer idVenta) {
        return modelMapper.map(buscarVenta(idVenta), VentaSalida.class);
    }

    @Override
    public VentaSalida crear(VentaGuardar ventaGuardar) {
        validar(ventaGuardar.getTotal(), ventaGuardar.getCliente(), ventaGuardar.getUsuario());

        Venta venta = new Venta();
        venta.setFecha(ventaGuardar.getFecha()); // si es null, @PrePersist pone la fecha de hoy
        venta.setTotal(ventaGuardar.getTotal());
        venta.setCliente(ventaGuardar.getCliente());
        venta.setUsuario(ventaGuardar.getUsuario());

        return modelMapper.map(ventaRepositorio.save(venta), VentaSalida.class);
    }

    @Override
    public VentaSalida modificar(VentaModificar ventaModificar) {
        validar(ventaModificar.getTotal(), ventaModificar.getCliente(), ventaModificar.getUsuario());
        Venta venta = buscarVenta(ventaModificar.getIdVenta());

        if (ventaModificar.getFecha() != null) {
            venta.setFecha(ventaModificar.getFecha());
        }
        venta.setTotal(ventaModificar.getTotal());
        venta.setCliente(ventaModificar.getCliente());
        venta.setUsuario(ventaModificar.getUsuario());

        return modelMapper.map(ventaRepositorio.save(venta), VentaSalida.class);
    }

    @Override
    public void eliminarPorId(Integer idVenta) {
        ventaRepositorio.delete(buscarVenta(idVenta));
    }

    private Venta buscarVenta(Integer idVenta) {
        return ventaRepositorio.findById(idVenta)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));
    }

    private void validar(BigDecimal total, String cliente, String usuario) {
        if (total == null || total.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El total es obligatorio y no puede ser negativo");
        }
        if (cliente == null || cliente.isBlank() || usuario == null || usuario.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente y usuario son obligatorios");
        }
    }
}