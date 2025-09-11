package edu.gt.umg.mipymes.contable.mipymes_contable_api.controller;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.dto.AsientoContableDTO;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.service.AsientoContableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller REST para el manejo de Asientos Contables (Libro Diario)
 * 
 * Endpoints disponibles:
 * GET    /api/asientos                    - Libro diario completo
 * GET    /api/asientos/{id}               - Obtener asiento por ID
 * GET    /api/asientos/numero/{numero}    - Obtener asiento por número
 * GET    /api/asientos/periodo            - Libro diario por período
 * GET    /api/asientos/buscar             - Buscar asientos por descripción
 * POST   /api/asientos                    - Crear nuevo asiento
 * PUT    /api/asientos/{id}/contabilizar  - Contabilizar asiento
 */
@RestController
@RequestMapping("/api/asientos")
@RequiredArgsConstructor
@Slf4j
public class AsientoContableController {

    private final AsientoContableService asientoService;

    /**
     * GET /api/asientos
     * Obtener el libro diario completo
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<AsientoContableDTO>>> obtenerLibroDiario() {
        log.info("Solicitud para obtener libro diario completo");
        
        List<AsientoContableDTO> asientos = asientoService.obtenerLibroDiario();
        
        ApiResponse<List<AsientoContableDTO>> response = new ApiResponse<>(
            true,
            "Libro diario obtenido exitosamente",
            asientos
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/asientos/periodo?fechaInicio=2024-01-01&fechaFin=2024-12-31
     * Obtener libro diario por período
     */
    @GetMapping("/periodo")
    public ResponseEntity<ApiResponse<List<AsientoContableDTO>>> obtenerLibroDiarioPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        
        log.info("Solicitud libro diario del {} al {}", fechaInicio, fechaFin);
        
        List<AsientoContableDTO> asientos = asientoService.obtenerLibroDiarioPorPeriodo(fechaInicio, fechaFin);
        
        ApiResponse<List<AsientoContableDTO>> response = new ApiResponse<>(
            true,
            "Libro diario del período obtenido exitosamente",
            asientos
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/asientos/{id}
     * Obtener asiento por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AsientoContableDTO>> obtenerAsientoPorId(@PathVariable Long id) {
        log.info("Solicitud para obtener asiento con ID: {}", id);
        
        AsientoContableDTO asiento = asientoService.obtenerAsientoPorId(id);
        
        ApiResponse<AsientoContableDTO> response = new ApiResponse<>(
            true,
            "Asiento encontrado",
            asiento
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/asientos/numero/{numero}
     * Obtener asiento por número
     */
    @GetMapping("/numero/{numero}")
    public ResponseEntity<ApiResponse<AsientoContableDTO>> obtenerAsientoPorNumero(@PathVariable Integer numero) {
        log.info("Solicitud para obtener asiento número: {}", numero);
        
        AsientoContableDTO asiento = asientoService.obtenerAsientoPorNumero(numero);
        
        ApiResponse<AsientoContableDTO> response = new ApiResponse<>(
            true,
            "Asiento encontrado",
            asiento
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/asientos/buscar?descripcion=deposito
     * Buscar asientos por descripción
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<AsientoContableDTO>>> buscarAsientosPorDescripcion(
            @RequestParam String descripcion) {
        
        log.info("Solicitud para buscar asientos que contengan: {}", descripcion);
        
        List<AsientoContableDTO> asientos = asientoService.buscarAsientosPorDescripcion(descripcion);
        
        ApiResponse<List<AsientoContableDTO>> response = new ApiResponse<>(
            true,
            "Búsqueda completada",
            asientos
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/asientos
     * Crear un nuevo asiento contable
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AsientoContableDTO>> crearAsiento(
            @Valid @RequestBody AsientoContableDTO asientoDTO) {
        
        log.info("Solicitud para crear nuevo asiento: {}", asientoDTO.getDescripcion());
        
        AsientoContableDTO nuevoAsiento = asientoService.crearAsiento(asientoDTO);
        
        ApiResponse<AsientoContableDTO> response = new ApiResponse<>(
            true,
            "Asiento creado exitosamente con número " + nuevoAsiento.getNumeroAsiento(),
            nuevoAsiento
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/asientos/{id}/contabilizar
     * Contabilizar un asiento (afectar saldos de cuentas)
     */
    @PutMapping("/{id}/contabilizar")
    public ResponseEntity<ApiResponse<AsientoContableDTO>> contabilizarAsiento(@PathVariable Long id) {
        log.info("Solicitud para contabilizar asiento con ID: {}", id);
        
        AsientoContableDTO asientoContabilizado = asientoService.contabilizarAsiento(id);
        
        ApiResponse<AsientoContableDTO> response = new ApiResponse<>(
            true,
            "Asiento contabilizado exitosamente",
            asientoContabilizado
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