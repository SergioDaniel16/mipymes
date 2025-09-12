package edu.gt.umg.mipymes.contable.mipymes_contable_api.dto;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.MovimientoInventario.TipoMovimiento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoInventarioDTO {

    private Long id;

    @NotNull(message = "El producto es obligatorio")
    private Long productoId;
    
    private String productoNombre;
    private String productoCodigo;

    @NotNull(message = "El tipo de movimiento es obligatorio")
    private TipoMovimiento tipoMovimiento;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser positiva")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    private BigDecimal precioUnitario;

    private Integer existenciaAnterior;
    private Integer existenciaNueva;
    private String observaciones;
    private String numeroDocumento;
    private LocalDateTime fechaMovimiento;
    private String creadoPor;

    // Campos calculados
    private BigDecimal valorTotal;
    private String tipoMovimientoDescripcion;
    private Boolean esEntrada;

    public void calcularCampos() {
        this.valorTotal = precioUnitario != null && cantidad != null
            ? precioUnitario.multiply(new BigDecimal(cantidad))
            : BigDecimal.ZERO;
            
        this.tipoMovimientoDescripcion = tipoMovimiento != null
            ? tipoMovimiento.getDescripcion()
            : null;
            
        this.esEntrada = tipoMovimiento != null
            ? tipoMovimiento.esEntrada()
            : null;
    }
}