package com.example.gestion_usuarios.repository;
import com.example.gestion_usuarios.entity.Tienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TiendaRepository extends JpaRepository<Tienda, Long>{
    Optional<Tienda> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    List<Tienda> findByCiudad(String ciudad);
    List<Tienda> findByRegion(String region);
}
