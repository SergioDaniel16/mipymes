package edu.gt.umg.mipymes.contable.mipymes_contable_api.dto;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.MovimientoBanco.TipoMovimientoBanco;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearMovimientoBancoDTO {
    @NotNull(message = "La cuenta bancaria es obligatoria")
    private Long cuentaBancariaId;

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
    private String observaciones;
    private String creadoPor;
}