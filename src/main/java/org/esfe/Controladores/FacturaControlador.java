package org.esfe.Controladores;

import org.esfe.Servicios.Interfaces.IFacturaServicio;
import org.esfe.dtos.Factura.FacturaGuardar;
import org.esfe.dtos.Factura.FacturaModificar;
import org.esfe.dtos.Factura.FacturaSalida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturaControlador {

    @Autowired
    private IFacturaServicio facturaServicio;

    @GetMapping
    public ResponseEntity<Page<FacturaSalida>> mostrarTodosPaginados(Pageable pageable) {

        Page<FacturaSalida> facturas = facturaServicio.obtenerTodosPaginados(pageable);

        if (facturas.hasContent()) {
            return ResponseEntity.ok(facturas);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/lista")
    public ResponseEntity<List<FacturaSalida>> mostrarTodos() {

        List<FacturaSalida> facturas = facturaServicio.obtenerTodos();

        if (!facturas.isEmpty()) {
            return ResponseEntity.ok(facturas);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{idFactura}")
    public ResponseEntity<FacturaSalida> buscarPorId(
            @PathVariable Integer idFactura
    ) {

        FacturaSalida factura = facturaServicio.obtenerPorId(idFactura);

        if (factura != null) {
            return ResponseEntity.ok(factura);
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{IdFactura}")
    public ResponseEntity<FacturaSalida> modificar(
            @PathVariable Integer IdFactura,
            @RequestBody FacturaModificar facturaModificar
    ) {
        facturaModificar.setIdFactura(IdFactura);

        FacturaSalida factura =
                facturaServicio.modificar(facturaModificar);

        return ResponseEntity.ok(factura);
    }

    @DeleteMapping("/{idFactura}")
    public ResponseEntity<String> eliminar(
            @PathVariable Integer idFactura
    ) {

        facturaServicio.eliminarPorId(idFactura);

        return ResponseEntity.ok("Factura eliminada correctamente");
    }
}
