package com.example.gestion_usuarios.repository;
import com.example.gestion_usuarios.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombreDeRol(String nombreDeRol);
    boolean existsByNombreDeRol(String nombreDeRol);
}
