package com.example.gestion_productos.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "producto_seq")
    @SequenceGenerator(name = "producto_seq", sequenceName = "PRODUCTO_SEQ", allocationSize = 1)
    @Column(name = "producto_id")
    private Long productoId;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 100, message = "El nombre no debe exceder los 100 caracteres")
    @Column(name = "nombre_producto", length = 100, nullable = false)
    private String nombreProducto;

    @Size(max = 50, message = "El código SKU no debe exceder los 50 caracteres")
    @Column(name = "codigosku", length = 50, unique = true)
    private String codigoSku;

    @NotNull(message = "El precio unitario es obligatorio")
    @Positive(message = "El precio unitario debe ser mayor que cero")
    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    @Column(name = "precio_compra", precision = 10, scale = 2)
    private BigDecimal precioCompra;

    @Column(name = "margen_ganancia", precision = 5, scale = 2)
    private BigDecimal margenGanancia;

    @Column(name = "descripcion", columnDefinition = "CLOB")
    private String descripcion;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Column(name = "es_ecologico", length = 1)
    private String esEcologico;

    @Column(name = "certificacion_eco", length = 100)
    private String certificacionEco;

    @Column(name = "material", length = 100)
    private String material;

    @Column(name = "origen", length = 100)
    private String origen;

    @Column(name = "huella_carbono", length = 50)
    private String huellaCarbono;

    @Column(name = "reciclable", length = 1)
    private String reciclable;

    @Column(name = "biodegradable", length = 1)
    private String biodegradable;

    @Column(name = "fecha_registro")
    private Timestamp fechaRegistro;

    @Column(name = "estado", length = 50)
    private String estado;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @Column(name = "codigo_proveedor", length = 50)
    private String codigoProveedor;

    @Column(name = "ultima_compra")
    private Date ultimaCompra;

    @JsonIgnore
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductoProveedor> proveedores = new ArrayList<>();

    // Métodos para trabajar con ProveedorId
    @JsonProperty("ProveedorId")
    public Long getProveedorId() {
        return proveedor != null ? proveedor.getProveedorId() : null;
    }

    @JsonProperty("ProveedorId")
    public void setProveedorId(Long proveedorId) {
        this.proveedor = null; // Limpiar la referencia actual
        if (proveedorId != null) {
            Proveedor newProveedor = new Proveedor();
            newProveedor.setProveedorId(proveedorId);
            this.proveedor = newProveedor;
        }
    }
    // Métodos para trabajar con ProveedorId
    @JsonProperty("CategoriaId")
    public Long getCategoriaId() {
        return categoria != null ? categoria.getCategoriaId() : null;
    }

    @JsonProperty("CategoriaId")
    public void setCategoriaId(Long categoriaId) {
        this.categoria = null; // Limpiar la referencia actual
        if (categoriaId != null) {
            Categoria newCategoria = new Categoria();
            newCategoria.setCategoriaId(categoriaId);
            this.categoria = newCategoria;
        }
    }


    // Métodos helper para manejar relaciones bidireccionales
    public void addProductoProveedor(ProductoProveedor productoProveedor) {
        proveedores.add(productoProveedor);
        productoProveedor.setProducto(this);
    }

    public void removeProductoProveedor(ProductoProveedor productoProveedor) {
        // Cambiar productosProveedores por proveedores
        proveedores.remove(productoProveedor);
        productoProveedor.setProducto(null);
    }

    // Método para calcular el margen de ganancia
    @PrePersist
    @PreUpdate
    public void calcularMargenGanancia() {
        if (precioUnitario != null && precioCompra != null && precioCompra.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal ganancia = precioUnitario.subtract(precioCompra);
            margenGanancia = ganancia.multiply(new BigDecimal("100"))
                    .divide(precioCompra, 2, BigDecimal.ROUND_HALF_UP);
        }
    }
}
