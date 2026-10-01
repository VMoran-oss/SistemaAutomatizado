package org.esfe.Servicios.Implementaciones;

import lombok.RequiredArgsConstructor;
import org.esfe.Config.ModelMapperConfig;
import org.esfe.Modelos.Inventario;
import org.esfe.Repositorios.IInventarioRepositorio;
import org.esfe.Servicios.Interfaces.IInventarioServicio;
import org.esfe.dtos.Inventario.InventarioGuardar;
import org.esfe.dtos.Inventario.InventarioModificar;
import org.esfe.dtos.Inventario.InventarioSalida;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventarioServicio implements IInventarioServicio {

    @Autowired
    private IInventarioRepositorio inventarioRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<InventarioSalida> obtenerTodos() {
        List<Inventario> inventarios = inventarioRepositorio.findAll();

        return inventarios.stream()
                .map(Inventario -> modelMapper.map(Inventario, InventarioSalida.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<InventarioSalida> obtenerTodosPaginados(Pageable pageable) {
        Page<Inventario> page = inventarioRepositorio.findAll(pageable);

        List<InventarioSalida> Inventariodto = page.stream()
                .map(Inventario -> modelMapper.map(Inventario, InventarioSalida.class))
                .collect(Collectors.toList());
        return new PageImpl<>(Inventariodto, page.getPageable(), page.getTotalElements());
    }

    @Override
    public InventarioSalida obtenerPorId(Integer IdInventario) {
        return modelMapper.map(inventarioRepositorio.findById(IdInventario).get(), InventarioSalida.class);
    }

    @Override
    public InventarioSalida crear(InventarioGuardar inventarioGuardar) {
        Inventario inventario = inventarioRepositorio.save(modelMapper.map(inventarioGuardar, Inventario.class));
        return modelMapper.map(inventario, InventarioSalida.class);
    }

    @Override
    public InventarioSalida modificar(InventarioModificar inventarioModificar) {
        Inventario inventario = inventarioRepositorio.save(modelMapper.map(inventarioModificar, Inventario.class));
        return modelMapper.map(inventario, InventarioSalida.class);
    }

    @Override
    public void eliminarPorId(Integer IdInventario) {
        inventarioRepositorio.deleteById(IdInventario);
    }
}
