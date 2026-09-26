package org.esfe.Repositorios;

import org.esfe.Modelos.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFacturaRepositorio extends JpaRepository<Inventario, Integer> {
}
