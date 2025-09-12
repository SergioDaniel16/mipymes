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
public class EstadoResultadosDTO {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String empresa;
    private String periodo;
    
    // INGRESOS
    private List<LineaBalanceDTO> ingresos;
    private BigDecimal totalIngresos;
    
    // COSTOS
    private List<LineaBalanceDTO> costos;
    private BigDecimal totalCostos;
    
    // UTILIDAD BRUTA
    private BigDecimal utilidadBruta; // Ingresos - Costos
    
    // GASTOS
    private List<LineaBalanceDTO> gastos;
    private BigDecimal totalGastos;
    
    // RESULTADO NETO
    private BigDecimal utilidadNeta; // Utilidad Bruta - Gastos
    private String tipoResultado; // "UTILIDAD" o "PERDIDA"
}