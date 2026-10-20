package org.esfe.Servicios.Implementaciones;

import org.esfe.Modelos.Producto;
import org.esfe.Repositorios.IProductoRepositorio;
import org.esfe.Servicios.Interfaces.IProductoServicio;
import org.esfe.dtos.Producto.ProductoGuardar;
import org.esfe.dtos.Producto.ProductoModificar;
import org.esfe.dtos.Producto.ProductoSalida;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoServicio implements IProductoServicio {

    @Autowired
    private IProductoRepositorio productoRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ProductoSalida> obtenerTodos() {
        return productoRepositorio.findAll().stream()
                .map(producto -> modelMapper.map(producto, ProductoSalida.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductoSalida> obtenerTodosPaginados(Pageable pageable) {
        return productoRepositorio.findAll(pageable)
                .map(producto -> modelMapper.map(producto, ProductoSalida.class));
    }

    @Override
    public ProductoSalida obtenerPorId(Integer idProducto) {
        return productoRepositorio.findById(idProducto)
                .map(producto -> modelMapper.map(producto, ProductoSalida.class))
                .orElse(null);
    }

    @Override
    public ProductoSalida crear(ProductoGuardar productoGuardar) {
        Producto producto = productoRepositorio.save(modelMapper.map(productoGuardar, Producto.class));
        return modelMapper.map(producto, ProductoSalida.class);
    }

    @Override
    public ProductoSalida modificar(ProductoModificar productoModificar) {
        Producto existente = productoRepositorio.findById(productoModificar.getIdProducto()).orElse(null);
        if (existente == null) {
            return null;
        }
        modelMapper.map(productoModificar, existente);
        return modelMapper.map(productoRepositorio.save(existente), ProductoSalida.class);
    }

    @Override
    public void eliminarPorId(Integer idProducto) {
        productoRepositorio.deleteById(idProducto);
    }
}
