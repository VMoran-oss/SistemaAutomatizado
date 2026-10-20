package org.esfe.Controladores;

import org.esfe.Servicios.Interfaces.IRolServicio;
import org.esfe.dtos.Rol.RolGuardar;
import org.esfe.dtos.Rol.RolModificar;
import org.esfe.dtos.Rol.RolSalida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Roles")
public class RolControlador {

    @Autowired
    private IRolServicio rolServicio;

    @GetMapping
    public ResponseEntity<Page<RolSalida>> mostrarTodosPaginados(Pageable pageable) {
        Page<RolSalida> roles = rolServicio.obtenerTodosPaginados(pageable);
        if (roles.hasContent()) {
            return ResponseEntity.ok(roles);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/lista")
    public ResponseEntity<List<RolSalida>> mostrarTodos() {
        List<RolSalida> roles = rolServicio.obtenerTodos();
        if (!roles.isEmpty()) {
            return ResponseEntity.ok(roles);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{idRol}")
    public ResponseEntity<RolSalida> buscarPorId(@PathVariable Integer idRol) {
        RolSalida rol = rolServicio.obtenerPorId(idRol);
        if (rol != null) {
            return ResponseEntity.ok(rol);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<RolSalida> crear(@RequestBody RolGuardar rolGuardar) {
        RolSalida rol = rolServicio.crear(rolGuardar);
        return ResponseEntity.ok(rol);
    }

    @PutMapping("/{idRol}")
    public ResponseEntity<RolSalida> modificar(@PathVariable Integer idRol, @RequestBody RolModificar rolModificar) {
        rolModificar.setIdRol(idRol);
        RolSalida rol = rolServicio.modificar(rolModificar);
        return ResponseEntity.ok(rol);
    }

    @DeleteMapping("/{idRol}")
    public ResponseEntity<String> eliminar(@PathVariable Integer idRol) {
        rolServicio.eliminarPorId(idRol);
        return ResponseEntity.ok("Rol eliminado correctamente");
    }
}