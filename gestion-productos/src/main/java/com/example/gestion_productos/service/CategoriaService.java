package com.example.gestion_productos.service;
import com.example.gestion_productos.entity.Categoria;
import com.example.gestion_productos.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // Guardar una nueva categoría
    public Categoria guardar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    // Actualizar una categoría existente
    public Categoria actualizar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    // Buscar todas las categorías
    @Transactional(readOnly = true)
    public List<Categoria> buscarTodas() {
        return categoriaRepository.findAll();
    }

    // Buscar categoría por ID
    @Transactional(readOnly = true)
    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    // Buscar categoría por nombre
    @Transactional(readOnly = true)
    public Optional<Categoria> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    // Buscar categorías principales (sin categoría padre)
    @Transactional(readOnly = true)
    public List<Categoria> buscarCategoriasPrincipales() {
        return categoriaRepository.findByCategoriaPadreIsNull();
    }

    // Buscar subcategorías de una categoría
    @Transactional(readOnly = true)
    public List<Categoria> buscarSubcategorias(Long categoriaId) {
        return categoriaRepository.findByCategoriaPadreCategoriaId(categoriaId);
    }

    // Eliminar una categoría
    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }

    // Verificar si la categoría tiene productos asociados
    @Transactional(readOnly = true)
    public boolean tieneProductosAsociados(Long categoriaId) {
        return categoriaRepository.existsByCategoriaPadreCategoriaIdAndProductosIsNotEmpty(categoriaId);
    }
}
