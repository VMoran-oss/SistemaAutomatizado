package org.esfe.Repositorios;

import org.esfe.Modelos.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClienteRepositorio extends JpaRepository<Cliente,Integer> {
}
