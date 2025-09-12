package edu.gt.umg.mipymes.contable.mipymes_contable_api.dto;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.CuentaBancaria.TipoCuentaBancaria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaBancariaDTO {
    private Long id;

    @NotBlank(message = "El nombre de la cuenta bancaria es obligatorio")
    private String nombre;

    @NotBlank(message = "El banco es obligatorio")
    private String banco;

    @NotBlank(message = "El número de cuenta es obligatorio")
    private String numeroCuenta;

    @NotNull(message = "El tipo de cuenta es obligatorio")
    private TipoCuentaBancaria tipo;

    private BigDecimal saldoLibros;
    private BigDecimal saldoBanco;
    private Boolean activa;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    // Campos calculados
    private BigDecimal diferenciaConciliacion;
    private Boolean estaConciliada;
    private String tipoDescripcion;

    public void calcularCampos() {
        this.diferenciaConciliacion = saldoLibros != null && saldoBanco != null
            ? saldoLibros.subtract(saldoBanco)
            : BigDecimal.ZERO;
            
        this.estaConciliada = diferenciaConciliacion.abs().compareTo(new BigDecimal("0.01")) < 0;
        
        this.tipoDescripcion = tipo != null ? tipo.getDescripcion() : null;
    }
}