package com.example.gestion_pedidos.repository;

import com.example.gestion_pedidos.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface InventarioRepository extends JpaRepository<Inventario,Long> {
    // Buscar por producto y tienda
    Optional<Inventario> findByProductoIdAndTiendaId(Long productoId, Long tiendaId);

    // Inventario por tienda
    List<Inventario> findByTiendaIdOrderByProductoId(Long tiendaId);

    // Inventario por producto (todas las tiendas)
    List<Inventario> findByProductoIdOrderByTiendaId(Long productoId);

    boolean existsByProductoIdAndTiendaId(Long productoId, Long tiendaId);

    @Query("SELECT i FROM Inventario i WHERE i.productoId = :productoId AND i.tiendaId = :tiendaId AND " +
            "(i.cantidadDisponible - i.cantidadReservada) >= :cantidadSolicitada")
    Optional<Inventario> verificarStockDisponible(
            @Param("productoId") Long productoId,
            @Param("tiendaId") Long tiendaId,
            @Param("cantidadSolicitada") Integer cantidadSolicitada
    );
}
