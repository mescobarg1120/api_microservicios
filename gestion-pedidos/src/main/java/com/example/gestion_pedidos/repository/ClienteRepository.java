package com.example.gestion_pedidos.repository;

import com.example.gestion_pedidos.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Buscar por email
    Optional<Cliente> findByEmail(String email);

    // Verificar si existe email
    boolean existsByEmail(String email);

    // Buscar clientes activos
    List<Cliente> findByEstado(String estado);

    // Buscar por ciudad
    List<Cliente> findByCiudadIgnoreCase(String ciudad);

    // Buscar por región
    List<Cliente> findByRegionIgnoreCase(String region);

    // Buscar clientes que prefieren productos ecológicos
    List<Cliente> findByPrefiereEcologicos(String prefiereEcologicos);

    // Buscar por tipo de cliente
    List<Cliente> findByTipoDeCliente(String tipoDeCliente);

}
