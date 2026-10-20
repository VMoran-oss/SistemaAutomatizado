package org.esfe.Servicios.Implementaciones;

import org.esfe.Modelos.Inventario;
import org.esfe.Modelos.Producto;
import org.esfe.Repositorios.IInventarioRepositorio;
import org.esfe.Repositorios.IProductoRepositorio;
import org.esfe.Servicios.Interfaces.IInventarioServicio;
import org.esfe.dtos.Inventario.InventarioGuardar;
import org.esfe.dtos.Inventario.InventarioModificar;
import org.esfe.dtos.Inventario.InventarioSalida;
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
public class InventarioServicio implements IInventarioServicio {

    @Autowired
    private IInventarioRepositorio inventarioRepositorio;

    @Autowired
    private IProductoRepositorio productoRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<InventarioSalida> obtenerTodos() {
        List<Inventario> inventarios = inventarioRepositorio.findAll();

        return inventarios.stream()
                .map(inventario -> modelMapper.map(inventario, InventarioSalida.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<InventarioSalida> obtenerTodosPaginados(Pageable pageable) {
        Page<Inventario> page = inventarioRepositorio.findAll(pageable);

        List<InventarioSalida> inventariosDto = page.stream()
                .map(inventario -> modelMapper.map(inventario, InventarioSalida.class))
                .collect(Collectors.toList());

        return new PageImpl<>(inventariosDto, page.getPageable(), page.getTotalElements());
    }

    @Override
    public InventarioSalida obtenerPorId(Integer idInventario) {
        Inventario inventario = inventarioRepositorio.findById(idInventario)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Inventario no encontrado"));
        return modelMapper.map(inventario, InventarioSalida.class);
    }

    @Override
    public InventarioSalida crear(InventarioGuardar inventarioGuardar) {
        Producto producto = productoRepositorio.findById(inventarioGuardar.getIdProducto())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "El producto indicado no existe"));

        Inventario inventario = new Inventario();
        inventario.setCantidad(inventarioGuardar.getCantidad());
        inventario.setProducto(producto);

        Inventario guardado = inventarioRepositorio.save(inventario);

        return modelMapper.map(guardado, InventarioSalida.class);
    }

    @Override
    public InventarioSalida modificar(InventarioModificar inventarioModificar) {
        Inventario inventario = inventarioRepositorio.findById(inventarioModificar.getIdInventario())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Inventario no encontrado"));

        Producto producto = productoRepositorio.findById(inventarioModificar.getIdProducto())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "El producto indicado no existe"));

        inventario.setCantidad(inventarioModificar.getCantidad());
        inventario.setProducto(producto);

        Inventario modificado = inventarioRepositorio.save(inventario);

        return modelMapper.map(modificado, InventarioSalida.class);
    }

    @Override
    public void eliminarPorId(Integer idInventario) {
        inventarioRepositorio.deleteById(idInventario);
    }
}