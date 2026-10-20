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
import org.springframework.stereotype.Service;

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
        return modelMapper.map(rolRepositorio.findById(idRol).get(), RolSalida.class);
    }

    @Override
    public RolSalida crear(RolGuardar rolGuardar) {
        Rol rol = rolRepositorio.save(modelMapper.map(rolGuardar, Rol.class));
        return modelMapper.map(rol, RolSalida.class);
    }

    @Override
    public RolSalida modificar(RolModificar rolModificar) {
        Rol rol = rolRepositorio.save(modelMapper.map(rolModificar, Rol.class));
        return modelMapper.map(rol, RolSalida.class);
    }

    @Override
    public void eliminarPorId(Integer idRol) {
        rolRepositorio.deleteById(idRol);
    }
}