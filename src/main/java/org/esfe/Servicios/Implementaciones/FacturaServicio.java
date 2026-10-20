package org.esfe.Servicios.Implementaciones;

import org.esfe.Modelos.Factura;
import org.esfe.Modelos.Venta;
import org.esfe.Repositorios.IFacturaRepositorio;
import org.esfe.Repositorios.IVentaRepositorio;
import org.esfe.Servicios.Interfaces.IFacturaServicio;
import org.esfe.dtos.Factura.FacturaGuardar;
import org.esfe.dtos.Factura.FacturaModificar;
import org.esfe.dtos.Factura.FacturaSalida;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacturaServicio implements IFacturaServicio {

    @Autowired
    private IFacturaRepositorio facturaRepositorio;

    @Autowired
    private IVentaRepositorio ventaRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<FacturaSalida> obtenerTodos() {
        List<Factura> facturas = facturaRepositorio.findAll();

        return facturas.stream()
                .map(factura -> modelMapper.map(factura, FacturaSalida.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<FacturaSalida> obtenerTodosPaginados(Pageable pageable) {
        Page<Factura> page = facturaRepositorio.findAll(pageable);

        List<FacturaSalida> facturasDto = page.stream()
                .map(factura -> modelMapper.map(factura, FacturaSalida.class))
                .collect(Collectors.toList());

        return new PageImpl<>(facturasDto, page.getPageable(), page.getTotalElements());
    }

    @Override
    public FacturaSalida obtenerPorId(Integer idFactura) {
        return facturaRepositorio.findById(idFactura)
                .map(factura -> modelMapper.map(factura, FacturaSalida.class))
                .orElse(null);
    }

    @Override
    public FacturaSalida crear(FacturaGuardar facturaGuardar) {
        Venta venta = ventaRepositorio.findById(facturaGuardar.getIdVenta())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "La venta indicada no existe"));

        Factura factura = new Factura();
        factura.setNumeroFactura(facturaGuardar.getNumeroFactura());
        factura.setFecha(facturaGuardar.getFecha());
        factura.setSubTotal(facturaGuardar.getSubTotal());
        factura.setIVA(facturaGuardar.getIVA());
        factura.setTotal(facturaGuardar.getTotal());
        factura.setMetodoDePago(facturaGuardar.getMetodoDePago());
        factura.setEstado(facturaGuardar.getEstado());
        factura.setDescuento(facturaGuardar.getDescuento());
        factura.setVenta(venta);

        Factura facturaGuardada = facturaRepositorio.save(factura);

        return modelMapper.map(facturaGuardada, FacturaSalida.class);
    }

    @Override
    public FacturaSalida modificar(FacturaModificar facturaModificar) {
        Factura factura = facturaRepositorio.findById(facturaModificar.getIdFactura())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Factura no encontrada"));

        Venta venta = ventaRepositorio.findById(facturaModificar.getIdVenta())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "La venta indicada no existe"));

        factura.setNumeroFactura(facturaModificar.getNumeroFactura());
        factura.setFecha(facturaModificar.getFecha());
        factura.setSubTotal(facturaModificar.getSubTotal());
        factura.setIVA(facturaModificar.getIVA());
        factura.setTotal(facturaModificar.getTotal());
        factura.setMetodoDePago(facturaModificar.getMetodoDePago());
        factura.setEstado(facturaModificar.getEstado());
        factura.setDescuento(facturaModificar.getDescuento());
        factura.setVenta(venta);

        Factura facturaModificada = facturaRepositorio.save(factura);

        return modelMapper.map(facturaModificada, FacturaSalida.class);
    }

    @Override
    public void eliminarPorId(Integer idFactura) {
        facturaRepositorio.deleteById(idFactura);
    }
}