package org.esfe.Controladores;

import org.esfe.Servicios.Interfaces.IClienteServicio;
import org.esfe.dtos.Cliente.ClienteGuardar;
import org.esfe.dtos.Cliente.ClienteModificar;
import org.esfe.dtos.Cliente.ClienteSalida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Clientes")
public class ClienteControlador {

    @Autowired
    private IClienteServicio clienteServicio;

    @GetMapping
    public ResponseEntity<Page<ClienteSalida>> mostrarTodosPaginados(Pageable pageable) {
        Page<ClienteSalida> clientes = clienteServicio.obtenerTodosPaginados(pageable);
        if (clientes.hasContent()) {
            return ResponseEntity.ok(clientes);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/lista")
    public ResponseEntity<List<ClienteSalida>> mostrarTodos() {
        List<ClienteSalida> clientes = clienteServicio.obtenerTodos();
        if (!clientes.isEmpty()) {
            return ResponseEntity.ok(clientes);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{IdCliente}")
    public ResponseEntity<ClienteSalida> buscarPorId(@PathVariable Integer IdCliente) {
        ClienteSalida cliente = clienteServicio.obtenerPorId(IdCliente);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ClienteSalida> crear(@RequestBody ClienteGuardar clienteGuardar) {
        ClienteSalida cliente = clienteServicio.crear(clienteGuardar);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{IdCliente}")
    public ResponseEntity<ClienteSalida> modificar(@PathVariable Integer IdCliente, @RequestBody ClienteModificar clienteModificar) {
        clienteModificar.setIdCliente(IdCliente);
        ClienteSalida cliente = clienteServicio.modificar(clienteModificar);
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{IdCliente}")
    public ResponseEntity<String> eliminar(@PathVariable Integer IdCliente) {
        clienteServicio.eliminarPorId(IdCliente);
        return ResponseEntity.ok("Cliente eliminado correctamente");
    }
}