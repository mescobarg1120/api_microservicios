package com.example.gestion_pedidos.repository;

import com.example.gestion_pedidos.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido,Long> {

    // Buscar por número de pedido
    Optional<Pedido> findByNumeroPedido(String numeroPedido);

    // Pedidos por cliente
    List<Pedido> findByClienteClienteIdOrderByFechaPedidoDesc(Long clienteId);


}
