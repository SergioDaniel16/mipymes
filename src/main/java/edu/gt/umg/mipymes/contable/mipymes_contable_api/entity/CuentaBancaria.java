package edu.gt.umg.mipymes.contable.mipymes_contable_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa una cuenta bancaria de la empresa
 */
@Entity
@Table(name = "cuentas_bancarias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaBancaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "El nombre de la cuenta bancaria es obligatorio")
    private String nombre;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "El banco es obligatorio")
    private String banco;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "El número de cuenta es obligatorio")
    private String numeroCuenta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "El tipo de cuenta es obligatorio")
    private TipoCuentaBancaria tipo;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoLibros = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoBanco = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean activa = true;

    @Column(length = 500)
    private String descripcion;

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
     * Calcular diferencia entre saldos (para conciliación)
     */
    public BigDecimal getDiferenciaConciliacion() {
        return saldoLibros.subtract(saldoBanco);
    }

    /**
     * Verificar si la cuenta está conciliada
     */
    public boolean estaConciliada() {
        return getDiferenciaConciliacion().abs().compareTo(new BigDecimal("0.01")) < 0;
    }

    /**
     * Tipos de cuenta bancaria
     */
    public enum TipoCuentaBancaria {
        CUENTA_CORRIENTE("Cuenta Corriente"),
        CUENTA_AHORRO("Cuenta de Ahorros"),
        CUENTA_DEPOSITO("Cuenta de Depósito a Plazo");

        private final String descripcion;

        TipoCuentaBancaria(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }
}