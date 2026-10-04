package org.esfe.Servicios.Interfaces;

import org.esfe.dtos.Producto.ProductoGuardar;
import org.esfe.dtos.Producto.ProductoModificar;
import org.esfe.dtos.Producto.ProductoSalida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductoServicio {

    Page<ProductoSalida> obtenerTodosPaginados(Pageable pageable);

    List<ProductoSalida> obtenerTodos();

    ProductoSalida obtenerPorId(Integer idProducto);

    ProductoSalida crear(ProductoGuardar productoGuardar);

    ProductoSalida modificar(ProductoModificar productoModificar);

    void eliminarPorId(Integer idProducto);
}
