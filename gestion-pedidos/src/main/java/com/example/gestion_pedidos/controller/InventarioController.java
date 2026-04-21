package com.example.gestion_pedidos.controller;
import com.example.gestion_pedidos.entity.*;
import com.example.gestion_pedidos.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/v1/inventario")
@RequiredArgsConstructor
@Slf4j
public class InventarioController {

    private final InventarioService inventarioService;

    @PostMapping
    public ResponseEntity<Inventario> crearInventario(@Valid @RequestBody Inventario inventario) {
        log.info("POST /api/v1/inventario - Producto: {}, Tienda: {}",
                inventario.getProductoId(), inventario.getTiendaId());
        Inventario inventarioCreado = inventarioService.crearInventario(inventario);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioCreado);
    }

    @GetMapping("/producto/{productoId}/tienda/{tiendaId}")
    public ResponseEntity<Inventario> obtenerInventario(
            @PathVariable Long productoId,
            @PathVariable Long tiendaId) {

        log.info("GET /api/v1/inventario/producto/{}/tienda/{}", productoId, tiendaId);
        Inventario inventario = inventarioService.obtenerInventario(productoId, tiendaId);
        return ResponseEntity.ok(inventario);
    }

    @GetMapping("/tienda/{tiendaId}")
    public ResponseEntity<List<Inventario>> obtenerInventarioPorTienda(@PathVariable Long tiendaId) {
        log.info("GET /api/v1/inventario/tienda/{}", tiendaId);
        List<Inventario> inventarios = inventarioService.obtenerInventarioPorTienda(tiendaId);
        return ResponseEntity.ok(inventarios);
    }



    @GetMapping("/verificar-disponibilidad")
    public ResponseEntity<Map<String, Boolean>> verificarDisponibilidad(
            @RequestParam Long productoId,
            @RequestParam Long tiendaId,
            @RequestParam Integer cantidad) {

        log.info("GET /api/v1/inventario/verificar-disponibilidad?productoId={}&tiendaId={}&cantidad={}",
                productoId, tiendaId, cantidad);

        boolean disponible = inventarioService.verificarDisponibilidad(productoId, tiendaId, cantidad);

        Map<String, Boolean> response = new HashMap<>();
        response.put("disponible", disponible);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{inventarioId}")
    public ResponseEntity<Inventario> actualizarInventario(
            @PathVariable Long inventarioId,
            @Valid @RequestBody Inventario inventario) {

        log.info("PUT /api/v1/inventario/{}", inventarioId);
        Inventario inventarioActualizado = inventarioService.actualizarInventario(inventarioId, inventario);
        return ResponseEntity.ok(inventarioActualizado);
    }


}
