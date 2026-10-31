package org.esfe.Servicios.Implementaciones;

import org.esfe.Modelos.Rol;
import org.esfe.Repositorios.IRolRepositorio;
import org.esfe.Servicios.Interfaces.IRolServicio;
import org.esfe.dtos.Rol.RolGuardar;
import org.esfe.dtos.Rol.RolModificar;
import org.esfe.dtos.Rol.RolSalida;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolServicio implements IRolServicio {

    @Autowired
    private IRolRepositorio rolRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<RolSalida> obtenerTodos() {
        List<Rol> roles = rolRepositorio.findAll();

        return roles.stream()
                .map(rol -> modelMapper.map(rol, RolSalida.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<RolSalida> obtenerTodosPaginados(Pageable pageable) {
        Page<Rol> page = rolRepositorio.findAll(pageable);

        List<RolSalida> rolesDto = page.stream()
                .map(rol -> modelMapper.map(rol, RolSalida.class))
                .collect(Collectors.toList());

        return new PageImpl<>(rolesDto, page.getPageable(), page.getTotalElements());
    }

    @Override
    public RolSalida obtenerPorId(Integer idRol) {
        return modelMapper.map(buscarRol(idRol), RolSalida.class);
    }

    @Override
    public RolSalida crear(RolGuardar rolGuardar) {
        validarNombre(rolGuardar.getNombre());

        Rol rol = rolRepositorio.save(modelMapper.map(rolGuardar, Rol.class));
        return modelMapper.map(rol, RolSalida.class);
    }

    @Override
    public RolSalida modificar(RolModificar rolModificar) {
        validarNombre(rolModificar.getNombre());


        Rol rol = buscarRol(rolModificar.getIdRol());
        rol.setNombre(rolModificar.getNombre());

        return modelMapper.map(rolRepositorio.save(rol), RolSalida.class);
    }

    @Override
    public void eliminarPorId(Integer idRol) {
        rolRepositorio.delete(buscarRol(idRol));
    }

    private Rol buscarRol(Integer idRol) {
        return rolRepositorio.findById(idRol)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado"));
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del rol es obligatorio");
        }
    }
}