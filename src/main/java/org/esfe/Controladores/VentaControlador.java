package org.esfe.Controladores;

import org.esfe.Servicios.Interfaces.IVentaServicio;
import org.esfe.dtos.Venta.VentaGuardar;
import org.esfe.dtos.Venta.VentaModificar;
import org.esfe.dtos.Venta.VentaSalida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Ventas")
public class VentaControlador {

    @Autowired
    private IVentaServicio ventaServicio;

    @GetMapping
    public ResponseEntity<Page<VentaSalida>> mostrarTodosPaginados(Pageable pageable) {
        Page<VentaSalida> ventas = ventaServicio.obtenerTodosPaginados(pageable);
        if (ventas.hasContent()) {
            return ResponseEntity.ok(ventas);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/lista")
    public ResponseEntity<List<VentaSalida>> mostrarTodos() {
        List<VentaSalida> ventas = ventaServicio.obtenerTodos();
        if (!ventas.isEmpty()) {
            return ResponseEntity.ok(ventas);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{idVenta}")
    public ResponseEntity<VentaSalida> buscarPorId(@PathVariable Integer idVenta) {
        return ResponseEntity.ok(ventaServicio.obtenerPorId(idVenta));
    }

    @PostMapping
    public ResponseEntity<VentaSalida> crear(@RequestBody VentaGuardar ventaGuardar) {
        return ResponseEntity.ok(ventaServicio.crear(ventaGuardar));
    }

    @PutMapping("/{idVenta}")
    public ResponseEntity<VentaSalida> modificar(@PathVariable Integer idVenta, @RequestBody VentaModificar ventaModificar) {
        ventaModificar.setIdVenta(idVenta);
        return ResponseEntity.ok(ventaServicio.modificar(ventaModificar));
    }

    @DeleteMapping("/{idVenta}")
    public ResponseEntity<String> eliminar(@PathVariable Integer idVenta) {
        ventaServicio.eliminarPorId(idVenta);
        return ResponseEntity.ok("Venta eliminada correctamente");
    }
}