package com.example.gestion_usuarios.service;
import com.example.gestion_usuarios.entity.Rol;
import com.example.gestion_usuarios.entity.Tienda;
import com.example.gestion_usuarios.entity.Usuario;
import com.example.gestion_usuarios.repository.RolRepository;
import com.example.gestion_usuarios.repository.TiendaRepository;
import com.example.gestion_usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class UsuarioService {


    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final TiendaRepository tiendaRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          TiendaRepository tiendaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.tiendaRepository = tiendaRepository;
    }

    @Transactional
    public Usuario crearUsuario(Usuario usuario) {
        // Verificar si el correo ya existe directamente usando el repositorio
        if (usuarioRepository.existsByCorreoUsuario(usuario.getCorreoUsuario())) {
            throw new RuntimeException("El correo " + usuario.getCorreoUsuario() + " ya está registrado");
        }

        if (usuario.getContraseña() == null || usuario.getContraseña().length() < 8) {
            throw new RuntimeException("La contraseña debe tener al menos 8 caracteres");
        }


        // Resolver la relación con Rol
        if (usuario.getRolId() != null) {
            Rol rol = rolRepository.findById(usuario.getRolId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + usuario.getRolId()));
            usuario.setRol(rol);
        } else {
            throw new RuntimeException("El rol es obligatorio");
        }

        // Resolver la relación con Tienda
        if (usuario.getTiendaId() != null) {
            Tienda tienda = tiendaRepository.findById(usuario.getTiendaId())
                    .orElseThrow(() -> new RuntimeException("Tienda no encontrada con ID: " + usuario.getTiendaId()));
            usuario.setTienda(tienda);
        } else {
            throw new RuntimeException("La tienda es obligatoria");
        }

        // Establecer valores por defecto
        usuario.setEstado("ACTIVO");
        usuario.setFechaRegistro(LocalDateTime.now());

        // Guardar y devolver el usuario
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public Usuario obtenerUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreoUsuario(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con correo: " + correo));
    }

    @Transactional
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        // Verificar si el usuario existe
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // Actualizar campos básicos
        usuario.setNombreUsuario(usuarioActualizado.getNombreUsuario());
        usuario.setApellido(usuarioActualizado.getApellido());

        // Verificar si el correo nuevo ya existe y es diferente al actual
        if (!usuario.getCorreoUsuario().equals(usuarioActualizado.getCorreoUsuario()) &&
                usuarioRepository.existsByCorreoUsuario(usuarioActualizado.getCorreoUsuario())) {
            throw new RuntimeException("El correo " + usuarioActualizado.getCorreoUsuario() + " ya está registrado");
        }

        usuario.setCorreoUsuario(usuarioActualizado.getCorreoUsuario());


        if (usuarioActualizado.getContraseña() != null && !usuarioActualizado.getContraseña().isEmpty()) {
            usuario.setContraseña(usuarioActualizado.getContraseña());
        }

        // Actualizar rol si se proporciona
        if (usuarioActualizado.getRolId() != null) {
            Rol rol = rolRepository.findById(usuarioActualizado.getRolId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + usuarioActualizado.getRolId()));
            usuario.setRol(rol);
        }

        // Actualizar tienda si se proporciona
        if (usuarioActualizado.getTiendaId() != null) {
            Tienda tienda = tiendaRepository.findById(usuarioActualizado.getTiendaId())
                    .orElseThrow(() -> new RuntimeException("Tienda no encontrada con ID: " + usuarioActualizado.getTiendaId()));
            usuario.setTienda(tienda);
        }

        // Actualizar estado si se proporciona
        if (usuarioActualizado.getEstado() != null) {
            usuario.setEstado(usuarioActualizado.getEstado());
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }

        // Eliminación lógica (cambiar estado) en lugar de eliminación física
        Usuario usuario = usuarioRepository.findById(id).get();
        usuario.setEstado("INACTIVO");
        usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuariosPorRol(Long rolId) {
        if (!rolRepository.existsById(rolId)) {
            throw new RuntimeException("Rol no encontrado con ID: " + rolId);
        }
        return usuarioRepository.findByRolRolId(rolId);
    }

    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuariosPorTienda(Long tiendaId) {
        if (!tiendaRepository.existsById(tiendaId)) {
            throw new RuntimeException("Tienda no encontrada con ID: " + tiendaId);
        }
        return usuarioRepository.findByTiendaTiendaId(tiendaId);
    }

    @Transactional
    public Usuario cambiarEstadoUsuario(Long id, String nuevoEstado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        usuario.setEstado(nuevoEstado);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public boolean cambiarContraseña(Long id, String contraseñaActual, String nuevaContraseña) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // Verificar contraseña actual
        if (!contraseñaActual.equals(usuario.getContraseña())) {
            return false;
        }

        // Validaciones básicas de la nueva contraseña
        if (nuevaContraseña == null || nuevaContraseña.trim().isEmpty()) {
            throw new RuntimeException("La nueva contraseña no puede estar vacía");
        }

        if (nuevaContraseña.length() < 6) {
            throw new RuntimeException("La nueva contraseña debe tener al menos 6 caracteres");
        }

        // Verificar que la nueva contraseña sea diferente a la actual
        if (contraseñaActual.equals(nuevaContraseña)) {
            throw new RuntimeException("La nueva contraseña debe ser diferente a la actual");
        }

        // Actualizar contraseña
        usuario.setContraseña(nuevaContraseña.trim());
        usuarioRepository.save(usuario);

        return true;
    }

}
