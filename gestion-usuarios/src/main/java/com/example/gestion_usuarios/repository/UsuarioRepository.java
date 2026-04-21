package com.example.gestion_usuarios.repository;

import com.example.gestion_usuarios.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreoUsuario(String correoUsuario);
    boolean existsByCorreoUsuario(String correoUsuario);
    List<Usuario> findByRolRolId(Long rolId);
    List<Usuario> findByTiendaTiendaId(Long tiendaId);
}
