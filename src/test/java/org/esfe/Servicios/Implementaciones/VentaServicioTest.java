package org.esfe.Servicios.Implementaciones;

import org.esfe.Servicios.Interfaces.IVentaServicio;
import org.esfe.dtos.Venta.VentaGuardar;
import org.esfe.dtos.Venta.VentaModificar;
import org.esfe.dtos.Venta.VentaSalida;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class VentaServicioTest {

    @Autowired
    private IVentaServicio ventaServicio;

    // Id de la venta creada en t1, la usan las pruebas siguientes
    private static Integer idVentaCreada;

    @Test
    void t1_crear() {
        VentaSalida ventaSalida = ventaServicio.crear(new VentaGuardar(
                LocalDate.now(), new BigDecimal("100.50"), "Cliente de prueba", "admin", null));
        assertTrue(ventaSalida != null);
        assertNotNull(ventaSalida.getIdVenta());
        idVentaCreada = ventaSalida.getIdVenta();
    }

    @Test
    void t2_obtenerTodos() {
        int actual = ventaServicio.obtenerTodos().size();
        assertTrue(actual >= 1);
    }

    @Test
    void t3_obtenerPorId() {
        VentaSalida ventaSalida = ventaServicio.obtenerPorId(idVentaCreada);
        assertNotEquals(null, ventaSalida);
        assertEquals("Cliente de prueba", ventaSalida.getCliente());
        assertEquals(0, new BigDecimal("100.50").compareTo(ventaSalida.getTotal()));
    }

    @Test
    void t4_editar() {
        VentaSalida ventaSalida = ventaServicio.modificar(new VentaModificar(
                idVentaCreada, LocalDate.now(), new BigDecimal("250.00"),
                "Cliente Modificado", "admin", null));
        assertNotEquals(null, ventaSalida);
        assertEquals("Cliente Modificado", ventaSalida.getCliente());
    }

    @Test
    void t5_crearConTotalNegativo_lanzaExcepcion() {
        assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                ventaServicio.crear(new VentaGuardar(
                        LocalDate.now(), new BigDecimal("-1"), "Cliente", "admin", null));
            }
        });
    }

    @Test
    void t6_crearSinCliente_lanzaExcepcion() {
        assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                ventaServicio.crear(new VentaGuardar(
                        LocalDate.now(), new BigDecimal("10"), "", "admin", null));
            }
        });
    }

    @Test
    void t7_obtenerPorIdInexistente_lanzaExcepcion() {
        assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                ventaServicio.obtenerPorId(-1);
            }
        });
    }

    @Test
    void t8_eliminarPorId() {
        assertDoesNotThrow(new Executable() {
                               @Override
                               public void execute() throws Throwable {
                                   ventaServicio.eliminarPorId(idVentaCreada);
                               }
                           }
        );
    }
}