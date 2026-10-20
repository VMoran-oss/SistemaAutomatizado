package org.esfe.Repositorios;

import org.esfe.Modelos.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IVentaRepositorio extends JpaRepository<Venta, Integer> {

    List<Venta> findByClienteIdCliente(Integer idCliente);
}