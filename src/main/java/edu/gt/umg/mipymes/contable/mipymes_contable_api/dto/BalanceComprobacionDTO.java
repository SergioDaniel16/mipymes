package edu.gt.umg.mipymes.contable.mipymes_contable_api.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceComprobacionDTO {
    private LocalDate fechaCorte;
    private List<LineaBalanceDTO> lineas;
    private TotalesBalanceDTO totales;
    private boolean balanceado;
    private String empresa;
    private String periodo;
}