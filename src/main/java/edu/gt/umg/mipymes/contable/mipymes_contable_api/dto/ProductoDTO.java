package edu.gt.umg.mipymes.contable.mipymes_contable_api.dto;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.Producto.UnidadMedida;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    private Long id;

    @NotBlank(message = "El código del producto es obligatorio")
    private String codigo;

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El precio de compra es obligatorio")
    @PositiveOrZero(message = "El precio de compra debe ser mayor o igual a cero")
    private BigDecimal precioCompra;

    @NotNull(message = "El precio de venta es obligatorio")
    @PositiveOrZero(message = "El precio de venta debe ser mayor o igual a cero")
    private BigDecimal precioVenta;

    @NotNull(message = "La cantidad en existencia es obligatoria")
    @PositiveOrZero(message = "La existencia debe ser mayor o igual a cero")
    private Integer existencia;

    @PositiveOrZero(message = "El stock mínimo debe ser mayor o igual a cero")
    private Integer stockMinimo;

    @NotNull(message = "La unidad de medida es obligatoria")
    private UnidadMedida unidadMedida;

    private Boolean activo;
    private String categoria;
    private String proveedor;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    // Campos calculados
    private BigDecimal valorInventario;
    private BigDecimal margenGanancia;
    private Boolean enStockMinimo;
    private String unidadMedidaDescripcion;

    public void calcularCampos() {
        this.valorInventario = precioCompra != null && existencia != null 
            ? precioCompra.multiply(new BigDecimal(existencia))
            : BigDecimal.ZERO;
            
        this.margenGanancia = precioCompra != null && precioVenta != null && precioCompra.compareTo(BigDecimal.ZERO) > 0
            ? precioVenta.subtract(precioCompra).divide(precioCompra, 4, BigDecimal.ROUND_HALF_UP)
            : BigDecimal.ZERO;
            
        this.enStockMinimo = existencia != null && stockMinimo != null 
            ? existencia <= stockMinimo
            : false;
            
        this.unidadMedidaDescripcion = unidadMedida != null 
            ? unidadMedida.getDescripcion()
            : null;
    }
}