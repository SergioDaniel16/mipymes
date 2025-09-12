package edu.gt.umg.mipymes.contable.mipymes_contable_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa un proveedor de "El Almacén El Planeador"
 */
@Entity
@Table(name = "proveedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    @NotBlank(message = "El código del proveedor es obligatorio")
    private String codigo;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "El nombre del proveedor es obligatorio")
    private String nombre;

    @Column(length = 200)
    private String razonSocial;

    @Column(length = 20)
    private String nit;

    @Column(length = 15)
    private String telefono;

    @Column(length = 100)
    @Email(message = "El formato del email no es válido")
    private String email;

    @Column(length = 500)
    private String direccion;

    @Column(length = 100)
    private String contacto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoProveedor tipoProveedor = TipoProveedor.MERCADERIAS;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoActual = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer diasPago = 30;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(length = 500)
    private String observaciones;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private LocalDateTime fechaModificacion;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        fechaCreacion = now;
        fechaModificacion = now;
    }

    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }

    /**
     * Verificar si tiene saldo pendiente
     */
    public boolean tieneSaldoPendiente() {
        return saldoActual.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Tipos de proveedor
     */
    public enum TipoProveedor {
        MERCADERIAS("Mercaderías"),
        SERVICIOS("Servicios"),
        ACTIVOS_FIJOS("Activos Fijos"),
        SUMINISTROS("Suministros");

        private final String descripcion;

        TipoProveedor(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }
}
