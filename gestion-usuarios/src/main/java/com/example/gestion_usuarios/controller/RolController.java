package com.example.gestion_usuarios.controller;
import com.example.gestion_usuarios.entity.Rol;
import com.example.gestion_usuarios.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
public class RolController {
    private final RolService rolService;

    @Autowired
    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public ResponseEntity<List<Rol>> obtenerTodosLosRoles() {
        List<Rol> roles = rolService.obtenerTodosLosRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rol> obtenerRolPorId(@PathVariable Long id) {
        Rol rol = rolService.obtenerRolPorId(id);
        return new ResponseEntity<>(rol, HttpStatus.OK);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Rol> obtenerRolPorNombre(@PathVariable String nombre) {
        Rol rol = rolService.obtenerRolPorNombre(nombre);
        return new ResponseEntity<>(rol, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Rol> crearRol( @RequestBody Rol rol) {
        Rol nuevoRol = rolService.crearRol(rol);
        return new ResponseEntity<>(nuevoRol, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rol> actualizarRol(@PathVariable Long id,  @RequestBody Rol rol) {
        Rol rolActualizado = rolService.actualizarRol(id, rol);
        return new ResponseEntity<>(rolActualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarRol(@PathVariable Long id) {
        rolService.eliminarRol(id);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Rol eliminado correctamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
