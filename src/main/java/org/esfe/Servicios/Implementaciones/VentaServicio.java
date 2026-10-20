package org.esfe.Servicios.Implementaciones;

import org.esfe.Modelos.Cliente;
import org.esfe.Modelos.Venta;
import org.esfe.Repositorios.IClienteRepositorio;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VentaServicio implements IVentaServicio {

    @Autowired
    private IVentaRepositorio ventaRepositorio;

    @Autowired
    private IClienteRepositorio clienteRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<VentaSalida> obtenerTodos() {
        return ventaRepositorio.findAll().stream()
                .map(venta -> modelMapper.map(venta, VentaSalida.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VentaSalida> obtenerTodosPaginados(Pageable pageable) {
        return ventaRepositorio.findAll(pageable)
                .map(venta -> modelMapper.map(venta, VentaSalida.class));
    }

    @Override
    @Transactional(readOnly = true)
    public VentaSalida obtenerPorId(Integer idVenta) {
        return modelMapper.map(buscarVenta(idVenta), VentaSalida.class);
    }

    @Override
    @Transactional
    public VentaSalida crear(VentaGuardar ventaGuardar) {
        validar(ventaGuardar.getTotal(), ventaGuardar.getIdCliente());

        Venta venta = new Venta();
        venta.setFecha(ventaGuardar.getFecha()); // si es null, @PrePersist pone la fecha de hoy
        venta.setTotal(ventaGuardar.getTotal());
        venta.setCliente(buscarCliente(ventaGuardar.getIdCliente()));

        return modelMapper.map(ventaRepositorio.save(venta), VentaSalida.class);
    }

    @Override
    @Transactional
    public VentaSalida modificar(VentaModificar ventaModificar) {
        validar(ventaModificar.getTotal(), ventaModificar.getIdCliente());
        Venta venta = buscarVenta(ventaModificar.getIdVenta());

        if (ventaModificar.getFecha() != null) {
            venta.setFecha(ventaModificar.getFecha());
        }
        venta.setTotal(ventaModificar.getTotal());
        venta.setCliente(buscarCliente(ventaModificar.getIdCliente()));

        return modelMapper.map(ventaRepositorio.save(venta), VentaSalida.class);
    }

    @Override
    @Transactional
    public void eliminarPorId(Integer idVenta) {
        ventaRepositorio.delete(buscarVenta(idVenta));
    }

    private Venta buscarVenta(Integer idVenta) {
        return ventaRepositorio.findById(idVenta)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));
    }

    private Cliente buscarCliente(Integer idCliente) {
        return clienteRepositorio.findById(idCliente)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }

    private void validar(BigDecimal total, Integer idCliente) {
        if (total == null || total.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El total es obligatorio y no puede ser negativo");
        }
        if (idCliente == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cliente es obligatorio");
        }
    }
}