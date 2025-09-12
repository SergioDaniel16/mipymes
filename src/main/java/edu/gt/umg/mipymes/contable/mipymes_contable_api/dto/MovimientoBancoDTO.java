package edu.gt.umg.mipymes.contable.mipymes_contable_api.dto;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.MovimientoBanco.TipoMovimientoBanco;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.MovimientoBanco.EstadoMovimiento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoBancoDTO {
    private Long id;

    @NotNull(message = "La cuenta bancaria es obligatoria")
    private Long cuentaBancariaId;
    private String cuentaBancariaNombre;

    @NotNull(message = "El tipo de movimiento es obligatorio")
    private TipoMovimientoBanco tipoMovimiento;

    @NotNull(message = "La fecha del movimiento es obligatoria")
    private LocalDate fechaMovimiento;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser positivo")
    private BigDecimal monto;

    @NotNull(message = "La descripción es obligatoria")
    private String descripcion;

    private String numeroDocumento;
    private String beneficiario;
    private EstadoMovimiento estado;
    private LocalDate fechaConciliacion;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private String creadoPor;

    // Campos calculados
    private BigDecimal montoConSigno;
    private String tipoMovimientoDescripcion;
    private String estadoDescripcion;
    private Boolean esDebito;

    public void calcularCampos() {
        this.montoConSigno = tipoMovimiento != null && monto != null
            ? (tipoMovimiento.esDebito() ? monto.negate() : monto)
            : BigDecimal.ZERO;
            
        this.tipoMovimientoDescripcion = tipoMovimiento != null
            ? tipoMovimiento.getDescripcion()
            : null;
            
        this.estadoDescripcion = estado != null
            ? estado.getDescripcion()
            : null;
            
        this.esDebito = tipoMovimiento != null
            ? tipoMovimiento.esDebito()
            : null;
    }
}