package com.example.gestion_productos.repository;
import com.example.gestion_productos.entity.ProductoProveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoProveedorRepository extends JpaRepository<ProductoProveedor, ProductoProveedor.ProductoProveedorId>{
    // Buscar por producto y proveedor - Versión corregida
    Optional<ProductoProveedor> findByProductoProductoIdAndProveedorProveedorId(Long productoId, Long proveedorId);

    // Buscar todos los proveedores de un producto - Versión corregida
    List<ProductoProveedor> findByProductoProductoId(Long productoId);

    // Buscar todos los productos de un proveedor - Versión corregida
    List<ProductoProveedor> findByProveedorProveedorId(Long proveedorId);

}
