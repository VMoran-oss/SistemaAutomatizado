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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        return modelMapper.map(buscarCliente(IdCliente), ClienteSalida.class);
    }

    @Override
    public ClienteSalida crear(ClienteGuardar clienteGuardar) {
        validarNombre(clienteGuardar.getNombre());

        Cliente cliente = clienteRepositorio.save(modelMapper.map(clienteGuardar, Cliente.class));
        return modelMapper.map(cliente, ClienteSalida.class);
    }

    @Override
    public ClienteSalida modificar(ClienteModificar clienteModificar) {
        validarNombre(clienteModificar.getNombre());


        Cliente cliente = buscarCliente(clienteModificar.getIdCliente());
        cliente.setNombre(clienteModificar.getNombre());
        cliente.setTelefono(clienteModificar.getTelefono());
        cliente.setCorreo(clienteModificar.getCorreo());

        return modelMapper.map(clienteRepositorio.save(cliente), ClienteSalida.class);
    }

    @Override
    public void eliminarPorId(Integer IdCliente) {
        clienteRepositorio.delete(buscarCliente(IdCliente));
    }

    private Cliente buscarCliente(Integer idCliente) {
        return clienteRepositorio.findById(idCliente)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del cliente es obligatorio");
        }
    }
}