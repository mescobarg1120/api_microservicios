package com.example.gestion_productos.repository;
import com.example.gestion_productos.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Buscar por código SKU
    Optional<Producto> findByCodigoSku(String codigoSku);

    // Buscar por nombre que contenga (case insensitive)
    List<Producto> findByNombreProductoContainingIgnoreCase(String nombre);

    // Buscar por categoría
    List<Producto> findByCategoriaCategoriaId(Long categoriaId);

    // Buscar por rango de precio
    List<Producto> findByPrecioUnitarioBetween(BigDecimal precioMin, BigDecimal precioMax);

    // Buscar productos ecológicos
    List<Producto> findByEsEcologico(String esEcologico);

    // Buscar por proveedor (proveedor principal)
    List<Producto> findByProveedorProveedorId(Long proveedorId);


}
