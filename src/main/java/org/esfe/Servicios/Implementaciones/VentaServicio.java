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
import org.springframework.stereotype.Service;

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
        return ventaRepositorio.findById(idVenta)
                .map(venta -> modelMapper.map(venta, VentaSalida.class))
                .orElse(null);
    }

    @Override
    public VentaSalida crear(VentaGuardar ventaGuardar) {
        Venta venta = ventaRepositorio.save(modelMapper.map(ventaGuardar, Venta.class));
        return modelMapper.map(venta, VentaSalida.class);
    }

    @Override
    public VentaSalida modificar(VentaModificar ventaModificar) {
        Venta existente = ventaRepositorio.findById(ventaModificar.getIdVenta()).orElse(null);
        if (existente == null) {
            return null;
        }
        // Copia solo los campos del DTO sobre la venta existente (la fecha se conserva)
        modelMapper.map(ventaModificar, existente);
        return modelMapper.map(ventaRepositorio.save(existente), VentaSalida.class);
    }

    @Override
    public void eliminarPorId(Integer idVenta) {
        ventaRepositorio.deleteById(idVenta);
    }
}