package com.example.gestion_productos.controller;
import com.example.gestion_productos.entity.ProductoProveedor;
import com.example.gestion_productos.service.ProductoProveedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/producto-proveedor")
@CrossOrigin(origins = "*")
public class ProductoProveedorController {

    private final ProductoProveedorService productoProveedorService;

    @Autowired
    public ProductoProveedorController(ProductoProveedorService productoProveedorService) {
        this.productoProveedorService = productoProveedorService;
    }

    // Obtener todas las relaciones producto-proveedor
    @GetMapping
    public ResponseEntity<List<ProductoProveedor>> obtenerTodas() {
        List<ProductoProveedor> relaciones = productoProveedorService.buscarTodas();
        return new ResponseEntity<>(relaciones, HttpStatus.OK);
    }

    // Obtener relación por producto y proveedor
    @GetMapping("/{productoId}/{proveedorId}")
    public ResponseEntity<ProductoProveedor> obtenerPorProductoYProveedor(
            @PathVariable Long productoId,
            @PathVariable Long proveedorId) {
        return productoProveedorService.buscarPorProductoYProveedor(productoId, proveedorId)
                .map(relacion -> new ResponseEntity<>(relacion, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Obtener proveedores de un producto
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<ProductoProveedor>> obtenerProveedoresPorProducto(@PathVariable Long productoId) {
        List<ProductoProveedor> proveedores = productoProveedorService.buscarProveedoresPorProducto(productoId);
        return new ResponseEntity<>(proveedores, HttpStatus.OK);
    }

    // Obtener productos de un proveedor
    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<List<ProductoProveedor>> obtenerProductosPorProveedor(@PathVariable Long proveedorId) {
        List<ProductoProveedor> productos = productoProveedorService.buscarProductosPorProveedor(proveedorId);
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    // Crear una nueva relación producto-proveedor
    @PostMapping
    public ResponseEntity<ProductoProveedor> crearRelacion(@Valid @RequestBody ProductoProveedor productoProveedor) {
        ProductoProveedor nuevaRelacion = productoProveedorService.guardar(productoProveedor);
        return new ResponseEntity<>(nuevaRelacion, HttpStatus.CREATED);
    }

    // Crear una relación producto-proveedor con IDs
    @PostMapping("/relacionar")
    public ResponseEntity<ProductoProveedor> relacionarProductoProveedor(
            @RequestParam Long productoId,
            @RequestParam Long proveedorId,
            @RequestParam BigDecimal precioCompra) {
        ProductoProveedor relacion = productoProveedorService.crearRelacion(productoId, proveedorId, precioCompra);
        if (relacion != null) {
            return new ResponseEntity<>(relacion, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Actualizar precio de compra
    @PatchMapping("/{productoId}/{proveedorId}/precio")
    public ResponseEntity<ProductoProveedor> actualizarPrecioCompra(
            @PathVariable Long productoId,
            @PathVariable Long proveedorId,
            @RequestParam BigDecimal precio) {
        ProductoProveedor relacion = productoProveedorService.actualizarPrecioCompra(productoId, proveedorId, precio);
        if (relacion != null) {
            return new ResponseEntity<>(relacion, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Establecer como proveedor principal
    @PatchMapping("/{productoId}/{proveedorId}/principal")
    public ResponseEntity<ProductoProveedor> establecerComoProveedorPrincipal(
            @PathVariable Long productoId,
            @PathVariable Long proveedorId) {
        ProductoProveedor relacion = productoProveedorService.establecerComoProveedorPrincipal(productoId, proveedorId);
        if (relacion != null) {
            return new ResponseEntity<>(relacion, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Registrar una compra
    @PatchMapping("/{productoId}/{proveedorId}/compra")
    public ResponseEntity<ProductoProveedor> registrarCompra(
            @PathVariable Long productoId,
            @PathVariable Long proveedorId) {
        ProductoProveedor relacion = productoProveedorService.registrarCompra(productoId, proveedorId);
        if (relacion != null) {
            return new ResponseEntity<>(relacion, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Eliminar una relación producto-proveedor
    @DeleteMapping("/{productoId}/{proveedorId}")
    public ResponseEntity<Void> eliminarRelacion(
            @PathVariable Long productoId,
            @PathVariable Long proveedorId) {
        if (productoProveedorService.buscarPorProductoYProveedor(productoId, proveedorId).isPresent()) {
            productoProveedorService.eliminar(productoId, proveedorId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
