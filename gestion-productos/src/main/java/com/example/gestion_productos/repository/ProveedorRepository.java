package com.example.gestion_productos.repository;
import com.example.gestion_productos.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    // Buscar por RUT
    Optional<Proveedor> findByRut(String rut);



    // Buscar por nombre que contenga (case insensitive)
    List<Proveedor> findByNombreContainingIgnoreCase(String nombre);

    // Buscar por ciudad
    List<Proveedor> findByCiudad(String ciudad);

    // Buscar proveedores ecológicos
    List<Proveedor> findByEsEcologico(String esEcologico);

    // Buscar por tipo de producto
    List<Proveedor> findByTipoProductosContainingIgnoreCase(String tipoProducto);

    // Buscar por país de origen
    List<Proveedor> findByPaisOrigen(String paisOrigen);


    // Verificar si existe un proveedor con productos asociados
    boolean existsByProveedorIdAndProductosProveedoresIsNotEmpty(Long proveedorId);


}
