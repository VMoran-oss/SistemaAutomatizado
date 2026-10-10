package org.esfe.Servicios.Implementaciones;

import org.esfe.Modelos.Rol;
import org.esfe.Modelos.Usuario;
import org.esfe.Repositorios.IRolRepositorio;
import org.esfe.Repositorios.IUsuarioRepositorio;
import org.esfe.Servicios.Interfaces.IUsuarioServicio;
import org.esfe.dtos.Usuario.UsuarioGuardar;
import org.esfe.dtos.Usuario.UsuarioModificar;
import org.esfe.dtos.Usuario.UsuarioSalida;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServicio implements IUsuarioServicio {

    @Autowired
    private IUsuarioRepositorio usuarioRepositorio;

    @Autowired
    private IRolRepositorio rolRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public List<UsuarioSalida> obtenerTodos() {
        List<Usuario> usuarios = usuarioRepositorio.findAll();

        return usuarios.stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioSalida.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<UsuarioSalida> obtenerTodosPaginados(Pageable pageable) {
        Page<Usuario> page = usuarioRepositorio.findAll(pageable);

        List<UsuarioSalida> usuariosDto = page.stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioSalida.class))
                .collect(Collectors.toList());

        return new PageImpl<>(usuariosDto, page.getPageable(), page.getTotalElements());
    }

    @Override
    public UsuarioSalida obtenerPorId(Integer idUsuario) {
        return modelMapper.map(buscarUsuario(idUsuario), UsuarioSalida.class);
    }

    @Override
    public UsuarioSalida crear(UsuarioGuardar usuarioGuardar) {
        validarObligatorios(usuarioGuardar.getNombre(), usuarioGuardar.getCorreo(), usuarioGuardar.getIdRol());
        if (usuarioGuardar.getContrasena() == null || usuarioGuardar.getContrasena().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña es obligatoria");
        }
        if (usuarioRepositorio.existsByCorreo(usuarioGuardar.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con ese correo");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioGuardar.getNombre());
        usuario.setCorreo(usuarioGuardar.getCorreo());
        usuario.setContrasena(encoder.encode(usuarioGuardar.getContrasena()));
        usuario.setRol(buscarRol(usuarioGuardar.getIdRol()));

        return modelMapper.map(usuarioRepositorio.save(usuario), UsuarioSalida.class);
    }

    @Override
    public UsuarioSalida modificar(UsuarioModificar usuarioModificar) {
        validarObligatorios(usuarioModificar.getNombre(), usuarioModificar.getCorreo(), usuarioModificar.getIdRol());
        Usuario usuario = buscarUsuario(usuarioModificar.getIdUsuario());

        usuarioRepositorio.findByCorreo(usuarioModificar.getCorreo())
                .filter(otro -> !otro.getIdUsuario().equals(usuario.getIdUsuario()))
                .ifPresent(otro -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con ese correo");
                });

        usuario.setNombre(usuarioModificar.getNombre());
        usuario.setCorreo(usuarioModificar.getCorreo());
        usuario.setRol(buscarRol(usuarioModificar.getIdRol()));
        if (usuarioModificar.getContrasena() != null && !usuarioModificar.getContrasena().isBlank()) {
            usuario.setContrasena(encoder.encode(usuarioModificar.getContrasena()));
        }

        return modelMapper.map(usuarioRepositorio.save(usuario), UsuarioSalida.class);
    }

    @Override
    public void eliminarPorId(Integer idUsuario) {
        usuarioRepositorio.delete(buscarUsuario(idUsuario));
    }

    private Usuario buscarUsuario(Integer idUsuario) {
        return usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    private Rol buscarRol(Integer idRol) {
        return rolRepositorio.findById(idRol)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rol indicado no existe"));
    }

    private void validarObligatorios(String nombre, String correo, Integer idRol) {
        if (nombre == null || nombre.isBlank() || correo == null || correo.isBlank() || idRol == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre, correo y rol son obligatorios");
        }
    }
}