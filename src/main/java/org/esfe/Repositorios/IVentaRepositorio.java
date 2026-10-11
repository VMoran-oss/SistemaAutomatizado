package org.esfe.Repositorios;

import org.esfe.Modelos.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IVentaRepositorio extends JpaRepository<Venta, Integer> {
}