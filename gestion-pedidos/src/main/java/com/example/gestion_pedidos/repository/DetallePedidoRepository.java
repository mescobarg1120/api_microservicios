package com.example.gestion_pedidos.repository;

import com.example.gestion_pedidos.entity.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido,Long> {

    // Detalles por pedido
    List<DetallePedido> findByPedidoPedidoIdOrderByDetalleId(Long pedidoId);

    // Detalles por producto
    List<DetallePedido> findByProductoIdOrderByPedidoFechaPedidoDesc(Long productoId);


}
