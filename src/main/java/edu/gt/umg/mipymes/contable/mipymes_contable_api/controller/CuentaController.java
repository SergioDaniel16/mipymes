package edu.gt.umg.mipymes.contable.mipymes_contable_api.controller;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.dto.CuentaDTO;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.Cuenta.TipoCuenta;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.service.CuentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para el manejo de Cuentas Contables
 * 
 * Endpoints disponibles:
 * GET    /api/cuentas              - Listar todas las cuentas
 * GET    /api/cuentas/{id}         - Obtener cuenta por ID
 * GET    /api/cuentas/codigo/{codigo} - Obtener cuenta por código
 * GET    /api/cuentas/tipo/{tipo}  - Obtener cuentas por tipo
 * GET    /api/cuentas/buscar       - Buscar cuentas por nombre
 * POST   /api/cuentas              - Crear nueva cuenta
 * PUT    /api/cuentas/{id}         - Actualizar cuenta
 * DELETE /api/cuentas/{id}         - Desactivar cuenta
 */
@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*") // Permitir CORS para Angular (frontend)
public class CuentaController {

    private final CuentaService cuentaService;

    /**
     * GET /api/cuentas
     * Obtener todas las cuentas activas del catálogo
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CuentaDTO>>> obtenerTodasLasCuentas() {
        log.info("Solicitud para obtener todas las cuentas");
        
        List<CuentaDTO> cuentas = cuentaService.obtenerTodasLasCuentas();
        
        ApiResponse<List<CuentaDTO>> response = new ApiResponse<>(
            true,
            "Cuentas obtenidas exitosamente",
            cuentas
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/cuentas/{id}
     * Obtener una cuenta específica por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CuentaDTO>> obtenerCuentaPorId(@PathVariable Long id) {
        log.info("Solicitud para obtener cuenta con ID: {}", id);
        
        CuentaDTO cuenta = cuentaService.obtenerCuentaPorId(id);
        
        ApiResponse<CuentaDTO> response = new ApiResponse<>(
            true,
            "Cuenta encontrada",
            cuenta
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/cuentas/codigo/{codigo}
     * Obtener una cuenta por su código único
     */
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ApiResponse<CuentaDTO>> obtenerCuentaPorCodigo(@PathVariable String codigo) {
        log.info("Solicitud para obtener cuenta con código: {}", codigo);
        
        CuentaDTO cuenta = cuentaService.obtenerCuentaPorCodigo(codigo);
        
        ApiResponse<CuentaDTO> response = new ApiResponse<>(
            true,
            "Cuenta encontrada",
            cuenta
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/cuentas/tipo/{tipo}
     * Obtener cuentas por tipo (ACTIVO, PASIVO, PATRIMONIO, INGRESO, GASTO)
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<ApiResponse<List<CuentaDTO>>> obtenerCuentasPorTipo(@PathVariable TipoCuenta tipo) {
        log.info("Solicitud para obtener cuentas de tipo: {}", tipo);
        
        List<CuentaDTO> cuentas = cuentaService.obtenerCuentasPorTipo(tipo);
        
        ApiResponse<List<CuentaDTO>> response = new ApiResponse<>(
            true,
            "Cuentas de tipo " + tipo.getDescripcion() + " obtenidas exitosamente",
            cuentas
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/cuentas/buscar?nombre=efectivo
     * Buscar cuentas por nombre (búsqueda parcial)
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<CuentaDTO>>> buscarCuentasPorNombre(
            @RequestParam String nombre) {
        log.info("Solicitud para buscar cuentas que contengan: {}", nombre);
        
        List<CuentaDTO> cuentas = cuentaService.buscarCuentasPorNombre(nombre);
        
        ApiResponse<List<CuentaDTO>> response = new ApiResponse<>(
            true,
            "Búsqueda completada",
            cuentas
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/cuentas
     * Crear una nueva cuenta
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CuentaDTO>> crearCuenta(@Valid @RequestBody CuentaDTO cuentaDTO) {
        log.info("Solicitud para crear nueva cuenta con código: {}", cuentaDTO.getCodigo());
        
        CuentaDTO nuevaCuenta = cuentaService.crearCuenta(cuentaDTO);
        
        ApiResponse<CuentaDTO> response = new ApiResponse<>(
            true,
            "Cuenta creada exitosamente",
            nuevaCuenta
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/cuentas/{id}
     * Actualizar una cuenta existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CuentaDTO>> actualizarCuenta(
            @PathVariable Long id, 
            @Valid @RequestBody CuentaDTO cuentaDTO) {
        log.info("Solicitud para actualizar cuenta con ID: {}", id);
        
        CuentaDTO cuentaActualizada = cuentaService.actualizarCuenta(id, cuentaDTO);
        
        ApiResponse<CuentaDTO> response = new ApiResponse<>(
            true,
            "Cuenta actualizada exitosamente",
            cuentaActualizada
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/cuentas/{id}
     * Desactivar una cuenta (no la elimina físicamente)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> desactivarCuenta(@PathVariable Long id) {
        log.info("Solicitud para desactivar cuenta con ID: {}", id);
        
        cuentaService.desactivarCuenta(id);
        
        ApiResponse<Void> response = new ApiResponse<>(
            true,
            "Cuenta desactivada exitosamente",
            null
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/cuentas/catalogo
     * Obtener el catálogo completo de cuentas (endpoint específico del dominio contable)
     */
    @GetMapping("/catalogo")
    public ResponseEntity<ApiResponse<List<CuentaDTO>>> obtenerCatalogoCuentas() {
        log.info("Solicitud para obtener catálogo completo de cuentas");
        
        List<CuentaDTO> catalogo = cuentaService.obtenerCatalogoCuentas();
        
        ApiResponse<List<CuentaDTO>> response = new ApiResponse<>(
            true,
            "Catálogo de cuentas obtenido exitosamente",
            catalogo
        );
        
        return ResponseEntity.ok(response);
    }

    // ========== CLASE PARA RESPUESTAS ESTANDARIZADAS ==========

    /**
     * Estructura estándar para todas las respuestas de la API
     * Facilita el manejo en el frontend
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