package com.example.gestion_pedidos.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import com.example.gestion_pedidos.entity.DetallePedido;
import com.example.gestion_pedidos.repository.DetallePedidoRepository;
import com.example.gestion_pedidos.repository.PedidoRepository;
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final InventarioService inventarioService;

    /*
    public DetallePedido agregarDetalle(DetallePedido detalle) {
        log.info("Agregando detalle de producto {} al pedido {}",
                detalle.getProductoId(), detalle.getPedido().getPedidoId());

        // Validar que el pedido existe y puede ser modificado
        Pedido pedido = pedidoRepository.findById(detalle.getPedido().getPedidoId())
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));

        if (!pedido.puedeSerModificado()) {
            throw new BusinessException("El pedido no puede ser modificado en su estado actual");
        }

        // Verificar stock disponible si hay tienda asignada
        if (pedido.getTiendaId() != null) {
            if (!inventarioService.verificarDisponibilidad(
                    detalle.getProductoId(), pedido.getTiendaId(), detalle.getCantidad())) {
                throw new BusinessException("Stock insuficiente para el producto solicitado");
            }
        }

        // Establecer valores por defecto
        if (detalle.getDescuentoUnitario() == null) {
            detalle.getDescuentoUnitario() = BigDecimal.ZERO;
        }
        if (detalle.getEsProductoEcologico() == null) {
            detalle.getEsProductoEcologico() = "S";
        }

        DetallePedido detalleGuardado = detallePedidoRepository.save(detalle);
        log.info("Detalle agregado con ID: {}", detalleGuardado.getDetalleId());

        return detalleGuardado;
    }
    */
    public DetallePedido actualizarDetalle(Long detalleId, DetallePedido detalleActualizado) {
        log.info("Actualizando detalle ID: {}", detalleId);

        DetallePedido detalleExistente = obtenerDetallePorId(detalleId);

        // Verificar que el pedido puede ser modificado
        if (!detalleExistente.getPedido().puedeSerModificado()) {
            throw new BusinessException("El pedido no puede ser modificado en su estado actual");
        }

        // Verificar stock si cambió la cantidad
        if (!detalleExistente.getCantidad().equals(detalleActualizado.getCantidad())) {
            Long tiendaId = detalleExistente.getPedido().getTiendaId();
            if (tiendaId != null) {
                if (!inventarioService.verificarDisponibilidad(
                        detalleExistente.getProductoId(), tiendaId, detalleActualizado.getCantidad())) {
                    throw new BusinessException("Stock insuficiente para la cantidad solicitada");
                }
            }
        }

        // Actualizar campos
        detalleExistente.setCantidad(detalleActualizado.getCantidad());
        detalleExistente.setPrecioUnitario(detalleActualizado.getPrecioUnitario());
        detalleExistente.setDescuentoUnitario(detalleActualizado.getDescuentoUnitario());
        detalleExistente.setCertificacionAplicada(detalleActualizado.getCertificacionAplicada());

        return detallePedidoRepository.save(detalleExistente);
    }

    public void eliminarDetalle(Long detalleId) {
        log.info("Eliminando detalle ID: {}", detalleId);

        DetallePedido detalle = obtenerDetallePorId(detalleId);

        // Verificar que el pedido puede ser modificado
        if (!detalle.getPedido().puedeSerModificado()) {
            throw new BusinessException("El pedido no puede ser modificado en su estado actual");
        }

        detallePedidoRepository.delete(detalle);
        log.info("Detalle eliminado exitosamente");
    }

    public DetallePedido obtenerDetallePorId(Long detalleId) {
        return detallePedidoRepository.findById(detalleId)
                .orElseThrow(() -> new EntityNotFoundException("Detalle de pedido no encontrado con ID: " + detalleId));
    }

    public List<DetallePedido> listarDetallesPorPedido(Long pedidoId) {
        return detallePedidoRepository.findByPedidoPedidoIdOrderByDetalleId(pedidoId);
    }

    public List<DetallePedido> listarDetallesPorProducto(Long productoId) {
        return detallePedidoRepository.findByProductoIdOrderByPedidoFechaPedidoDesc(productoId);
    }

    public DetallePedido actualizarCantidad(Long detalleId, Integer nuevaCantidad) {
        log.info("Actualizando cantidad del detalle {} a {}", detalleId, nuevaCantidad);

        if (nuevaCantidad <= 0) {
            throw new BusinessException("La cantidad debe ser mayor a 0");
        }

        DetallePedido detalle = obtenerDetallePorId(detalleId);

        // Verificar que el pedido puede ser modificado
        if (!detalle.getPedido().puedeSerModificado()) {
            throw new BusinessException("El pedido no puede ser modificado en su estado actual");
        }

        // Verificar stock disponible
        Long tiendaId = detalle.getPedido().getTiendaId();
        if (tiendaId != null) {
            if (!inventarioService.verificarDisponibilidad(
                    detalle.getProductoId(), tiendaId, nuevaCantidad)) {
                throw new BusinessException("Stock insuficiente para la cantidad solicitada");
            }
        }

        detalle.setCantidad(nuevaCantidad);
        return detallePedidoRepository.save(detalle);
    }

    public DetallePedido aplicarDescuento(Long detalleId, BigDecimal descuentoUnitario) {
        log.info("Aplicando descuento de {} al detalle {}", descuentoUnitario, detalleId);

        if (descuentoUnitario.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("El descuento no puede ser negativo");
        }

        DetallePedido detalle = obtenerDetallePorId(detalleId);

        // Verificar que el pedido puede ser modificado
        if (!detalle.getPedido().puedeSerModificado()) {
            throw new BusinessException("El pedido no puede ser modificado en su estado actual");
        }

        // Verificar que el descuento no sea mayor al precio
        if (descuentoUnitario.compareTo(detalle.getPrecioUnitario()) > 0) {
            throw new BusinessException("El descuento no puede ser mayor al precio unitario");
        }

        detalle.setDescuentoUnitario(descuentoUnitario);
        return detallePedidoRepository.save(detalle);
    }
    /*
    public void eliminarTodosLosDetallesPorPedido(Long pedidoId) {
        log.info("Eliminando todos los detalles del pedido {}", pedidoId);

        // Verificar que el pedido existe y puede ser modificado
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));

        if (!pedido.puedeSerModificado()) {
            throw new BusinessException("El pedido no puede ser modificado en su estado actual");
        }

        detallePedidoRepository.deleteByPedidoId(pedidoId);
        log.info("Todos los detalles del pedido eliminados exitosamente");
    }
    */

}
