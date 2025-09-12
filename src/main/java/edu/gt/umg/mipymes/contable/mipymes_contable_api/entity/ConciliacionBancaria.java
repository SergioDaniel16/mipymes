package edu.gt.umg.mipymes.contable.mipymes_contable_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad que representa el proceso de conciliación bancaria
 */
@Entity
@Table(name = "conciliaciones_bancarias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConciliacionBancaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_bancaria_id", nullable = false)
    @NotNull(message = "La cuenta bancaria es obligatoria")
    private CuentaBancaria cuentaBancaria;

    @Column(nullable = false)
    @NotNull(message = "La fecha de conciliación es obligatoria")
    private LocalDate fechaConciliacion;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoSegunLibros;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoSegunBanco;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal depositosEnTransito = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal chequesEnCirculacion = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal ajustesLibros = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal ajustesBanco = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean conciliado = false;

    @Column(length = 1000)
    private String observaciones;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(length = 100)
    private String creadoPor;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    /**
     * Calcular saldo conciliado según libros
     */
    public BigDecimal getSaldoConciliadoLibros() {
        return saldoSegunLibros.add(ajustesLibros);
    }

    /**
     * Calcular saldo conciliado según banco
     */
    public BigDecimal getSaldoConciliadoBanco() {
        return saldoSegunBanco
            .add(depositosEnTransito)
            .subtract(chequesEnCirculacion)
            .add(ajustesBanco);
    }

    /**
     * Verificar si la conciliación está balanceada
     */
    public boolean estaBalanceada() {
        BigDecimal diferencia = getSaldoConciliadoLibros().subtract(getSaldoConciliadoBanco());
        return diferencia.abs().compareTo(new BigDecimal("0.01")) < 0;
    }

    /**
     * Obtener diferencia de conciliación
     */
    public BigDecimal getDiferencia() {
        return getSaldoConciliadoLibros().subtract(getSaldoConciliadoBanco());
    }
}