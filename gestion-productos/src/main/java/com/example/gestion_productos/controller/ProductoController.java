package com.example.gestion_productos.controller;
import com.example.gestion_productos.entity.Producto;
import com.example.gestion_productos.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")

public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> obtenerProductos() {
        List<Producto> Producto = productoService.obtenerTodosLosProductos();
        return new ResponseEntity<>(Producto, HttpStatus.OK);
    }

    // Crear un nuevo producto
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody Producto producto) {
        Producto nuevoProducto = productoService.guardar(producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    // Actualizar un producto existente
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        return productoService.buscarPorId(id)
                .map(productoExistente -> {
                    producto.setProductoId(id);
                    Producto productoActualizado = productoService.actualizar(producto);
                    return new ResponseEntity<>(productoActualizado, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }



    // Eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        return productoService.buscarPorId(id)
                .map(producto -> {
                    productoService.eliminar(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    // Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        return productoService.buscarPorId(id)
                .map(producto -> new ResponseEntity<>(producto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Obtener producto por código SKU
    @GetMapping("/sku/{codigoSku}")
    public ResponseEntity<Producto> obtenerProductoPorSku(@PathVariable String codigoSku) {
        return productoService.buscarPorCodigoSku(codigoSku)
                .map(producto -> new ResponseEntity<>(producto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Buscar productos por categoría
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> buscarPorCategoria(@PathVariable Long categoriaId) {
        List<Producto> productos = productoService.buscarPorCategoria(categoriaId);
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }


    // Buscar productos ecológicos
    @GetMapping("/ecologicos")
    public ResponseEntity<List<Producto>> buscarProductosEcologicos() {
        List<Producto> productos = productoService.buscarProductosEcologicos();
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    // Buscar productos por proveedor
    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<List<Producto>> buscarPorProveedor(@PathVariable Long proveedorId) {
        List<Producto> productos = productoService.buscarPorProveedor(proveedorId);
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }


}
