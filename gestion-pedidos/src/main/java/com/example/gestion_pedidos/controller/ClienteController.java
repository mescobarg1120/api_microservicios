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
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@Valid @RequestBody Cliente cliente) {
        log.info("POST /api/v1/clientes - Creando cliente: {}", cliente.getEmail());
        Cliente clienteCreado = clienteService.crearCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteCreado);
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<Cliente> obtenerCliente(@PathVariable Long clienteId) {
        log.info("GET /api/v1/clientes/{}", clienteId);
        Cliente cliente = clienteService.obtenerClientePorId(clienteId);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Cliente> obtenerClientePorEmail(@PathVariable String email) {
        log.info("GET /api/v1/clientes/email/{}", email);
        Cliente cliente = clienteService.obtenerClientePorEmail(email);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes(
            @RequestParam(defaultValue = "ACTIVO") String estado) {
        log.info("GET /api/v1/clientes?estado={}", estado);
        List<Cliente> clientes = "ACTIVO".equals(estado) ?
                clienteService.listarClientesActivos() :
                clienteService.listarClientesActivos(); // Ajustar según necesidad
        return ResponseEntity.ok(clientes);
    }
    /*
    @GetMapping("/buscar")
    public ResponseEntity<List<Cliente>> buscarClientes(
            @RequestParam String busqueda) {
        log.info("GET /api/v1/clientes/buscar?busqueda={}", busqueda);
        List<Cliente> clientes = clienteService.buscarClientesPorNombre(busqueda);
        return ResponseEntity.ok(clientes);
    }
    */
    @PutMapping("/{clienteId}")
    public ResponseEntity<Cliente> actualizarCliente(
            @PathVariable Long clienteId,
            @Valid @RequestBody Cliente cliente) {
        log.info("PUT /api/v1/clientes/{}", clienteId);
        Cliente clienteActualizado = clienteService.actualizarCliente(clienteId, cliente);
        return ResponseEntity.ok(clienteActualizado);
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Map<String, String>> eliminarCliente(@PathVariable Long clienteId) {
        log.info("DELETE /api/v1/clientes/{}", clienteId);
        clienteService.eliminarCliente(clienteId);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Cliente eliminado/desactivado exitosamente");
        return ResponseEntity.ok(response);
    }
}
