package org.esfe.Repositorios;
import org.esfe.Modelos.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductoRepositorio extends JpaRepository<Producto, Integer> {
}
