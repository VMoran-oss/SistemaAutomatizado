package org.esfe.Servicios.Implementaciones;

import org.esfe.Servicios.Interfaces.IInventarioServicio;
import org.esfe.Servicios.Interfaces.IProductoServicio;
import org.esfe.dtos.Inventario.InventarioGuardar;
import org.esfe.dtos.Inventario.InventarioModificar;
import org.esfe.dtos.Inventario.InventarioSalida;
import org.esfe.dtos.Producto.ProductoGuardar;
import org.esfe.dtos.Producto.ProductoSalida;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class InventarioServicioTest {

    @Autowired
    private IInventarioServicio inventarioServicio;

    @Autowired
    private IProductoServicio productoServicio;

    // Datos compartidos entre las pruebas (se crean en t1 y se borran en t9)
    private static Integer idProductoPrueba;
    private static Integer idInventarioCreado;

    private InventarioGuardar nuevoInventario(Integer cantidad) {
        InventarioGuardar guardar = new InventarioGuardar();
        guardar.setCantidad(cantidad);
        guardar.setIdProducto(idProductoPrueba);
        return guardar;
    }

    @Test
    void t1_crear() {
        ProductoGuardar productoGuardar = new ProductoGuardar();
        productoGuardar.setNombre("Producto prueba " + System.currentTimeMillis());
        productoGuardar.setDescripcion("Producto para pruebas de inventario");
        productoGuardar.setPrecio(new BigDecimal("10.00"));
        productoGuardar.setStock(100);
        ProductoSalida productoSalida = productoServicio.crear(productoGuardar);
        idProductoPrueba = productoSalida.getIdProducto();

        InventarioSalida salida = inventarioServicio.crear(nuevoInventario(50));

        assertNotNull(salida);
        assertNotNull(salida.getIdInventario());
        assertEquals(50, salida.getCantidad());
        idInventarioCreado = salida.getIdInventario();
    }

    @Test
    void t2_obtenerTodos() {
        assertTrue(inventarioServicio.obtenerTodos().size() >= 1);
    }

    @Test
    void t3_obtenerPorId() {
        InventarioSalida salida = inventarioServicio.obtenerPorId(idInventarioCreado);
        assertNotNull(salida);
        assertEquals(50, salida.getCantidad());
        assertEquals(idProductoPrueba, salida.getProducto().getIdProducto());
    }

    @Test
    void t4_editar() {
        InventarioModificar modificar = new InventarioModificar();
        modificar.setIdInventario(idInventarioCreado);
        modificar.setCantidad(80);
        modificar.setIdProducto(idProductoPrueba);

        InventarioSalida salida = inventarioServicio.modificar(modificar);

        assertNotNull(salida);
        assertEquals(80, salida.getCantidad());
    }

    @Test
    void t5_crearConProductoInexistente_lanzaExcepcion() {
        assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                InventarioGuardar guardar = nuevoInventario(10);
                guardar.setIdProducto(-1);
                inventarioServicio.crear(guardar);
            }
        });
    }

    @Test
    void t6_modificarConProductoInexistente_lanzaExcepcion() {
        assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                InventarioModificar modificar = new InventarioModificar();
                modificar.setIdInventario(idInventarioCreado);
                modificar.setCantidad(10);
                modificar.setIdProducto(-1);
                inventarioServicio.modificar(modificar);
            }
        });
    }

    @Test
    void t7_modificarInventarioInexistente_lanzaExcepcion() {
        assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                InventarioModificar modificar = new InventarioModificar();
                modificar.setIdInventario(-1);
                modificar.setCantidad(10);
                modificar.setIdProducto(idProductoPrueba);
                inventarioServicio.modificar(modificar);
            }
        });
    }

    @Test
    void t8_obtenerPorIdInexistente_lanzaExcepcion() {
        assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                inventarioServicio.obtenerPorId(-1);
            }
        });
    }

    @Test
    void t9_eliminarPorId() {
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                inventarioServicio.eliminarPorId(idInventarioCreado);
                productoServicio.eliminarPorId(idProductoPrueba);   // primero el inventario, luego el producto
            }
        });

        // Ya eliminado: buscarlo debe fallar
        assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                inventarioServicio.obtenerPorId(idInventarioCreado);
            }
        });
    }
}