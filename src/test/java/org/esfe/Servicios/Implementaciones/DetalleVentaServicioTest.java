package org.esfe.Servicios.Implementaciones;

import org.esfe.Servicios.Interfaces.IDetalleVentaServicio;
import org.esfe.Servicios.Interfaces.IVentaServicio;
import org.esfe.dtos.DetalleVenta.DetalleVentaGuardar;
import org.esfe.dtos.DetalleVenta.DetalleVentaModificar;
import org.esfe.dtos.DetalleVenta.DetalleVentaSalida;
import org.esfe.dtos.Venta.VentaGuardar;
import org.esfe.dtos.Venta.VentaSalida;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class DetalleVentaServicioTest {

    @Autowired
    private IDetalleVentaServicio detalleVentaServicio;

    @Autowired
    private IVentaServicio ventaServicio;

    // Datos compartidos entre las pruebas (se crean en t1 y se borran en t9)
    private static Integer idVentaPrueba;
    private static Integer idDetalleCreado;

    private DetalleVentaGuardar nuevoDetalle(Integer cantidad, BigDecimal subTotal, Integer idVenta) {
        DetalleVentaGuardar guardar = new DetalleVentaGuardar();
        guardar.setCantidad(cantidad);
        guardar.setSubTotal(subTotal);
        guardar.setIdVenta(idVenta);
        return guardar;
    }

    @Test
    void t1_crear() {
        // El detalle necesita una venta existente
        VentaGuardar ventaGuardar = new VentaGuardar();
        ventaGuardar.setFecha(LocalDate.now());
        ventaGuardar.setTotal(new BigDecimal("100.00"));
        ventaGuardar.setCliente("Cliente detalle prueba");
        ventaGuardar.setUsuario("admin");
        VentaSalida ventaSalida = ventaServicio.crear(ventaGuardar);
        idVentaPrueba = ventaSalida.getIdVenta();

        DetalleVentaSalida salida = detalleVentaServicio.crear(
                nuevoDetalle(2, new BigDecimal("50.00"), idVentaPrueba));

        assertNotNull(salida);
        assertNotNull(salida.getIdDetalleVenta());
        assertEquals(2, salida.getCantidad());
        assertNotNull(salida.getVenta());
        assertEquals(idVentaPrueba, salida.getVenta().getIdVenta());
        idDetalleCreado = salida.getIdDetalleVenta();
    }

    @Test
    void t2_obtenerTodos() {
        assertTrue(detalleVentaServicio.obtenerTodos().size() >= 1);

        // Versión paginada
        assertTrue(detalleVentaServicio.obtenerTodosPaginados(PageRequest.of(0, 5)).getTotalElements() >= 1);
    }

    @Test
    void t3_obtenerPorId() {
        DetalleVentaSalida salida = detalleVentaServicio.obtenerPorId(idDetalleCreado);
        assertNotNull(salida);
        assertEquals(2, salida.getCantidad());
        assertEquals(0, new BigDecimal("50.00").compareTo(salida.getSubTotal()));
    }

    @Test
    void t4_editar() {
        DetalleVentaModificar modificar = new DetalleVentaModificar();
        modificar.setIdDetalleVenta(idDetalleCreado);
        modificar.setCantidad(5);
        modificar.setSubTotal(new BigDecimal("125.00"));
        modificar.setIdVenta(idVentaPrueba);

        DetalleVentaSalida salida = detalleVentaServicio.modificar(modificar);

        assertNotNull(salida);
        assertEquals(5, salida.getCantidad());
        assertEquals(0, new BigDecimal("125.00").compareTo(salida.getSubTotal()));
    }

    @Test
    void t5_crearConCantidadCero_lanzaExcepcion() {
        assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                detalleVentaServicio.crear(nuevoDetalle(0, new BigDecimal("10.00"), idVentaPrueba));
            }
        });
    }

    @Test
    void t6_crearConSubTotalNegativo_lanzaExcepcion() {
        assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                detalleVentaServicio.crear(nuevoDetalle(1, new BigDecimal("-5.00"), idVentaPrueba));
            }
        });
    }

    @Test
    void t7_ventaOdetalleInexistente_lanzaExcepcion() {
        // Venta inexistente al crear
        assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                detalleVentaServicio.crear(nuevoDetalle(1, new BigDecimal("10.00"), -1));
            }
        });

        // Detalle inexistente al buscar
        assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                detalleVentaServicio.obtenerPorId(-1);
            }
        });
    }

    @Test
    void t8_modificarDetalleInexistente_lanzaExcepcion() {
        assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                DetalleVentaModificar modificar = new DetalleVentaModificar();
                modificar.setIdDetalleVenta(-1);
                modificar.setCantidad(1);
                modificar.setSubTotal(new BigDecimal("10.00"));
                modificar.setIdVenta(idVentaPrueba);
                detalleVentaServicio.modificar(modificar);
            }
        });
    }

    @Test
    void t9_eliminarPorId() {
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                detalleVentaServicio.eliminarPorId(idDetalleCreado);
                ventaServicio.eliminarPorId(idVentaPrueba);   // primero el detalle, luego la venta
            }
        });

        // Ya eliminado: buscarlo debe fallar
        assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                detalleVentaServicio.obtenerPorId(idDetalleCreado);
            }
        });
    }
}