package org.esfe.Servicios.Implementaciones;

import org.esfe.Servicios.Interfaces.IClienteServicio;
import org.esfe.dtos.Cliente.ClienteGuardar;
import org.esfe.dtos.Cliente.ClienteModificar;
import org.esfe.dtos.Cliente.ClienteSalida;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClienteServicioTest {

    @Autowired
    private IClienteServicio clienteServicio;

    // Id del cliente creado en la prueba 1; lo usan las pruebas siguientes
    private static Integer idClienteCreado;

    private static final String NOMBRE = "Cliente de prueba";
    private static final Integer TELEFONO = 70001234;
    private static final String CORREO = "cliente.prueba@correo.com";

    // Ejecuta la acción y comprueba que lanza ResponseStatusException con el código HTTP esperado
    private void assertCodigoHttp(int codigoEsperado, Executable accion) {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, accion);
        assertEquals(codigoEsperado, ex.getStatusCode().value());
    }

    @Test
    @Order(1)
    void crear() {
        ClienteSalida salida = clienteServicio.crear(new ClienteGuardar(NOMBRE, TELEFONO, CORREO));

        assertNotNull(salida);
        assertNotNull(salida.getIdCliente());
        assertEquals(NOMBRE, salida.getNombre());
        assertEquals(TELEFONO, salida.getTelefono());
        assertEquals(CORREO, salida.getCorreo());
        idClienteCreado = salida.getIdCliente();
    }

    @Test
    @Order(2)
    void obtenerTodos() {
        int actual = clienteServicio.obtenerTodos().size();
        assertTrue(actual >= 1);
    }

    @Test
    @Order(3)
    void obtenerTodosPaginados() {
        Page<ClienteSalida> pagina = clienteServicio.obtenerTodosPaginados(PageRequest.of(0, 5));

        assertFalse(pagina.getContent().isEmpty());
        assertTrue(pagina.getContent().size() <= 5);   // nunca más que el tamaño de página
        assertTrue(pagina.getTotalElements() >= 1);
    }

    @Test
    @Order(4)
    void obtenerPorId() {
        ClienteSalida salida = clienteServicio.obtenerPorId(idClienteCreado);

        assertNotNull(salida);
        assertEquals(idClienteCreado, salida.getIdCliente());
        assertEquals(NOMBRE, salida.getNombre());
        assertEquals(TELEFONO, salida.getTelefono());
        assertEquals(CORREO, salida.getCorreo());
    }

    @Test
    @Order(5)
    void modificar() {
        ClienteModificar modificar = new ClienteModificar();
        modificar.setIdCliente(idClienteCreado);
        modificar.setNombre("Cliente Modificado");
        modificar.setTelefono(60009876);
        modificar.setCorreo("modificado@correo.com");

        ClienteSalida salida = clienteServicio.modificar(modificar);

        assertNotNull(salida);
        assertEquals(idClienteCreado, salida.getIdCliente());
        assertEquals("Cliente Modificado", salida.getNombre());

        ClienteSalida releido = clienteServicio.obtenerPorId(idClienteCreado);
        assertEquals("Cliente Modificado", releido.getNombre());
        assertEquals(Integer.valueOf(60009876), releido.getTelefono());
        assertEquals("modificado@correo.com", releido.getCorreo());
    }

    @Test
    @Order(6)
    void crearSinNombre_lanzaExcepcion() {
        assertCodigoHttp(400, () -> clienteServicio.crear(new ClienteGuardar("", TELEFONO, CORREO)));
    }

    @Test
    @Order(7)
    void crearConNombreNulo_lanzaExcepcion() {
        assertCodigoHttp(400, () -> clienteServicio.crear(new ClienteGuardar(null, TELEFONO, CORREO)));
    }

    @Test
    @Order(8)
    void obtenerPorIdInexistente_lanzaExcepcion() {
        assertCodigoHttp(404, () -> clienteServicio.obtenerPorId(-1));
    }

    @Test
    @Order(9)
    void modificarInexistente_lanzaExcepcion() {
        ClienteModificar modificar = new ClienteModificar();
        modificar.setIdCliente(-1);
        modificar.setNombre("No existe");

        assertCodigoHttp(404, () -> clienteServicio.modificar(modificar));
    }

    @Test
    @Order(10)
    void modificarSinNombre_lanzaExcepcion() {
        ClienteModificar modificar = new ClienteModificar();
        modificar.setIdCliente(idClienteCreado);
        modificar.setNombre("   ");
        assertCodigoHttp(400, () -> clienteServicio.modificar(modificar));
    }

    @Test
    @Order(11)
    void eliminarPorId() {
        assertDoesNotThrow(() -> clienteServicio.eliminarPorId(idClienteCreado));
        assertCodigoHttp(404, () -> clienteServicio.obtenerPorId(idClienteCreado));
    }

    @Test
    @Order(12)
    void eliminarInexistente_lanzaExcepcion() {
        assertCodigoHttp(404, () -> clienteServicio.eliminarPorId(-1));
    }
}