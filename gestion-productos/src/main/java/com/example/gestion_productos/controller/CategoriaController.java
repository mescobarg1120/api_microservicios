package com.example.gestion_productos.controller;
import com.example.gestion_productos.entity.Categoria;
import com.example.gestion_productos.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")

public class CategoriaController {

    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // Obtener todas las categorías
    @GetMapping
    public ResponseEntity<List<Categoria>> obtenerTodasCategorias() {
        List<Categoria> categorias = categoriaService.buscarTodas();
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    // Obtener categoría por ID
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerCategoriaPorId(@PathVariable Long id) {
        return categoriaService.buscarPorId(id)
                .map(categoria -> new ResponseEntity<>(categoria, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Obtener categorías principales (sin categoría padre)
    @GetMapping("/principales")
    public ResponseEntity<List<Categoria>> obtenerCategoriasPrincipales() {
        List<Categoria> categorias = categoriaService.buscarCategoriasPrincipales();
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    // Obtener subcategorías de una categoría
    @GetMapping("/{id}/subcategorias")
    public ResponseEntity<List<Categoria>> obtenerSubcategorias(@PathVariable Long id) {
        List<Categoria> subcategorias = categoriaService.buscarSubcategorias(id);
        return new ResponseEntity<>(subcategorias, HttpStatus.OK);
    }

    // Crear una nueva categoría
    @PostMapping
    public ResponseEntity<Categoria> crearCategoria(@Valid @RequestBody Categoria categoria) {
        Categoria nuevaCategoria = categoriaService.guardar(categoria);
        return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
    }

    // Actualizar una categoría existente
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable Long id, @Valid @RequestBody Categoria categoria) {
        return categoriaService.buscarPorId(id)
                .map(categoriaExistente -> {
                    categoria.setCategoriaId(id);
                    Categoria categoriaActualizada = categoriaService.actualizar(categoria);
                    return new ResponseEntity<>(categoriaActualizada, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Eliminar una categoría
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        return categoriaService.buscarPorId(id)
                .map(categoria -> {
                    // Verificar si tiene productos asociados
                    if (categoriaService.tieneProductosAsociados(id)) {
                        return new ResponseEntity<Void>(HttpStatus.CONFLICT);
                    }
                    categoriaService.eliminar(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }



}
