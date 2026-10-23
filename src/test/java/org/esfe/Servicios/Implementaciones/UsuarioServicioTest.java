package org.esfe.Servicios.Implementaciones;

import org.esfe.Servicios.Interfaces.IRolServicio;
import org.esfe.Servicios.Interfaces.IUsuarioServicio;
import org.esfe.dtos.Rol.RolGuardar;
import org.esfe.dtos.Rol.RolSalida;
import org.esfe.dtos.Usuario.UsuarioGuardar;
import org.esfe.dtos.Usuario.UsuarioModificar;
import org.esfe.dtos.Usuario.UsuarioSalida;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class UsuarioServicioTest {

    @Autowired
    private IUsuarioServicio usuarioServicio;

    @Autowired
    private IRolServicio rolServicio;

    // Datos compartidos entre las pruebas (se crean en t1 y se borran en t9)

    private static Integer idRolPrueba;
    private static Integer idUsuarioCreado;

    // Correo distinto en cada ejecución para no chocar con datos anteriores
    private static final String CORREO = "prueba" + System.currentTimeMillis() + "@correo.com";

    private UsuarioGuardar nuevoUsuario(String correo) {
        UsuarioGuardar guardar = new UsuarioGuardar();
        guardar.setNombre("Usuario de prueba");
        guardar.setCorreo(correo);
        guardar.setContrasena("clave123");
        guardar.setIdRol(idRolPrueba);
        return guardar;
    }

    @Test
    void t1_crear() {
        RolGuardar rolGuardar = new RolGuardar();
        rolGuardar.setNombre("ROL_PRUEBA");
        RolSalida rolSalida = rolServicio.crear(rolGuardar);
        idRolPrueba = rolSalida.getIdRol();

        UsuarioSalida usuarioSalida = usuarioServicio.crear(nuevoUsuario(CORREO));

        assertTrue(usuarioSalida != null);
        assertNotNull(usuarioSalida.getIdUsuario());
        assertEquals(CORREO, usuarioSalida.getCorreo());
        idUsuarioCreado = usuarioSalida.getIdUsuario();
    }

    @Test
    void t2_obtenerTodos() {
        int actual = usuarioServicio.obtenerTodos().size();
        assertTrue(actual >= 1);
    }

    @Test
    void t3_obtenerPorId() {
        UsuarioSalida usuarioSalida = usuarioServicio.obtenerPorId(idUsuarioCreado);
        assertNotEquals(null, usuarioSalida);
        assertEquals("Usuario de prueba", usuarioSalida.getNombre());
    }

    @Test
    void t4_editar() {
        UsuarioModificar modificar = new UsuarioModificar();
        modificar.setIdUsuario(idUsuarioCreado);
        modificar.setNombre("Usuario Modificado");
        modificar.setCorreo(CORREO);
        modificar.setIdRol(idRolPrueba);   // sin contraseña: conserva la anterior

        UsuarioSalida usuarioSalida = usuarioServicio.modificar(modificar);

        assertNotEquals(null, usuarioSalida);
        assertEquals("Usuario Modificado", usuarioSalida.getNombre());
    }

    @Test
    void t5_crearConCorreoDuplicado_lanzaExcepcion() {
        assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                usuarioServicio.crear(nuevoUsuario(CORREO));
            }
        });
    }

    @Test
    void t6_crearSinContrasena_lanzaExcepcion() {
        assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                UsuarioGuardar guardar = nuevoUsuario("otro" + System.currentTimeMillis() + "@correo.com");
                guardar.setContrasena("");
                usuarioServicio.crear(guardar);
            }
        });
    }

    @Test
    void t7_crearConRolInexistente_lanzaExcepcion() {
        assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                UsuarioGuardar guardar = nuevoUsuario("rol" + System.currentTimeMillis() + "@correo.com");
                guardar.setIdRol(-1);
                usuarioServicio.crear(guardar);
            }
        });
    }

    @Test
    void t8_obtenerPorIdInexistente_lanzaExcepcion() {
        assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                usuarioServicio.obtenerPorId(-1);
            }
        });
    }

    @Test
    void t9_eliminarPorId() {
        assertDoesNotThrow(new Executable() {
                               @Override
                               public void execute() throws Throwable {
                                   usuarioServicio.eliminarPorId(idUsuarioCreado);
                                   rolServicio.eliminarPorId(idRolPrueba);
                               }
                           }
        );
    }
}