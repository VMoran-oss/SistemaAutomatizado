package org.esfe.Servicios.Implementaciones;

import org.esfe.Modelos.Cliente;
import org.esfe.Repositorios.IClienteRepositorio;
import org.esfe.Servicios.Interfaces.IClienteServicio;
import org.esfe.dtos.Cliente.ClienteGuardar;
import org.esfe.dtos.Cliente.ClienteModificar;
import org.esfe.dtos.Cliente.ClienteSalida;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteServicio implements IClienteServicio {

    @Autowired
    private IClienteRepositorio clienteRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ClienteSalida> obtenerTodos() {
        List<Cliente> clientes = clienteRepositorio.findAll();

        return clientes.stream()
                .map(cliente -> modelMapper.map(cliente, ClienteSalida.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<ClienteSalida> obtenerTodosPaginados(Pageable pageable) {
        Page<Cliente> page = clienteRepositorio.findAll(pageable);

        List<ClienteSalida> clientesDto = page.stream()
                .map(cliente -> modelMapper.map(cliente, ClienteSalida.class))
                .collect(Collectors.toList());

        return new PageImpl<>(clientesDto, page.getPageable(), page.getTotalElements());
    }

    @Override
    public ClienteSalida obtenerPorId(Integer IdCliente) {
        return modelMapper.map(clienteRepositorio.findById(IdCliente).get(), ClienteSalida.class);
    }

    @Override
    public ClienteSalida crear(ClienteGuardar clienteGuardar) {
        Cliente cliente = clienteRepositorio.save(modelMapper.map(clienteGuardar, Cliente.class));
        return modelMapper.map(cliente, ClienteSalida.class);
    }

    @Override
    public ClienteSalida modificar(ClienteModificar clienteModificar) {
        Cliente cliente = clienteRepositorio.save(modelMapper.map(clienteModificar, Cliente.class));
        return modelMapper.map(cliente, ClienteSalida.class);
    }

    @Override
    public void eliminarPorId(Integer IdCliente) {
        clienteRepositorio.deleteById(IdCliente);
    }
}