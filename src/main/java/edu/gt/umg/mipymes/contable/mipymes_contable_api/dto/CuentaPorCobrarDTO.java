package edu.gt.umg.mipymes.contable.mipymes_contable_api.dto;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.CuentaPorCobrar.EstadoCuenta;
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
public class CuentaPorCobrarDTO {
    private Long id;

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;
    private String clienteNombre;

    @NotNull(message = "El número de documento es obligatorio")
    private String numeroDocumento;

    @NotNull(message = "La fecha de emisión es obligatoria")
    private LocalDate fechaEmision;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    private LocalDate fechaVencimiento;

    @NotNull(message = "El monto original es obligatorio")
    @Positive(message = "El monto debe ser positivo")
    private BigDecimal montoOriginal;

    private BigDecimal montoAbonado;
    private BigDecimal saldoPendiente;
    private EstadoCuenta estado;
    private String descripcion;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    // Campos calculados
    private Boolean estaVencida;
    private Long diasAtraso;
    private String estadoDescripcion;

    public void calcularCampos() {
        LocalDate hoy = LocalDate.now();
        this.estaVencida = fechaVencimiento != null && hoy.isAfter(fechaVencimiento) && 
                         estado == EstadoCuenta.PENDIENTE;
                         
        if (estaVencida && fechaVencimiento != null) {
            this.diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(fechaVencimiento, hoy);
        } else {
            this.diasAtraso = 0L;
        }
        
        this.estadoDescripcion = estado != null ? estado.getDescripcion() : null;
    }
}