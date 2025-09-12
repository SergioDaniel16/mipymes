package edu.gt.umg.mipymes.contable.mipymes_contable_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad que representa movimientos bancarios (depósitos, cheques, etc.)
 */
@Entity
@Table(name = "movimientos_banco")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoBanco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_bancaria_id", nullable = false)
    @NotNull(message = "La cuenta bancaria es obligatoria")
    private CuentaBancaria cuentaBancaria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "El tipo de movimiento es obligatorio")
    private TipoMovimientoBanco tipoMovimiento;

    @Column(nullable = false)
    @NotNull(message = "La fecha del movimiento es obligatoria")
    private LocalDate fechaMovimiento;

    @Column(nullable = false, precision = 15, scale = 2)
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser positivo")
    private BigDecimal monto;

    @Column(nullable = false, length = 500)
    @NotNull(message = "La descripción es obligatoria")
    private String descripcion;

    @Column(length = 50)
    private String numeroDocumento; // Número de cheque, boleta de depósito, etc.

    @Column(length = 200)
    private String beneficiario; // Para cheques

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoMovimiento estado = EstadoMovimiento.PENDIENTE;

    @Column
    private LocalDate fechaConciliacion;

    @Column(length = 500)
    private String observaciones;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private LocalDateTime fechaModificacion;

    @Column(length = 100)
    private String creadoPor;

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
     * Verificar si es un débito (salida de dinero)
     */
    public boolean esDebito() {
        return tipoMovimiento.esDebito();
    }

    /**
     * Verificar si es un crédito (entrada de dinero)
     */
    public boolean esCredito() {
        return tipoMovimiento.esCredito();
    }

    /**
     * Obtener monto con signo según el tipo
     */
    public BigDecimal getMontoConSigno() {
        return esDebito() ? monto.negate() : monto;
    }

    /**
     * Tipos de movimiento bancario
     */
    public enum TipoMovimientoBanco {
        DEPOSITO("Depósito", false),
        CHEQUE_EMITIDO("Cheque Emitido", true),
        TRANSFERENCIA_ENTRADA("Transferencia Recibida", false),
        TRANSFERENCIA_SALIDA("Transferencia Enviada", true),
        NOTA_DEBITO("Nota de Débito Bancaria", true),
        NOTA_CREDITO("Nota de Crédito Bancaria", false),
        COMISION_BANCARIA("Comisión Bancaria", true),
        INTERES_GANADO("Interés Ganado", false);

        private final String descripcion;
        private final boolean esDebito;

        TipoMovimientoBanco(String descripcion, boolean esDebito) {
            this.descripcion = descripcion;
            this.esDebito = esDebito;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public boolean esDebito() {
            return esDebito;
        }

        public boolean esCredito() {
            return !esDebito;
        }
    }

    /**
     * Estados del movimiento bancario
     */
    public enum EstadoMovimiento {
        PENDIENTE("Pendiente"),
        CONCILIADO("Conciliado"),
        ANULADO("Anulado");

        private final String descripcion;

        EstadoMovimiento(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }
}