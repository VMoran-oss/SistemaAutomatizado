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
    public ResponseEntity<Page<VentaSalida>> mostrarTodosPaginados(Pageable pageable){
        Page<VentaSalida> ventas = ventaServicio.obtenerTodosPaginados(pageable);
        if(ventas.hasContent()){
            return ResponseEntity.ok(ventas);
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/lista")
    public ResponseEntity<List<VentaSalida>> mostrarTodos() {
        List<VentaSalida> ventas = ventaServicio.obtenerTodos();
        if(!ventas.isEmpty()) {
            return ResponseEntity.ok(ventas);
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/{IdVenta}")
    public ResponseEntity<VentaSalida> buscarPorId(@PathVariable Integer IdVenta){
        VentaSalida Venta = ventaServicio.obtenerPorId(IdVenta);
        if(Venta != null){
            return ResponseEntity.ok(Venta);
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping
    public ResponseEntity<VentaSalida> crear(@RequestBody VentaGuardar ventaGuardar){
        VentaSalida Venta = ventaServicio.crear(ventaGuardar);
        return ResponseEntity.ok(Venta);
    }
    @PutMapping("/{IdVenta}")
    public ResponseEntity<VentaSalida> modificar(@PathVariable Integer IdVenta, @RequestBody VentaModificar ventaModificar) {
        ventaModificar.setIdVenta(IdVenta);
        VentaSalida Venta = ventaServicio.modificar(ventaModificar);
        if(Venta != null){
            return ResponseEntity.ok(Venta);
        }
        return ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{IdVenta}")
    public ResponseEntity<String> eliminar(@PathVariable Integer IdVenta){
        ventaServicio.eliminarPorId(IdVenta);
        return ResponseEntity.ok("Venta Eliminada correctamente");
    }
}