package org.esfe.Modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer IdFactura;

    @Column(unique = true, nullable = false)
    private String NumeroFactura;

    private LocalDate Fecha;

    private BigDecimal SubTotal;

    private BigDecimal IVA;

    private BigDecimal Total;

    private String MetodoDePago;

    private String Estado;

    private Integer Descuento;

    @OneToOne
    @JoinColumn(name = "idVenta")
    private Venta Venta;
}
