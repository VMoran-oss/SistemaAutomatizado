package org.esfe.Controladores;

import org.esfe.Servicios.Interfaces.IUsuarioServicio;
import org.esfe.dtos.Usuario.UsuarioGuardar;
import org.esfe.dtos.Usuario.UsuarioModificar;
import org.esfe.dtos.Usuario.UsuarioSalida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Usuarios")
public class UsuarioControlador {

    @Autowired
    private IUsuarioServicio usuarioServicio;

    @GetMapping
    public ResponseEntity<Page<UsuarioSalida>> mostrarTodosPaginados(Pageable pageable) {
        Page<UsuarioSalida> usuarios = usuarioServicio.obtenerTodosPaginados(pageable);
        if (usuarios.hasContent()) {
            return ResponseEntity.ok(usuarios);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/lista")
    public ResponseEntity<List<UsuarioSalida>> mostrarTodos() {
        List<UsuarioSalida> usuarios = usuarioServicio.obtenerTodos();
        if (!usuarios.isEmpty()) {
            return ResponseEntity.ok(usuarios);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioSalida> buscarPorId(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(usuarioServicio.obtenerPorId(idUsuario));
    }

    @PostMapping
    public ResponseEntity<UsuarioSalida> crear(@RequestBody UsuarioGuardar usuarioGuardar) {
        return ResponseEntity.ok(usuarioServicio.crear(usuarioGuardar));
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<UsuarioSalida> modificar(@PathVariable Integer idUsuario, @RequestBody UsuarioModificar usuarioModificar) {
        usuarioModificar.setIdUsuario(idUsuario);
        return ResponseEntity.ok(usuarioServicio.modificar(usuarioModificar));
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<String> eliminar(@PathVariable Integer idUsuario) {
        usuarioServicio.eliminarPorId(idUsuario);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }
}