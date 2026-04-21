package com.example.gestion_pedidos.repository;

import com.example.gestion_pedidos.entity.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPago,Long> {

    // Buscar métodos activos
    List<MetodoPago> findByActivoOrderByNombre(String activo);

    // Buscar por nombre
    Optional<MetodoPago> findByNombreIgnoreCase(String nombre);

    // Métodos que requieren verificación
    List<MetodoPago> findByRequiereVerificacionAndActivo(String requiereVerificacion, String activo);
}
