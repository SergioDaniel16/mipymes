package edu.gt.umg.mipymes.contable.mipymes_contable_api.controller;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.dto.BalanceComprobacionDTO;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.Cuenta.TipoCuenta;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.service.BalanceComprobacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Controller REST para Balance de Comprobación
 * 
 * Endpoints disponibles:
 * GET /api/balance-comprobacion              - Balance actual
 * GET /api/balance-comprobacion/fecha/{fecha} - Balance a fecha específica
 * GET /api/balance-comprobacion/con-saldo    - Solo cuentas con saldo
 * GET /api/balance-comprobacion/tipo/{tipo}  - Balance por tipo de cuenta
 */
@RestController
@RequestMapping("/api/balance-comprobacion")
@RequiredArgsConstructor
@Slf4j
public class BalanceComprobacionController {

    private final BalanceComprobacionService balanceService;

    /**
     * GET /api/balance-comprobacion
     * Generar Balance de Comprobación a la fecha actual
     */
    @GetMapping
    public ResponseEntity<ApiResponse<BalanceComprobacionDTO>> generarBalanceActual() {
        log.info("Solicitud para generar Balance de Comprobación actual");
        
        BalanceComprobacionDTO balance = balanceService.generarBalanceComprobacion();
        
        ApiResponse<BalanceComprobacionDTO> response = new ApiResponse<>(
            true,
            "Balance de Comprobación generado exitosamente",
            balance
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/balance-comprobacion/fecha/2024-01-31
     * Generar Balance de Comprobación a fecha específica
     */
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<ApiResponse<BalanceComprobacionDTO>> generarBalancePorFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        
        log.info("Solicitud para generar Balance de Comprobación al {}", fecha);
        
        BalanceComprobacionDTO balance = balanceService.generarBalanceComprobacion(fecha);
        
        ApiResponse<BalanceComprobacionDTO> response = new ApiResponse<>(
            true,
            "Balance de Comprobación al " + fecha + " generado exitosamente",
            balance
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/balance-comprobacion/con-saldo
     * Generar Balance de Comprobación solo con cuentas que tienen saldo
     */
    @GetMapping("/con-saldo")
    public ResponseEntity<ApiResponse<BalanceComprobacionDTO>> generarBalanceConSaldo() {
        log.info("Solicitud para generar Balance de Comprobación con saldo únicamente");
        
        BalanceComprobacionDTO balance = balanceService.generarBalanceConSaldo();
        
        ApiResponse<BalanceComprobacionDTO> response = new ApiResponse<>(
            true,
            "Balance de Comprobación (solo cuentas con saldo) generado exitosamente",
            balance
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/balance-comprobacion/tipo/ACTIVO
     * Generar Balance de Comprobación filtrado por tipo de cuenta
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<ApiResponse<BalanceComprobacionDTO>> generarBalancePorTipo(
            @PathVariable TipoCuenta tipo) {
        
        log.info("Solicitud para generar Balance de Comprobación de tipo: {}", tipo);
        
        BalanceComprobacionDTO balance = balanceService.generarBalancePorTipo(tipo);
        
        ApiResponse<BalanceComprobacionDTO> response = new ApiResponse<>(
            true,
            "Balance de Comprobación de " + tipo.getDescripcion() + " generado exitosamente",
            balance
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