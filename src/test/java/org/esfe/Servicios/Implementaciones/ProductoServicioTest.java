package org.esfe.Servicios.Implementaciones;

import org.esfe.Servicios.Interfaces.IProductoServicio;
import org.esfe.dtos.Producto.ProductoGuardar;
import org.esfe.dtos.Producto.ProductoModificar;
import org.esfe.dtos.Producto.ProductoSalida;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class ProductoServicioTest {

    @Autowired
    private IProductoServicio productoServicio;

    // Id del producto creado en t1, lo usan las pruebas siguientes (se borra en t7)
    private static Integer idProductoCreado;

    // Un producto que no existe puede devolver null o lanzar ResponseStatusException:
    // las dos formas cuentan como "no existe".
    private boolean noExiste(Integer id) {
        try {
            return productoServicio.obtenerPorId(id) == null;
        } catch (ResponseStatusException e) {
            return true;
        }
    }

    @Test
    void t1_crear() {
        ProductoGuardar guardar = new ProductoGuardar();
        guardar.setNombre("Laptop de prueba");
        guardar.setDescripcion("Producto creado por la prueba");
        guardar.setPrecio(new BigDecimal("750.00"));
        guardar.setStock(10);

        ProductoSalida productoSalida = productoServicio.crear(guardar);

        assertNotNull(productoSalida);
        assertNotNull(productoSalida.getIdProducto());
        assertEquals("Laptop de prueba", productoSalida.getNombre());
        assertEquals(10, productoSalida.getStock());
        idProductoCreado = productoSalida.getIdProducto();
    }

    @Test
    void t2_obtenerTodos() {
        assertTrue(productoServicio.obtenerTodos().size() >= 1);

        // Versión paginada
        assertTrue(productoServicio.obtenerTodosPaginados(PageRequest.of(0, 5)).getTotalElements() >= 1);
    }

    @Test
    void t3_obtenerPorId() {
        ProductoSalida productoSalida = productoServicio.obtenerPorId(idProductoCreado);
        assertNotNull(productoSalida);
        assertEquals("Laptop de prueba", productoSalida.getNombre());
        assertEquals(0, new BigDecimal("750.00").compareTo(productoSalida.getPrecio()));
    }

    @Test
    void t4_editar() {
        ProductoModificar modificar = new ProductoModificar();
        modificar.setIdProducto(idProductoCreado);
        modificar.setNombre("Laptop Modificada");
        modificar.setDescripcion("Producto modificado por la prueba");
        modificar.setPrecio(new BigDecimal("800.00"));
        modificar.setStock(15);

        ProductoSalida productoSalida = productoServicio.modificar(modificar);

        assertNotNull(productoSalida);
        assertEquals("Laptop Modificada", productoSalida.getNombre());
        assertEquals(15, productoSalida.getStock());
        assertEquals(0, new BigDecimal("800.00").compareTo(productoSalida.getPrecio()));
    }

    @Test
    void t5_obtenerPorIdInexistente() {
        assertTrue(noExiste(-1));
    }

    @Test
    void t6_modificarInexistente() {
        ProductoModificar modificar = new ProductoModificar();
        modificar.setIdProducto(-1);
        modificar.setNombre("No existe");
        modificar.setDescripcion("No existe");
        modificar.setPrecio(new BigDecimal("10.00"));
        modificar.setStock(1);

        ProductoSalida resultado = null;
        try {
            resultado = productoServicio.modificar(modificar);
        } catch (ResponseStatusException e) {
            // también es válido que lance la excepción
        }
        assertNull(resultado);
    }

    @Test
    void t7_eliminarPorId() {
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                productoServicio.eliminarPorId(idProductoCreado);
            }
        });

        // Ya a sido  eliminado
        assertTrue(noExiste(idProductoCreado));
    }
}