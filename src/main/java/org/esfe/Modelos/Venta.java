package org.esfe.Modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

    @Getter
    @Setter
    @Entity
    @Table(name = "Ventas")
    public class Venta {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer idVenta;

        private LocalDate fecha;
        private BigDecimal total;
        private String cliente;
        private String usuario;

        @PrePersist
        void antesDeGuardar() {
            if (fecha == null) fecha = LocalDate.now();
        }
    }
