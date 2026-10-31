package org.esfe.Servicios.Implementaciones;

import org.esfe.Servicios.Interfaces.IRolServicio;
import org.esfe.Servicios.Interfaces.IUsuarioServicio;
import org.esfe.dtos.Rol.RolGuardar;
import org.esfe.dtos.Rol.RolModificar;
import org.esfe.dtos.Rol.RolSalida;
import org.esfe.dtos.Usuario.UsuarioGuardar;
import org.esfe.dtos.Usuario.UsuarioSalida;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RolServicioTest {

    @Autowired
    private IRolServicio rolServicio;

    @Autowired
    private IUsuarioServicio usuarioServicio;

    private static Integer idRolCreado;

    private static final String NOMBRE = "ROL_TEST_" + System.currentTimeMillis();
    private static final String NOMBRE_MODIFICADO = NOMBRE + "_MOD";

    private void assertCodigoHttp(int codigoEsperado, Executable accion) {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, accion);
        assertEquals(codigoEsperado, ex.getStatusCode().value());
    }

    @Test
    @Order(1)
    void crear() {
        RolSalida salida = rolServicio.crear(new RolGuardar(NOMBRE));

        assertNotNull(salida);
        assertNotNull(salida.getIdRol());
        assertEquals(NOMBRE, salida.getNombre());
        idRolCreado = salida.getIdRol();
    }

    @Test
    @Order(2)
    void obtenerTodos() {
        int actual = rolServicio.obtenerTodos().size();
        assertTrue(actual >= 1);
    }

    @Test
    @Order(3)
    void obtenerTodosPaginados() {
        Page<RolSalida> pagina = rolServicio.obtenerTodosPaginados(PageRequest.of(0, 5));

        assertFalse(pagina.getContent().isEmpty());
        assertTrue(pagina.getContent().size() <= 5);   // nunca más que el tamaño de página
        assertTrue(pagina.getTotalElements() >= 1);
    }

    @Test
    @Order(4)
    void obtenerPorId() {
        RolSalida salida = rolServicio.obtenerPorId(idRolCreado);

        assertNotNull(salida);
        assertEquals(idRolCreado, salida.getIdRol());
        assertEquals(NOMBRE, salida.getNombre());
    }

    @Test
    @Order(5)
    void modificar() {
        RolModificar modificar = new RolModificar();
        modificar.setIdRol(idRolCreado);
        modificar.setNombre(NOMBRE_MODIFICADO);

        RolSalida salida = rolServicio.modificar(modificar);

        assertNotNull(salida);
        assertEquals(idRolCreado, salida.getIdRol());   // el mismo registro, no uno nuevo
        assertEquals(NOMBRE_MODIFICADO, salida.getNombre());

        // Se vuelve a leer para comprobar que el cambio quedó guardado en la base de datos
        assertEquals(NOMBRE_MODIFICADO, rolServicio.obtenerPorId(idRolCreado).getNombre());
    }

    @Test
    @Order(6)
    void crearSinNombre_lanzaExcepcion() {
        assertCodigoHttp(400, () -> rolServicio.crear(new RolGuardar("")));
    }

    @Test
    @Order(7)
    void crearConNombreNulo_lanzaExcepcion() {
        assertCodigoHttp(400, () -> rolServicio.crear(new RolGuardar(null)));
    }

    @Test
    @Order(8)
    void obtenerPorIdInexistente_lanzaExcepcion() {
        assertCodigoHttp(404, () -> rolServicio.obtenerPorId(-1));
    }

    @Test
    @Order(9)
    void modificarInexistente_lanzaExcepcion() {
        RolModificar modificar = new RolModificar();
        modificar.setIdRol(-1);
        modificar.setNombre("NO_EXISTE");

        assertCodigoHttp(404, () -> rolServicio.modificar(modificar));
    }

    @Test
    @Order(10)
    void modificarSinNombre_lanzaExcepcion() {
        RolModificar modificar = new RolModificar();
        modificar.setIdRol(idRolCreado);
        modificar.setNombre("   ");   // solo espacios también cuenta como vacío

        assertCodigoHttp(400, () -> rolServicio.modificar(modificar));
    }

    @Test
    @Order(11)
    void eliminarRolEnUso_lanzaExcepcion() {
        // Un usuario usa este rol, así que la base de datos (llave foránea) debe impedir borrarlo
        UsuarioGuardar guardar = new UsuarioGuardar();
        guardar.setNombre("Usuario con rol de prueba");
        guardar.setCorreo("rolenuso" + System.currentTimeMillis() + "@correo.com");
        guardar.setContrasena("clave123");
        guardar.setIdRol(idRolCreado);
        UsuarioSalida usuario = usuarioServicio.crear(guardar);

        try {
            assertThrows(DataIntegrityViolationException.class, () -> rolServicio.eliminarPorId(idRolCreado));
        } finally {
            // Se borra el usuario aunque la prueba falle, para no dejar basura y liberar el rol
            usuarioServicio.eliminarPorId(usuario.getIdUsuario());
        }
    }

    @Test
    @Order(12)
    void eliminarPorId() {
        assertDoesNotThrow(() -> rolServicio.eliminarPorId(idRolCreado));

        // Confirmamos que de verdad ya no existe
        assertCodigoHttp(404, () -> rolServicio.obtenerPorId(idRolCreado));
    }

    @Test
    @Order(13)
    void eliminarInexistente_lanzaExcepcion() {
        assertCodigoHttp(404, () -> rolServicio.eliminarPorId(-1));
    }
}