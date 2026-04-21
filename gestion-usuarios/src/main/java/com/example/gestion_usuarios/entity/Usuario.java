package com.example.gestion_usuarios.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "USUARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Usuario {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "nombre_usuario", nullable = false, length = 100)
    private String nombreUsuario;

    @Column(length = 100)
    private String apellido;

    @Column(name = "correo_usuario", nullable = false, unique = true, length = 100)
    private String correoUsuario;

    @Column(name = "contraseña", nullable = false, length = 255)
    private String contraseña;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id")
    private Rol rol;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tienda_id")
    private Tienda tienda;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "estado")
    private String estado;

    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

    // Métodos para trabajar con rolId
    @JsonProperty("rolId")
    public Long getRolId() {
        return rol != null ? rol.getRolId() : null;
    }

    @JsonProperty("rolId")
    public void setRolId(Long rolId) {
        this.rol = null; // Limpiar la referencia actual
        if (rolId != null) {
            Rol newRol = new Rol();
            newRol.setRolId(rolId);
            this.rol = newRol;
        }
    }

    // Métodos para trabajar con tiendaId
    @JsonProperty("tiendaId")
    public Long getTiendaId() {
        return tienda != null ? tienda.getTiendaId() : null;
    }

    @JsonProperty("tiendaId")
    public void setTiendaId(Long tiendaId) {
        this.tienda = null; // Limpiar la referencia actual
        if (tiendaId != null) {
            Tienda newTienda = new Tienda();
            newTienda.setTiendaId(tiendaId);
            this.tienda = newTienda;
        }
    }

}
