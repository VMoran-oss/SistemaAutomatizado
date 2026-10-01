package org.esfe.Controladores;

import lombok.Getter;
import org.esfe.Servicios.Interfaces.IInventarioServicio;
import org.esfe.dtos.Inventario.InventarioGuardar;
import org.esfe.dtos.Inventario.InventarioModificar;
import org.esfe.dtos.Inventario.InventarioSalida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Inventarios)")
public class InventarioControlador {

    @Autowired
    private IInventarioServicio inventarioServicio;

    @GetMapping
    public ResponseEntity<Page<InventarioSalida>> mostrarTodosPaginados(Pageable pageable){
        Page<InventarioSalida> inventarios = inventarioServicio.obtenerTodosPaginados(pageable);
        if(inventarios.hasContent()){
            return ResponseEntity.ok(inventarios);
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/lista")
    public ResponseEntity<List<InventarioSalida>> mostrarTodos() {
        List<InventarioSalida> inventarios = inventarioServicio.obtenerTodos();
        if(!inventarios.isEmpty()) {
            return ResponseEntity.ok(inventarios);
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/{IdInventario}")
    public ResponseEntity<InventarioSalida> buscarPorId(@PathVariable Integer IdInventario){
        InventarioSalida Inventario = inventarioServicio.obtenerPorId(IdInventario);
        if(Inventario != null){
            return ResponseEntity.ok(Inventario);
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping
    public ResponseEntity<InventarioSalida> crear(@RequestBody InventarioGuardar inventarioGuardar){
        InventarioSalida Inventario = inventarioServicio.crear(inventarioGuardar);
        return ResponseEntity.ok(Inventario);
    }
    @PutMapping("/{IdInventario}")
    public ResponseEntity<InventarioSalida> modificar(@PathVariable Integer IdInventario, @RequestBody InventarioModificar inventarioModificar) {
        InventarioSalida Inventario = inventarioServicio.modificar(inventarioModificar);
        return ResponseEntity.ok(Inventario);
    }
    @DeleteMapping("/{id}")
        public ResponseEntity eliminar(@PathVariable Integer IdInventario){
        inventarioServicio.eliminarPorId(IdInventario);
        return ResponseEntity.ok("Inventario Eliminado correctamente");
        }
}
