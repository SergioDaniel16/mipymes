package edu.gt.umg.mipymes.contable.mipymes_contable_api.controller;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.dto.BalanceGeneralDTO;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.dto.EstadoResultadosDTO;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.service.EstadosFinancierosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Controller REST para Estados Financieros
 * 
 * Endpoints disponibles:
 * GET /api/estados-financieros/balance-general              - Balance General actual
 * GET /api/estados-financieros/balance-general/{fecha}      - Balance General a fecha
 * GET /api/estados-financieros/estado-resultados           - Estado de Resultados anual
 * GET /api/estados-financieros/estado-resultados/periodo   - Estado de Resultados por período
 */
@RestController
@RequestMapping("/api/estados-financieros")
@RequiredArgsConstructor
@Slf4j
public class EstadosFinancierosController {

    private final EstadosFinancierosService estadosFinancierosService;

    /**
     * GET /api/estados-financieros/balance-general
     * Balance General a la fecha actual
     */
    @GetMapping("/balance-general")
    public ResponseEntity<ApiResponse<BalanceGeneralDTO>> generarBalanceGeneral() {
        log.info("Solicitud para generar Balance General actual");
        
        BalanceGeneralDTO balance = estadosFinancierosService.generarBalanceGeneral();
        
        ApiResponse<BalanceGeneralDTO> response = new ApiResponse<>(
            true,
            "Balance General generado exitosamente",
            balance
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/estados-financieros/balance-general/2024-12-31
     * Balance General a fecha específica
     */
    @GetMapping("/balance-general/{fecha}")
    public ResponseEntity<ApiResponse<BalanceGeneralDTO>> generarBalanceGeneralPorFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        
        log.info("Solicitud para generar Balance General al {}", fecha);
        
        BalanceGeneralDTO balance = estadosFinancierosService.generarBalanceGeneral(fecha);
        
        ApiResponse<BalanceGeneralDTO> response = new ApiResponse<>(
            true,
            "Balance General al " + fecha + " generado exitosamente",
            balance
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/estados-financieros/estado-resultados
     * Estado de Resultados del año actual (enero a fecha actual)
     */
    @GetMapping("/estado-resultados")
    public ResponseEntity<ApiResponse<EstadoResultadosDTO>> generarEstadoResultadosAnual() {
        log.info("Solicitud para generar Estado de Resultados anual");
        
        EstadoResultadosDTO estadoResultados = estadosFinancierosService.generarEstadoResultadosAnual();
        
        ApiResponse<EstadoResultadosDTO> response = new ApiResponse<>(
            true,
            "Estado de Resultados generado exitosamente",
            estadoResultados
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/estados-financieros/estado-resultados/periodo?fechaInicio=2024-01-01&fechaFin=2024-12-31
     * Estado de Resultados por período específico
     */
    @GetMapping("/estado-resultados/periodo")
    public ResponseEntity<ApiResponse<EstadoResultadosDTO>> generarEstadoResultadosPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        
        log.info("Solicitud para generar Estado de Resultados del {} al {}", fechaInicio, fechaFin);
        
        EstadoResultadosDTO estadoResultados = estadosFinancierosService.generarEstadoResultados(fechaInicio, fechaFin);
        
        ApiResponse<EstadoResultadosDTO> response = new ApiResponse<>(
            true,
            "Estado de Resultados del período generado exitosamente",
            estadoResultados
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * Estructura estándar para respuestas de la API
     */
    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;

        public ApiResponse(boolean success, String message, T data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public T getData() { return data; }

        public void setSuccess(boolean success) { this.success = success; }
        public void setMessage(String message) { this.message = message; }
        public void setData(T data) { this.data = data; }
    }
}