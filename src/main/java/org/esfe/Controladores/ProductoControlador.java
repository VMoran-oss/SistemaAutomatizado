package org.esfe.Controladores;

import org.esfe.Servicios.Interfaces.IProductoServicio;
import org.esfe.dtos.Producto.ProductoGuardar;
import org.esfe.dtos.Producto.ProductoModificar;
import org.esfe.dtos.Producto.ProductoSalida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Productos")
public class ProductoControlador {

    @Autowired
    private IProductoServicio productoServicio;

    @GetMapping
    public ResponseEntity<Page<ProductoSalida>> mostrarTodosPaginados(Pageable pageable) {
        Page<ProductoSalida> productos = productoServicio.obtenerTodosPaginados(pageable);
        if (productos.hasContent()) {
            return ResponseEntity.ok(productos);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/lista")
    public ResponseEntity<List<ProductoSalida>> mostrarTodos() {
        List<ProductoSalida> productos = productoServicio.obtenerTodos();
        if (!productos.isEmpty()) {
            return ResponseEntity.ok(productos);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{IdProducto}")
    public ResponseEntity<ProductoSalida> buscarPorId(@PathVariable Integer IdProducto) {
        ProductoSalida Producto = productoServicio.obtenerPorId(IdProducto);
        if (Producto != null) {
            return ResponseEntity.ok(Producto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ProductoSalida> crear(@RequestBody ProductoGuardar productoGuardar) {
        ProductoSalida Producto = productoServicio.crear(productoGuardar);
        return ResponseEntity.ok(Producto);
    }

    @PutMapping("/{IdProducto}")
    public ResponseEntity<ProductoSalida> modificar(@PathVariable Integer IdProducto, @RequestBody ProductoModificar productoModificar) {
        productoModificar.setIdProducto(IdProducto);
        ProductoSalida Producto = productoServicio.modificar(productoModificar);
        if (Producto != null) {
            return ResponseEntity.ok(Producto);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{IdProducto}")
    public ResponseEntity<String> eliminar(@PathVariable Integer IdProducto) {
        productoServicio.eliminarPorId(IdProducto);
        return ResponseEntity.ok("Producto Eliminado correctamente");
    }
}
