package com.example.gestion_pedidos.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "CLIENTE", schema = "MICROS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cliente_seq")
    @SequenceGenerator(name = "cliente_seq", sequenceName = "MICROS.CLIENTE_SEQ", allocationSize = 1)
    @Column(name = "CLIENTE_ID")
    private Long clienteId;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    @Column(name = "APELLIDO", nullable = false, length = 100)
    private String apellido;

    @Email(message = "Debe ser un email válido")
    @NotBlank(message = "El email es obligatorio")
    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    private String email;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Column(name = "TELEFONO", length = 20)
    private String telefono;

    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    @Column(name = "DIRECCION", length = 200)
    private String direccion;

    @Size(max = 100, message = "La ciudad no puede exceder 100 caracteres")
    @Column(name = "CIUDAD", length = 100)
    private String ciudad;

    @Size(max = 100, message = "La región no puede exceder 100 caracteres")
    @Column(name = "REGION", length = 100)
    private String region;

    @Column(name = "TIPO_DE_CLIENTE", length = 50)
    private String tipoDeCliente = "REGULAR";

    @Column(name = "PREFIERE_ECOLOGICOS", length = 1)
    private String prefiereEcologicos = "S";

    @DecimalMin(value = "0.0", message = "El total de compras no puede ser negativo")
    @Column(name = "TOTAL_DE_COMPRAS", precision = 12, scale = 2)
    private BigDecimal totalDeCompras = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name = "FECHA_REGISTRO")
    private LocalDateTime fechaRegistro;

    @Column(name = "ESTADO", length = 50)
    private String estado = "ACTIVO";

    // Relación con Pedidos
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pedido> pedidos;

    // Métodos de conveniencia
    public boolean prefiereProductosEcologicos() {
        return "S".equals(this.prefiereEcologicos);
    }

    public boolean isActivo() {
        return "ACTIVO".equals(this.estado);
    }
}
