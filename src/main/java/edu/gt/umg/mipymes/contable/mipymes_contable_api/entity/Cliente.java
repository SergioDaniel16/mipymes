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
 * Entidad que representa un cliente de "El Almacén El Planeador"
 */
@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    @NotBlank(message = "El código del cliente es obligatorio")
    private String codigo;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String nombre;

    @Column(length = 200)
    private String razonSocial;

    @Column(length = 20)
    private String nit;

    @Column(length = 20)
    private String dpi;

    @Column(length = 15)
    private String telefono;

    @Column(length = 100)
    @Email(message = "El formato del email no es válido")
    private String email;

    @Column(length = 500)
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCliente tipoCliente = TipoCliente.CONSUMIDOR_FINAL;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal limiteCredito = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoActual = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer diasCredito = 0;

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
     * Calcular saldo disponible de crédito
     */
    public BigDecimal getSaldoDisponible() {
        return limiteCredito.subtract(saldoActual);
    }

    /**
     * Verificar si tiene crédito disponible
     */
    public boolean tieneCreditoDisponible() {
        return getSaldoDisponible().compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Verificar si excede el límite de crédito
     */
    public boolean excedeCredito(BigDecimal monto) {
        return saldoActual.add(monto).compareTo(limiteCredito) > 0;
    }

    /**
     * Tipos de cliente
     */
    public enum TipoCliente {
        CONSUMIDOR_FINAL("Consumidor Final"),
        EMPRESA("Empresa"),
        GOBIERNO("Gobierno"),
        ONG("ONG");

        private final String descripcion;

        TipoCliente(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }
}