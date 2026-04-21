package com.example.gestion_productos.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "proveedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proveedor_id")
    private Long proveedorId;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200, message = "El nombre no debe exceder los 200 caracteres")
    @Column(name = "nombre", length = 200, nullable = false)
    private String nombre;

    @NotBlank(message = "El RUT es obligatorio")
    @Size(max = 20, message = "El RUT no debe exceder los 20 caracteres")
    @Column(name = "rut", length = 20, nullable = false, unique = true)
    private String rut;

    @Size(max = 200, message = "La dirección no debe exceder los 200 caracteres")
    @Column(name = "direccion", length = 200)
    private String direccion;

    @Size(max = 100, message = "La ciudad no debe exceder los 100 caracteres")
    @Column(name = "ciudad", length = 100)
    private String ciudad;

    @Size(max = 20, message = "El teléfono no debe exceder los 20 caracteres")
    @Column(name = "telefono", length = 20)
    private String telefono;

    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no debe exceder los 100 caracteres")
    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Size(max = 100, message = "El sitio web no debe exceder los 100 caracteres")
    @Column(name = "sitio_web", length = 100)
    private String sitioWeb;

    @Size(max = 100, message = "El nombre del contacto no debe exceder los 100 caracteres")
    @Column(name = "contacto_nombre", length = 100)
    private String contactoNombre;

    @Size(max = 100, message = "El cargo del contacto no debe exceder los 100 caracteres")
    @Column(name = "contacto_cargo", length = 100)
    private String contactoCargo;

    @Size(max = 20, message = "El teléfono del contacto no debe exceder los 20 caracteres")
    @Column(name = "contacto_telefono", length = 20)
    private String contactoTelefono;

    @Size(max = 100, message = "Las condiciones de pago no deben exceder los 100 caracteres")
    @Column(name = "condiciones_pago", length = 100)
    private String condicionesPago;

    @Column(name = "plazo_entrega_dias")
    private Integer plazoEntregaDias;

    @Column(name = "es_ecologico", length = 1)
    private String esEcologico;

    @Size(max = 200, message = "Las certificaciones no deben exceder los 200 caracteres")
    @Column(name = "certificaciones", length = 200)
    private String certificaciones;

    @Size(max = 100, message = "El país de origen no debe exceder los 100 caracteres")
    @Column(name = "pais_origen", length = 100)
    private String paisOrigen;

    @Size(max = 100, message = "El tipo de productos no debe exceder los 100 caracteres")
    @Column(name = "tipo_productos", length = 100)
    private String tipoProductos;

    @Column(name = "sustentabilidad_rating", precision = 2, scale = 1)
    private BigDecimal sustentabilidadRating;

    @Size(max = 50, message = "El estado no debe exceder los 50 caracteres")
    @Column(name = "estado", length = 50)
    private String estado;

    @Column(name = "fecha_registro")
    private Timestamp fechaRegistro;

    @Column(name = "notas", columnDefinition = "CLOB")
    private String notas;

    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductoProveedor> productosProveedores = new ArrayList<>();

    // Métodos helper para manejar relaciones bidireccionales
    public void addProductoProveedor(ProductoProveedor productoProveedor) {
        productosProveedores.add(productoProveedor);
        productoProveedor.setProveedor(this);
    }

    public void removeProductoProveedor(ProductoProveedor productoProveedor) {
        productosProveedores.remove(productoProveedor);
        productoProveedor.setProveedor(null);
    }
}
