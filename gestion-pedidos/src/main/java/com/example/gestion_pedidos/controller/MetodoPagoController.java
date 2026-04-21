package com.example.gestion_pedidos.controller;
import com.example.gestion_pedidos.entity.*;
import com.example.gestion_pedidos.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/metodos-pago")
@RequiredArgsConstructor
@Slf4j
public class MetodoPagoController {

    private final MetodoPagoService metodoPagoService;

    @PostMapping
    public ResponseEntity<MetodoPago> crearMetodoPago(@Valid @RequestBody MetodoPago metodoPago) {
        log.info("POST /api/v1/metodos-pago - Creando: {}", metodoPago.getNombre());
        MetodoPago metodoCreado = metodoPagoService.crearMetodoPago(metodoPago);
        return ResponseEntity.status(HttpStatus.CREATED).body(metodoCreado);
    }

    @GetMapping
    public ResponseEntity<List<MetodoPago>> listarMetodosActivos() {
        log.info("GET /api/v1/metodos-pago");
        List<MetodoPago> metodos = metodoPagoService.listarMetodosActivos();
        return ResponseEntity.ok(metodos);
    }

    @GetMapping("/{metodoPagoId}")
    public ResponseEntity<MetodoPago> obtenerMetodoPago(@PathVariable Long metodoPagoId) {
        log.info("GET /api/v1/metodos-pago/{}", metodoPagoId);
        MetodoPago metodo = metodoPagoService.obtenerMetodoPorId(metodoPagoId);
        return ResponseEntity.ok(metodo);
    }

    @PutMapping("/{metodoPagoId}/activar")
    public ResponseEntity<MetodoPago> activarMetodo(@PathVariable Long metodoPagoId) {
        log.info("PUT /api/v1/metodos-pago/{}/activar", metodoPagoId);
        MetodoPago metodoActivado = metodoPagoService.activarDesactivarMetodo(metodoPagoId, true);
        return ResponseEntity.ok(metodoActivado);
    }

    @PutMapping("/{metodoPagoId}/desactivar")
    public ResponseEntity<MetodoPago> desactivarMetodo(@PathVariable Long metodoPagoId) {
        log.info("PUT /api/v1/metodos-pago/{}/desactivar", metodoPagoId);
        MetodoPago metodoDesactivado = metodoPagoService.activarDesactivarMetodo(metodoPagoId, false);
        return ResponseEntity.ok(metodoDesactivado);
    }
}

