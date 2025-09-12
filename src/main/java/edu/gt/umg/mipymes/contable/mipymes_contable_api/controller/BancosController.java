package edu.gt.umg.mipymes.contable.mipymes_contable_api.controller;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.dto.*;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.service.BancosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller REST para el Módulo de Bancos y Caja
 * 
 * Endpoints disponibles:
 * 
 * CUENTAS BANCARIAS:
 * GET    /api/bancos/cuentas                    - Listar cuentas bancarias
 * GET    /api/bancos/cuentas/{id}               - Obtener cuenta por ID
 * GET    /api/bancos/cuentas/no-conciliadas     - Cuentas no conciliadas
 * POST   /api/bancos/cuentas                    - Crear cuenta bancaria
 * PUT    /api/bancos/cuentas/{id}               - Actualizar cuenta bancaria
 * PUT    /api/bancos/cuentas/{id}/saldo-banco   - Actualizar saldo bancario
 * 
 * MOVIMIENTOS BANCARIOS:
 * GET    /api/bancos/movimientos                - Últimos movimientos
 * GET    /api/bancos/movimientos/cuenta/{id}    - Movimientos por cuenta
 * GET    /api/bancos/movimientos/fecha          - Movimientos por fecha
 * GET    /api/bancos/movimientos/cheques        - Cheques en circulación
 * POST   /api/bancos/movimientos                - Registrar movimiento
 * PUT    /api/bancos/movimientos/{id}/conciliar - Conciliar movimiento
 * 
 * REPORTES:
 * GET    /api/bancos/resumen                    - Resumen bancario
 */
@RestController
@RequestMapping("/api/bancos")
@RequiredArgsConstructor
@Slf4j
public class BancosController {

    private final BancosService bancosService;

    // ========== GESTIÓN DE CUENTAS BANCARIAS ==========

    /**
     * GET /api/bancos/cuentas
     * Obtener todas las cuentas bancarias activas
     */
    @GetMapping("/cuentas")
    public ResponseEntity<ApiResponse<List<CuentaBancariaDTO>>> obtenerTodasLasCuentasBancarias() {
        log.info("Solicitud para obtener todas las cuentas bancarias");
        
        List<CuentaBancariaDTO> cuentas = bancosService.obtenerTodasLasCuentasBancarias();
        
        ApiResponse<List<CuentaBancariaDTO>> response = new ApiResponse<>(
            true,
            "Cuentas bancarias obtenidas exitosamente",
            cuentas
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/bancos/cuentas/{id}
     * Obtener cuenta bancaria por ID
     */
    @GetMapping("/cuentas/{id}")
    public ResponseEntity<ApiResponse<CuentaBancariaDTO>> obtenerCuentaBancariaPorId(@PathVariable Long id) {
        log.info("Solicitud para obtener cuenta bancaria con ID: {}", id);
        
        CuentaBancariaDTO cuenta = bancosService.obtenerCuentaBancariaPorId(id);
        
        ApiResponse<CuentaBancariaDTO> response = new ApiResponse<>(
            true,
            "Cuenta bancaria encontrada",
            cuenta
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/bancos/cuentas/no-conciliadas
     * Obtener cuentas bancarias no conciliadas
     */
    @GetMapping("/cuentas/no-conciliadas")
    public ResponseEntity<ApiResponse<List<CuentaBancariaDTO>>> obtenerCuentasNoConciliadas() {
        log.info("Solicitud para obtener cuentas bancarias no conciliadas");
        
        List<CuentaBancariaDTO> cuentas = bancosService.obtenerCuentasNoConciliadas();
        
        ApiResponse<List<CuentaBancariaDTO>> response = new ApiResponse<>(
            true,
            "Cuentas no conciliadas obtenidas exitosamente",
            cuentas
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/bancos/cuentas
     * Crear una nueva cuenta bancaria
     */
    @PostMapping("/cuentas")
    public ResponseEntity<ApiResponse<CuentaBancariaDTO>> crearCuentaBancaria(
            @Valid @RequestBody CuentaBancariaDTO cuentaDTO) {
        log.info("Solicitud para crear nueva cuenta bancaria: {}", cuentaDTO.getNombre());
        
        CuentaBancariaDTO nuevaCuenta = bancosService.crearCuentaBancaria(cuentaDTO);
        
        ApiResponse<CuentaBancariaDTO> response = new ApiResponse<>(
            true,
            "Cuenta bancaria creada exitosamente",
            nuevaCuenta
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/bancos/cuentas/{id}
     * Actualizar una cuenta bancaria existente
     */
    @PutMapping("/cuentas/{id}")
    public ResponseEntity<ApiResponse<CuentaBancariaDTO>> actualizarCuentaBancaria(
            @PathVariable Long id, 
            @Valid @RequestBody CuentaBancariaDTO cuentaDTO) {
        log.info("Solicitud para actualizar cuenta bancaria con ID: {}", id);
        
        CuentaBancariaDTO cuentaActualizada = bancosService.actualizarCuentaBancaria(id, cuentaDTO);
        
        ApiResponse<CuentaBancariaDTO> response = new ApiResponse<>(
            true,
            "Cuenta bancaria actualizada exitosamente",
            cuentaActualizada
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/bancos/cuentas/{id}/saldo-banco
     * Actualizar saldo bancario (para conciliación)
     */
    @PutMapping("/cuentas/{id}/saldo-banco")
    public ResponseEntity<ApiResponse<CuentaBancariaDTO>> actualizarSaldoBanco(
            @PathVariable Long id,
            @RequestParam BigDecimal saldoBanco) {
        log.info("Solicitud para actualizar saldo bancario - Cuenta ID: {}, Saldo: {}", id, saldoBanco);
        
        CuentaBancariaDTO cuentaActualizada = bancosService.actualizarSaldoBanco(id, saldoBanco);
        
        ApiResponse<CuentaBancariaDTO> response = new ApiResponse<>(
            true,
            "Saldo bancario actualizado exitosamente",
            cuentaActualizada
        );
        
        return ResponseEntity.ok(response);
    }

    // ========== MOVIMIENTOS BANCARIOS ==========

    /**
     * GET /api/bancos/movimientos
     * Obtener últimos movimientos bancarios
     */
    @GetMapping("/movimientos")
    public ResponseEntity<ApiResponse<List<MovimientoBancoDTO>>> obtenerUltimosMovimientos() {
        log.info("Solicitud para obtener últimos movimientos bancarios");
        
        List<MovimientoBancoDTO> movimientos = bancosService.obtenerUltimosMovimientos();
        
        ApiResponse<List<MovimientoBancoDTO>> response = new ApiResponse<>(
            true,
            "Movimientos bancarios obtenidos exitosamente",
            movimientos
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/bancos/movimientos/cuenta/{id}
     * Obtener movimientos de una cuenta bancaria específica
     */
    @GetMapping("/movimientos/cuenta/{id}")
    public ResponseEntity<ApiResponse<List<MovimientoBancoDTO>>> obtenerMovimientosPorCuenta(
            @PathVariable Long id) {
        log.info("Solicitud para obtener movimientos de la cuenta bancaria ID: {}", id);
        
        List<MovimientoBancoDTO> movimientos = bancosService.obtenerMovimientosPorCuenta(id);
        
        ApiResponse<List<MovimientoBancoDTO>> response = new ApiResponse<>(
            true,
            "Movimientos de la cuenta obtenidos exitosamente",
            movimientos
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/bancos/movimientos/fecha?fechaInicio=2024-01-01&fechaFin=2024-12-31
     * Obtener movimientos por rango de fechas
     */
    @GetMapping("/movimientos/fecha")
    public ResponseEntity<ApiResponse<List<MovimientoBancoDTO>>> obtenerMovimientosPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        
        log.info("Solicitud para obtener movimientos bancarios del {} al {}", fechaInicio, fechaFin);
        
        List<MovimientoBancoDTO> movimientos = bancosService.obtenerMovimientosPorFecha(fechaInicio, fechaFin);
        
        ApiResponse<List<MovimientoBancoDTO>> response = new ApiResponse<>(
            true,
            "Movimientos del período obtenidos exitosamente",
            movimientos
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/bancos/movimientos/cheques
     * Obtener cheques en circulación (pendientes)
     */
    @GetMapping("/movimientos/cheques")
    public ResponseEntity<ApiResponse<List<MovimientoBancoDTO>>> obtenerChequesEnCirculacion() {
        log.info("Solicitud para obtener cheques en circulación");
        
        List<MovimientoBancoDTO> cheques = bancosService.obtenerChequesEnCirculacion();
        
        ApiResponse<List<MovimientoBancoDTO>> response = new ApiResponse<>(
            true,
            "Cheques en circulación obtenidos exitosamente",
            cheques
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/bancos/movimientos
     * Registrar un nuevo movimiento bancario
     */
    @PostMapping("/movimientos")
    public ResponseEntity<ApiResponse<MovimientoBancoDTO>> registrarMovimientoBancario(
            @Valid @RequestBody CrearMovimientoBancoDTO movimientoDTO) {
        log.info("Solicitud para registrar movimiento bancario - Cuenta ID: {}, Tipo: {}", 
                movimientoDTO.getCuentaBancariaId(), movimientoDTO.getTipoMovimiento());
        
        MovimientoBancoDTO movimiento = bancosService.registrarMovimientoBancario(movimientoDTO);
        
        ApiResponse<MovimientoBancoDTO> response = new ApiResponse<>(
            true,
            "Movimiento bancario registrado exitosamente",
            movimiento
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/bancos/movimientos/{id}/conciliar
     * Conciliar un movimiento bancario
     */
    @PutMapping("/movimientos/{id}/conciliar")
    public ResponseEntity<ApiResponse<MovimientoBancoDTO>> conciliarMovimiento(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaConciliacion) {
        
        log.info("Solicitud para conciliar movimiento bancario ID: {} en fecha: {}", id, fechaConciliacion);
        
        MovimientoBancoDTO movimiento = bancosService.conciliarMovimiento(id, fechaConciliacion);
        
        ApiResponse<MovimientoBancoDTO> response = new ApiResponse<>(
            true,
            "Movimiento bancario conciliado exitosamente",
            movimiento
        );
        
        return ResponseEntity.ok(response);
    }

    // ========== REPORTES ==========

    /**
     * GET /api/bancos/resumen
     * Generar resumen bancario completo
     */
    @GetMapping("/resumen")
    public ResponseEntity<ApiResponse<ResumenBancarioDTO>> generarResumenBancario() {
        log.info("Solicitud para generar resumen bancario");
        
        ResumenBancarioDTO resumen = bancosService.generarResumenBancario();
        
        ApiResponse<ResumenBancarioDTO> response = new ApiResponse<>(
            true,
            "Resumen bancario generado exitosamente",
            resumen
        );
        
        return ResponseEntity.ok(response);
    }

    // ========== CLASE PARA RESPUESTAS ESTANDARIZADAS ==========

    /**
     * Estructura estándar para todas las respuestas de la API
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

        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public T getData() { return data; }

        // Setters
        public void setSuccess(boolean success) { this.success = success; }
        public void setMessage(String message) { this.message = message; }
        public void setData(T data) { this.data = data; }
    }
}