
package org.esfe.Repositorios;

import org.esfe.Modelos.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IVentaRepositorio extends JpaRepository<Venta, Integer> {

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END " +
            "FROM DetalleVenta d WHERE d.venta.idVenta = :idVenta")
    boolean tieneDetalles(@Param("idVenta") Integer idVenta);

}
