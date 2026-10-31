package org.esfe.Servicios.Implementaciones;

import org.esfe.Servicios.Interfaces.IFacturaServicio;
import org.esfe.Servicios.Interfaces.IVentaServicio;
import org.esfe.dtos.Factura.FacturaGuardar;
import org.esfe.dtos.Factura.FacturaModificar;
import org.esfe.dtos.Factura.FacturaSalida;
import org.esfe.dtos.Venta.VentaGuardar;
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
class FacturaServicioTest {

    @Autowired
    private IFacturaServicio facturaServicio;

    @Autowired
    private IVentaServicio ventaServicio;

    // Datos compartidos entre las pruebas (se crean en t1 y se borran en t9)
    private static Integer idVentaPrueba;
    private static Integer idFacturaCreada;

    // Número distinto en cada ejecución para no chocar con datos anteriores
    private static final String NUMERO = "F-" + System.currentTimeMillis();

    private FacturaGuardar nuevaFactura(Integer idVenta) {
        FacturaGuardar guardar = new FacturaGuardar();
        guardar.setNumeroFactura(NUMERO);
        guardar.setFecha(LocalDate.now());
        guardar.setSubTotal(new BigDecimal("100.00"));
        guardar.setIVA(new BigDecimal("13.00"));
        guardar.setTotal(new BigDecimal("113.00"));
        guardar.setMetodoDePago("Efectivo");
        guardar.setEstado("Pagada");
        guardar.setDescuento(0);
        guardar.setIdVenta(idVenta);
        return guardar;
    }

    private FacturaModificar facturaModificar(Integer idFactura, Integer idVenta) {
        FacturaModificar modificar = new FacturaModificar();
        modificar.setIdFactura(idFactura);
        modificar.setNumeroFactura(NUMERO);
        modificar.setFecha(LocalDate.now());
        modificar.setSubTotal(new BigDecimal("200.00"));
        modificar.setIVA(new BigDecimal("26.00"));
        modificar.setTotal(new BigDecimal("226.00"));
        modificar.setMetodoDePago("Tarjeta");
        modificar.setEstado("Pendiente");
        modificar.setDescuento(0);
        modificar.setIdVenta(idVenta);
        return modificar;
    }

    @Test
    void t1_crear() {
        VentaSalida ventaSalida = ventaServicio.crear(new VentaGuardar(
                LocalDate.now(), new BigDecimal("113.00"), "Cliente factura", "admin", null));
        idVentaPrueba = ventaSalida.getIdVenta();

        FacturaSalida facturaSalida = facturaServicio.crear(nuevaFactura(idVentaPrueba));

        assertNotNull(facturaSalida);
        assertNotNull(facturaSalida.getIdFactura());
        assertEquals(NUMERO, facturaSalida.getNumeroFactura());
        idFacturaCreada = facturaSalida.getIdFactura();
    }

    @Test
    void t2_obtenerTodos() {
        assertTrue(facturaServicio.obtenerTodos().size() >= 1);
    }

    @Test
    void t3_obtenerPorId() {
        FacturaSalida facturaSalida = facturaServicio.obtenerPorId(idFacturaCreada);
        assertNotNull(facturaSalida);
        assertEquals(NUMERO, facturaSalida.getNumeroFactura());
        assertEquals(0, new BigDecimal("113.00").compareTo(facturaSalida.getTotal()));
        assertEquals(idVentaPrueba, facturaSalida.getVenta().getIdVenta());
    }

    @Test
    void t4_editar() {
        FacturaSalida facturaSalida = facturaServicio.modificar(
                facturaModificar(idFacturaCreada, idVentaPrueba));

        assertNotNull(facturaSalida);
        assertEquals(0, new BigDecimal("226.00").compareTo(facturaSalida.getTotal()));
        assertEquals("Tarjeta", facturaSalida.getMetodoDePago());
        assertEquals("Pendiente", facturaSalida.getEstado());
    }

    @Test
    void t5_crearConVentaInexistente_lanzaExcepcion() {
        assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                facturaServicio.crear(nuevaFactura(-1));
            }
        });
    }

    @Test
    void t6_modificarConVentaInexistente_lanzaExcepcion() {
        assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                facturaServicio.modificar(facturaModificar(idFacturaCreada, -1));
            }
        });
    }

    @Test
    void t7_modificarFacturaInexistente_lanzaExcepcion() {
        assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                facturaServicio.modificar(facturaModificar(-1, idVentaPrueba));
            }
        });
    }

    @Test
    void t8_obtenerPorIdInexistente_devuelveNull() {
        // obtenerPorId no lanza excepción: devuelve null cuando no existe
        assertNull(facturaServicio.obtenerPorId(-1));
    }

    @Test
    void t9_eliminarPorId() {
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                facturaServicio.eliminarPorId(idFacturaCreada);
                ventaServicio.eliminarPorId(idVentaPrueba);   // primero la factura, luego la venta
            }
        });

        // Ya eliminada: buscarla devuelve null
        assertNull(facturaServicio.obtenerPorId(idFacturaCreada));
    }
}