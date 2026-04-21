package com.example.gestion_pedidos.service;

import com.example.gestion_pedidos.entity.Cliente;
import com.example.gestion_pedidos.entity.DetallePedido;
import com.example.gestion_pedidos.entity.MetodoPago;
import com.example.gestion_pedidos.entity.Pedido;
import com.example.gestion_pedidos.repository.ClienteRepository;
import com.example.gestion_pedidos.repository.DetallePedidoRepository;
import com.example.gestion_pedidos.repository.MetodoPagoRepository;
import com.example.gestion_pedidos.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final ClienteRepository clienteRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final InventarioService inventarioService;

    public Pedido crearPedido(Pedido pedido) {
        log.info("Creando nuevo pedido para cliente {}", pedido.getCliente().getClienteId());

        // Validar cliente existe
        Cliente cliente = clienteRepository.findById(pedido.getCliente().getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

        // Validar método de pago existe y está activo
        MetodoPago metodoPago = metodoPagoRepository.findById(pedido.getMetodoPago().getMetodoPagoId())
                .orElseThrow(() -> new EntityNotFoundException("Método de pago no encontrado"));

        if (!"S".equals(metodoPago.getActivo())) {
            throw new BusinessException("El método de pago no está activo");
        }

        // Establecer valores por defecto
        pedido.setCliente(cliente);
        pedido.setMetodoPago(metodoPago);
        pedido.setEstado(DetallePedido.EstadoPedido.PENDIENTE);
        pedido.setFechaPedido(LocalDate.now());

        // Calcular totales si hay detalles
        if (pedido.getDetalles() != null && !pedido.getDetalles().isEmpty()) {
            calcularTotalesPedido(pedido);
        }

        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        log.info("Pedido creado con número: {}", pedidoGuardado.getNumeroPedido());

        return pedidoGuardado;
    }

    public Pedido agregarProductoAlPedido(Long pedidoId, Long productoId, Integer cantidad, BigDecimal precioUnitario) {
        log.info("Agregando producto {} al pedido {}", productoId, pedidoId);

        Pedido pedido = obtenerPedidoPorId(pedidoId);

        // Verificar que el pedido pueda ser modificado
        if (!pedido.puedeSerModificado()) {
            throw new BusinessException("El pedido no puede ser modificado en su estado actual: " + pedido.getEstado());
        }

        // Verificar disponibilidad de stock si hay tienda asignada
        if (pedido.getTiendaId() != null) {
            if (!inventarioService.verificarDisponibilidad(productoId, pedido.getTiendaId(), cantidad)) {
                throw new BusinessException("Stock insuficiente para el producto solicitado");
            }
        }

        // Buscar si ya existe el producto en el pedido
        Optional<DetallePedido> detalleExistente = pedido.getDetalles().stream()
                .filter(d -> d.getProductoId().equals(productoId))
                .findFirst();

        if (detalleExistente.isPresent()) {
            // Actualizar cantidad existente
            DetallePedido detalle = detalleExistente.get();
            detalle.setCantidad(detalle.getCantidad() + cantidad);
            detallePedidoRepository.save(detalle);
        } else {
            // Crear nuevo detalle
            DetallePedido nuevoDetalle = new DetallePedido();
            nuevoDetalle.setPedido(pedido);
            nuevoDetalle.setProductoId(productoId);
            nuevoDetalle.setCantidad(cantidad);
            nuevoDetalle.setPrecioUnitario(precioUnitario);
            nuevoDetalle.setEsProductoEcologico("S"); // Por defecto, ajustar según la lógica de negocio

            detallePedidoRepository.save(nuevoDetalle);
            pedido.getDetalles().add(nuevoDetalle);
        }

        // Recalcular totales
        calcularTotalesPedido(pedido);

        return pedidoRepository.save(pedido);
    }

    public Pedido eliminarProductoDelPedido(Long pedidoId, Long productoId) {
        log.info("Eliminando producto {} del pedido {}", productoId, pedidoId);

        Pedido pedido = obtenerPedidoPorId(pedidoId);

        if (!pedido.puedeSerModificado()) {
            throw new BusinessException("El pedido no puede ser modificado en su estado actual");
        }

        // Buscar y eliminar el detalle
        DetallePedido detalleAEliminar = pedido.getDetalles().stream()
                .filter(d -> d.getProductoId().equals(productoId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado en el pedido"));

        detallePedidoRepository.delete(detalleAEliminar);
        pedido.getDetalles().remove(detalleAEliminar);

        // Recalcular totales
        calcularTotalesPedido(pedido);

        return pedidoRepository.save(pedido);
    }

    public Pedido actualizarCantidadProducto(Long pedidoId, Long productoId, Integer nuevaCantidad) {
        log.info("Actualizando cantidad del producto {} en pedido {} a {}", productoId, pedidoId, nuevaCantidad);

        Pedido pedido = obtenerPedidoPorId(pedidoId);

        if (!pedido.puedeSerModificado()) {
            throw new BusinessException("El pedido no puede ser modificado en su estado actual");
        }

        if (nuevaCantidad <= 0) {
            return eliminarProductoDelPedido(pedidoId, productoId);
        }

        // Verificar stock si hay tienda asignada
        if (pedido.getTiendaId() != null) {
            if (!inventarioService.verificarDisponibilidad(productoId, pedido.getTiendaId(), nuevaCantidad)) {
                throw new BusinessException("Stock insuficiente para la cantidad solicitada");
            }
        }

        // Actualizar cantidad
        DetallePedido detalle = pedido.getDetalles().stream()
                .filter(d -> d.getProductoId().equals(productoId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado en el pedido"));

        detalle.setCantidad(nuevaCantidad);
        detallePedidoRepository.save(detalle);

        // Recalcular totales
        calcularTotalesPedido(pedido);

        return pedidoRepository.save(pedido);
    }



    public Pedido obtenerPedidoPorId(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con ID: " + pedidoId));
    }

    public Pedido obtenerPedidoPorNumero(String numeroPedido) {
        return pedidoRepository.findByNumeroPedido(numeroPedido)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con número: " + numeroPedido));
    }


    public List<Pedido> obtenerTodosLosPedidos() {
        log.info("Obteniendo todos los pedidos del sistema");
        return pedidoRepository.findAll();
    }


    private void calcularTotalesPedido(Pedido pedido) {
        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            pedido.setSubtotal(BigDecimal.ZERO);
            pedido.setTotal(BigDecimal.ZERO);
            return;
        }

        BigDecimal subtotal = pedido.getDetalles().stream()
                .map(detalle -> detalle.getPrecioUnitario()
                        .subtract(detalle.getDescuentoUnitario())
                        .multiply(BigDecimal.valueOf(detalle.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setSubtotal(subtotal);

        // Calcular total (subtotal - descuento + impuestos)
        BigDecimal total = subtotal
                .subtract(pedido.getDescuento() != null ? pedido.getDescuento() : BigDecimal.ZERO)
                .add(pedido.getImpuestos() != null ? pedido.getImpuestos() : BigDecimal.ZERO);

        pedido.setTotal(total);

        // Determinar si es pedido ecológico
        boolean todosEcologicos = pedido.getDetalles().stream()
                .allMatch(detalle -> "S".equals(detalle.getEsProductoEcologico()));

        pedido.setEsPedidoEcologico(todosEcologicos ? "S" : "N");
    }


    public Pedido actualizarPedido(Long pedidoId, Pedido pedidoActualizado) {
        log.info("Actualizando pedido ID: {}", pedidoId);

        Pedido pedidoExistente = obtenerPedidoPorId(pedidoId);

        // Verificar que el pedido puede ser modificado
        if (!pedidoExistente.puedeSerModificado()) {
            throw new BusinessException("El pedido no puede ser modificado en su estado actual: " + pedidoExistente.getEstado());
        }

        // Validar cliente si cambió
        if (pedidoActualizado.getCliente() != null &&
                !pedidoExistente.getCliente().getClienteId().equals(pedidoActualizado.getCliente().getClienteId())) {

            Cliente nuevoCliente = clienteRepository.findById(pedidoActualizado.getCliente().getClienteId())
                    .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
            pedidoExistente.setCliente(nuevoCliente);
        }

        // Validar método de pago si cambió
        if (pedidoActualizado.getMetodoPago() != null &&
                !pedidoExistente.getMetodoPago().getMetodoPagoId().equals(pedidoActualizado.getMetodoPago().getMetodoPagoId())) {

            MetodoPago nuevoMetodo = metodoPagoRepository.findById(pedidoActualizado.getMetodoPago().getMetodoPagoId())
                    .orElseThrow(() -> new EntityNotFoundException("Método de pago no encontrado"));

            if (!"S".equals(nuevoMetodo.getActivo())) {
                throw new BusinessException("El método de pago no está activo");
            }
            pedidoExistente.setMetodoPago(nuevoMetodo);
        }

        // Actualizar campos modificables
        if (pedidoActualizado.getTiendaId() != null) {
            pedidoExistente.setTiendaId(pedidoActualizado.getTiendaId());
        }

        if (pedidoActualizado.getDireccionEnvio() != null) {
            pedidoExistente.setDireccionEnvio(pedidoActualizado.getDireccionEnvio());
        }

        if (pedidoActualizado.getCiudadEnvio() != null) {
            pedidoExistente.setCiudadEnvio(pedidoActualizado.getCiudadEnvio());
        }

        if (pedidoActualizado.getRegionEnvio() != null) {
            pedidoExistente.setRegionEnvio(pedidoActualizado.getRegionEnvio());
        }

        if (pedidoActualizado.getTipoEntrega() != null) {
            pedidoExistente.setTipoEntrega(pedidoActualizado.getTipoEntrega());
        }

        if (pedidoActualizado.getFechaEntregaEstimada() != null) {
            pedidoExistente.setFechaEntregaEstimada(pedidoActualizado.getFechaEntregaEstimada());
        }

        if (pedidoActualizado.getNotas() != null) {
            pedidoExistente.setNotas(pedidoActualizado.getNotas());
        }

        if (pedidoActualizado.getDescuento() != null) {
            pedidoExistente.setDescuento(pedidoActualizado.getDescuento());
        }

        if (pedidoActualizado.getImpuestos() != null) {
            pedidoExistente.setImpuestos(pedidoActualizado.getImpuestos());
        }

        // Recalcular totales
        calcularTotalesPedido(pedidoExistente);

        return pedidoRepository.save(pedidoExistente);
    }

    public void eliminarPedido(Long pedidoId) {
        log.info("Eliminando pedido ID: {}", pedidoId);

        Pedido pedido = obtenerPedidoPorId(pedidoId);

        // Solo se pueden eliminar pedidos pendientes
        if (!DetallePedido.EstadoPedido.PENDIENTE.equals(pedido.getEstado())) {
            throw new BusinessException("Solo se pueden eliminar pedidos en estado PENDIENTE. Estado actual: " + pedido.getEstado());
        }

        pedidoRepository.delete(pedido);
        log.info("Pedido {} eliminado exitosamente", pedidoId);
    }
}
