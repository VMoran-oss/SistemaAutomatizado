package org.esfe.Controladores;

import org.esfe.Servicios.Interfaces.IDetalleVentaServicio;
import org.esfe.dtos.DetalleVenta.DetalleVentaGuardar;
import org.esfe.dtos.DetalleVenta.DetalleVentaModificar;
import org.esfe.dtos.DetalleVenta.DetalleVentaSalida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detallesVenta")
public class DetalleVentaControlador {

    @Autowired
    private IDetalleVentaServicio detalleVentaServicio;

    @GetMapping
    public ResponseEntity<Page<DetalleVentaSalida>> mostrarTodosPaginados(Pageable pageable) {
        Page<DetalleVentaSalida> detalles = detalleVentaServicio.obtenerTodosPaginados(pageable);
        if (detalles.hasContent()) {
            return ResponseEntity.ok(detalles);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/lista")
    public ResponseEntity<List<DetalleVentaSalida>> mostrarTodos() {
        List<DetalleVentaSalida> detalles = detalleVentaServicio.obtenerTodos();
        if (!detalles.isEmpty()) {
            return ResponseEntity.ok(detalles);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{idDetalleVenta}")
    public ResponseEntity<DetalleVentaSalida> buscarPorId(@PathVariable Integer idDetalleVenta) {
        DetalleVentaSalida detalle = detalleVentaServicio.obtenerPorId(idDetalleVenta);
        if (detalle != null) {
            return ResponseEntity.ok(detalle);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<DetalleVentaSalida> crear(@RequestBody DetalleVentaGuardar detalleVentaGuardar) {
        DetalleVentaSalida detalleCreado = detalleVentaServicio.crear(detalleVentaGuardar);
        return ResponseEntity.ok(detalleCreado);
    }

    @PutMapping("/{idDetalleVenta}")
    public ResponseEntity<DetalleVentaSalida> modificar(
            @PathVariable Integer idDetalleVenta,
            @RequestBody DetalleVentaModificar detalleVentaModificar) {
        detalleVentaModificar.setIdDetalleVenta(idDetalleVenta);
        DetalleVentaSalida detalleModificado = detalleVentaServicio.modificar(detalleVentaModificar);
        return ResponseEntity.ok(detalleModificado);
    }

    @DeleteMapping("/{idDetalleVenta}")
    public ResponseEntity<String> eliminar(@PathVariable Integer idDetalleVenta) {
        detalleVentaServicio.eliminarPorId(idDetalleVenta);
        return ResponseEntity.ok("Detalle de venta eliminado correctamente");
    }
}
