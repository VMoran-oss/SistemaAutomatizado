package org.esfe.Repositorios;

import org.esfe.Modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUsuarioRepositorio extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);
}