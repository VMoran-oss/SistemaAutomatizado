package org.esfe.Repositorios;

import org.esfe.Modelos.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDetalleVentaRepositorio extends JpaRepository<DetalleVenta, Integer> {
}
