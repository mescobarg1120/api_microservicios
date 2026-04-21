package com.example.gestion_usuarios.controller;

import com.example.gestion_usuarios.entity.Usuario;
import com.example.gestion_usuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/Usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", "Error al crear usuario: " + e.getMessage());
            e.printStackTrace(); // Para ver el error en los logs del servidor
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<Usuario> obtenerUsuarioPorCorreo(@PathVariable String correo) {
        Usuario usuario = usuarioService.obtenerUsuarioPorCorreo(correo);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id,  @RequestBody Usuario usuario) {
        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
        return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Usuario eliminado correctamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/rol/{rolId}")
    public ResponseEntity<List<Usuario>> obtenerUsuariosPorRol(@PathVariable Long rolId) {
        List<Usuario> usuarios = usuarioService.obtenerUsuariosPorRol(rolId);
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping("/tienda/{tiendaId}")
    public ResponseEntity<List<Usuario>> obtenerUsuariosPorTienda(@PathVariable Long tiendaId) {
        List<Usuario> usuarios = usuarioService.obtenerUsuariosPorTienda(tiendaId);
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }



    @PostMapping("/{id}/cambiar-contraseña")
    public ResponseEntity<Map<String, String>> cambiarContraseña(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String contraseñaActual = request.get("contraseñaActual");
        String nuevaContraseña = request.get("nuevaContraseña");

        boolean resultado = usuarioService.cambiarContraseña(id, contraseñaActual, nuevaContraseña);
        Map<String, String> response = new HashMap<>();

        if (resultado) {
            response.put("mensaje", "Contraseña actualizada con éxito");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("mensaje", "La contraseña actual es incorrecta");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
