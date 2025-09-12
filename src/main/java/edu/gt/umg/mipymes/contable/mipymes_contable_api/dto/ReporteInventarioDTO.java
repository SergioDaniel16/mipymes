package edu.gt.umg.mipymes.contable.mipymes_contable_api.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteInventarioDTO {
    private LocalDate fechaCorte;
    private String empresa;
    private List<ProductoDTO> productos;
    private Integer totalProductos;
    private Integer productosEnStockMinimo;
    private BigDecimal valorTotalInventario;
    private List<ProductoDTO> productosStockMinimo;
}