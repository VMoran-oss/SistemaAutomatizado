package org.esfe.Repositorios;

import org.esfe.Modelos.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFacturaRepositorio extends JpaRepository<Factura, Integer> {
}