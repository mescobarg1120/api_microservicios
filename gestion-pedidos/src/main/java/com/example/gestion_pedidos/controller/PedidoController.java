package com.example.gestion_pedidos.controller;
import com.example.gestion_pedidos.entity.*;
import com.example.gestion_pedidos.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
@Slf4j
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@Valid @RequestBody Pedido pedido) {
        log.info("POST /api/v1/pedidos - Creando pedido para cliente: {}",
                pedido.getCliente().getClienteId());
        Pedido pedidoCreado = pedidoService.crearPedido(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoCreado);
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> obtenerTodosLosPedidos() {
        log.info("GET /api/v1/pedidos - Obteniendo todos los pedidos");
        List<Pedido> pedidos = pedidoService.obtenerTodosLosPedidos();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{pedidoId}")
    public ResponseEntity<Pedido> obtenerPedido(@PathVariable Long pedidoId) {
        log.info("GET /api/v1/pedidos/{}", pedidoId);
        Pedido pedido = pedidoService.obtenerPedidoPorId(pedidoId);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/numero/{numeroPedido}")
    public ResponseEntity<Pedido> obtenerPedidoPorNumero(@PathVariable String numeroPedido) {
        log.info("GET /api/v1/pedidos/numero/{}", numeroPedido);
        Pedido pedido = pedidoService.obtenerPedidoPorNumero(numeroPedido);
        return ResponseEntity.ok(pedido);
    }

    @PutMapping("/{pedidoId}")
    public ResponseEntity<Pedido> actualizarPedido(
            @PathVariable Long pedidoId,
            @Valid @RequestBody Pedido pedidoActualizado) {

        log.info("PUT /api/v1/pedidos/{}", pedidoId);
        Pedido pedido = pedidoService.actualizarPedido(pedidoId, pedidoActualizado);
        return ResponseEntity.ok(pedido);
    }


    // Gestión de productos en el pedido
    @PostMapping("/{pedidoId}/productos")
    public ResponseEntity<Pedido> agregarProducto(
            @PathVariable Long pedidoId,
            @RequestBody Map<String, Object> productoData) {

        log.info("POST /api/v1/pedidos/{}/productos", pedidoId);

        Long productoId = Long.valueOf(productoData.get("productoId").toString());
        Integer cantidad = Integer.valueOf(productoData.get("cantidad").toString());
        BigDecimal precioUnitario = new BigDecimal(productoData.get("precioUnitario").toString());

        Pedido pedidoActualizado = pedidoService.agregarProductoAlPedido(
                pedidoId, productoId, cantidad, precioUnitario);

        return ResponseEntity.ok(pedidoActualizado);
    }

    @DeleteMapping("/{pedidoId}")
    public ResponseEntity<Map<String, String>> eliminarPedido(@PathVariable Long pedidoId) {
        log.info("DELETE /api/v1/pedidos/{}", pedidoId);
        pedidoService.eliminarPedido(pedidoId);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Pedido eliminado exitosamente");
        return ResponseEntity.ok(response);
    }
}
