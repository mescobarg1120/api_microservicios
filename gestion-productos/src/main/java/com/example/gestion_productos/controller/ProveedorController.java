package com.example.gestion_productos.controller;
import com.example.gestion_productos.entity.Proveedor;
import com.example.gestion_productos.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "*")
public class ProveedorController {

    private final ProveedorService proveedorService;

    @Autowired
    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    // Obtener todos los proveedores (paginados)
    @GetMapping
    public ResponseEntity<Page<Proveedor>> obtenerProveedoresPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<Proveedor> proveedores = proveedorService.buscarTodosPaginados(pageable);
        return new ResponseEntity<>(proveedores, HttpStatus.OK);
    }

    // Obtener todos los proveedores (sin paginar)
    @GetMapping("/todos")
    public ResponseEntity<List<Proveedor>> obtenerTodosProveedores() {
        List<Proveedor> proveedores = proveedorService.buscarTodos();
        return new ResponseEntity<>(proveedores, HttpStatus.OK);
    }

    // Obtener proveedor por ID
    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> obtenerProveedorPorId(@PathVariable Long id) {
        return proveedorService.buscarPorId(id)
                .map(proveedor -> new ResponseEntity<>(proveedor, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Obtener proveedor por RUT
    @GetMapping("/rut/{rut}")
    public ResponseEntity<Proveedor> obtenerProveedorPorRut(@PathVariable String rut) {
        return proveedorService.buscarPorRut(rut)
                .map(proveedor -> new ResponseEntity<>(proveedor, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Buscar proveedores por nombre
    @GetMapping("/buscar")
    public ResponseEntity<List<Proveedor>> buscarPorNombre(@RequestParam String nombre) {
        List<Proveedor> proveedores = proveedorService.buscarPorNombre(nombre);
        return new ResponseEntity<>(proveedores, HttpStatus.OK);
    }

    // Buscar proveedores por ciudad
    @GetMapping("/ciudad/{ciudad}")
    public ResponseEntity<List<Proveedor>> buscarPorCiudad(@PathVariable String ciudad) {
        List<Proveedor> proveedores = proveedorService.buscarPorCiudad(ciudad);
        return new ResponseEntity<>(proveedores, HttpStatus.OK);
    }

    // Buscar proveedores ecológicos
    @GetMapping("/ecologicos")
    public ResponseEntity<List<Proveedor>> buscarProveedoresEcologicos() {
        List<Proveedor> proveedores = proveedorService.buscarProveedoresEcologicos();
        return new ResponseEntity<>(proveedores, HttpStatus.OK);
    }

    // Buscar proveedores por tipo de producto
    @GetMapping("/tipo-producto")
    public ResponseEntity<List<Proveedor>> buscarPorTipoProducto(@RequestParam String tipo) {
        List<Proveedor> proveedores = proveedorService.buscarPorTipoProducto(tipo);
        return new ResponseEntity<>(proveedores, HttpStatus.OK);
    }

    // Buscar proveedores por país de origen
    @GetMapping("/pais/{pais}")
    public ResponseEntity<List<Proveedor>> buscarPorPaisOrigen(@PathVariable String pais) {
        List<Proveedor> proveedores = proveedorService.buscarPorPaisOrigen(pais);
        return new ResponseEntity<>(proveedores, HttpStatus.OK);
    }

    // Crear un nuevo proveedor
    @PostMapping
    public ResponseEntity<Proveedor> crearProveedor(@Valid @RequestBody Proveedor proveedor) {
        Proveedor nuevoProveedor = proveedorService.guardar(proveedor);
        return new ResponseEntity<>(nuevoProveedor, HttpStatus.CREATED);
    }

    // Actualizar un proveedor existente
    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizarProveedor(@PathVariable Long id, @Valid @RequestBody Proveedor proveedor) {
        return proveedorService.buscarPorId(id)
                .map(proveedorExistente -> {
                    proveedor.setProveedorId(id);
                    Proveedor proveedorActualizado = proveedorService.actualizar(proveedor);
                    return new ResponseEntity<>(proveedorActualizado, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Eliminar un proveedor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProveedor(@PathVariable Long id) {
        return proveedorService.buscarPorId(id)
                .map(proveedor -> {
                    // Verificar si tiene productos asociados
                    if (proveedorService.tieneProductosAsociados(id)) {
                        return new ResponseEntity<Void>(HttpStatus.CONFLICT);
                    }
                    proveedorService.eliminar(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
