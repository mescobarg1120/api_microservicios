package com.example.gestion_pedidos.controller;
import com.example.gestion_pedidos.entity.*;
import com.example.gestion_pedidos.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/v1/detalles-pedido")
@RequiredArgsConstructor
@Slf4j
public class DetallePedidoContoller {

    private final DetallePedidoService detallePedidoService;
    /*
    @PostMapping
    public ResponseEntity<DetallePedido> agregarDetalle(@Valid @RequestBody DetallePedido detalle) {
        log.info("POST /api/v1/detalles-pedido - Pedido: {}, Producto: {}",
                detalle.getPedido().getPedidoId(), detalle.getProductoId());
        DetallePedido detalleCreado = detallePedidoService.agregarDetalle(detalle);
        return ResponseEntity.status(HttpStatus.CREATED).body(detalleCreado);
    }
    */
    @GetMapping("/{detalleId}")
    public ResponseEntity<DetallePedido> obtenerDetalle(@PathVariable Long detalleId) {
        log.info("GET /api/v1/detalles-pedido/{}", detalleId);
        DetallePedido detalle = detallePedidoService.obtenerDetallePorId(detalleId);
        return ResponseEntity.ok(detalle);
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<DetallePedido>> listarDetallesPorPedido(@PathVariable Long pedidoId) {
        log.info("GET /api/v1/detalles-pedido/pedido/{}", pedidoId);
        List<DetallePedido> detalles = detallePedidoService.listarDetallesPorPedido(pedidoId);
        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<DetallePedido>> listarDetallesPorProducto(@PathVariable Long productoId) {
        log.info("GET /api/v1/detalles-pedido/producto/{}", productoId);
        List<DetallePedido> detalles = detallePedidoService.listarDetallesPorProducto(productoId);
        return ResponseEntity.ok(detalles);
    }

    @PutMapping("/{detalleId}")
    public ResponseEntity<DetallePedido> actualizarDetalle(
            @PathVariable Long detalleId,
            @Valid @RequestBody DetallePedido detalle) {

        log.info("PUT /api/v1/detalles-pedido/{}", detalleId);
        DetallePedido detalleActualizado = detallePedidoService.actualizarDetalle(detalleId, detalle);
        return ResponseEntity.ok(detalleActualizado);
    }

    @PutMapping("/{detalleId}/cantidad")
    public ResponseEntity<DetallePedido> actualizarCantidad(
            @PathVariable Long detalleId,
            @RequestBody Map<String, Integer> cantidadData) {

        log.info("PUT /api/v1/detalles-pedido/{}/cantidad", detalleId);
        Integer nuevaCantidad = cantidadData.get("cantidad");
        DetallePedido detalleActualizado = detallePedidoService.actualizarCantidad(detalleId, nuevaCantidad);
        return ResponseEntity.ok(detalleActualizado);
    }

    @PutMapping("/{detalleId}/descuento")
    public ResponseEntity<DetallePedido> aplicarDescuento(
            @PathVariable Long detalleId,
            @RequestBody Map<String, BigDecimal> descuentoData) {

        log.info("PUT /api/v1/detalles-pedido/{}/descuento", detalleId);
        BigDecimal descuento = descuentoData.get("descuento");
        DetallePedido detalleActualizado = detallePedidoService.aplicarDescuento(detalleId, descuento);
        return ResponseEntity.ok(detalleActualizado);
    }

    @DeleteMapping("/{detalleId}")
    public ResponseEntity<Map<String, String>> eliminarDetalle(@PathVariable Long detalleId) {
        log.info("DELETE /api/v1/detalles-pedido/{}", detalleId);
        detallePedidoService.eliminarDetalle(detalleId);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Detalle eliminado exitosamente");
        return ResponseEntity.ok(response);
    }

}
