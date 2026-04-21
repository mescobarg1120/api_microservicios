package com.example.gestion_productos.repository;
import com.example.gestion_productos.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Buscar por nombre exacto
    Optional<Categoria> findByNombre(String nombre);


    // Buscar categorías principales (sin categoría padre)
    List<Categoria> findByCategoriaPadreIsNull();

    // Buscar subcategorías de una categoría - Método corregido
    List<Categoria> findByCategoriaPadreCategoriaId(Long categoriaPadreId);

    // Verificar si existe una categoría con productos - Método corregido
    boolean existsByCategoriaPadreCategoriaIdAndProductosIsNotEmpty(Long categoriaPadreId);

}
