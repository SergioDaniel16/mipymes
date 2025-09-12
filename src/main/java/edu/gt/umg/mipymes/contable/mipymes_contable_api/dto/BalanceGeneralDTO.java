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
public class BalanceGeneralDTO {
    private LocalDate fechaCorte;
    private String empresa;
    private String periodo;
    
    // ACTIVOS
    private List<LineaBalanceDTO> activosCorrientes;
    private List<LineaBalanceDTO> activosNoCorrientes;
    private BigDecimal totalActivos;
    
    // PASIVOS
    private List<LineaBalanceDTO> pasivosCorrientes;
    private List<LineaBalanceDTO> pasivosNoCorrientes;
    private BigDecimal totalPasivos;
    
    // PATRIMONIO
    private List<LineaBalanceDTO> patrimonio;
    private BigDecimal totalPatrimonio;
    
    // VALIDACIÓN
    private boolean balanceado; // Total Activos = Total Pasivos + Patrimonio
    private BigDecimal diferencia;
}