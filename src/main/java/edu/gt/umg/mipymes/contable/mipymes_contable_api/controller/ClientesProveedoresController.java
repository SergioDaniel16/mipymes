package edu.gt.umg.mipymes.contable.mipymes_contable_api.controller;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.dto.*;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.service.ClientesProveedoresService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para el Módulo de Clientes y Proveedores
 * 
 * Endpoints disponibles:
 * 
 * CLIENTES:
 * GET    /api/clientes-proveedores/clientes                - Listar clientes
 * GET    /api/clientes-proveedores/clientes/{id}           - Obtener cliente por ID
 * GET    /api/clientes-proveedores/clientes/buscar         - Buscar clientes por nombre
 * GET    /api/clientes-proveedores/clientes/con-saldo      - Clientes con saldo pendiente
 * POST   /api/clientes-proveedores/clientes                - Crear cliente
 * PUT    /api/clientes-proveedores/clientes/{id}           - Actualizar cliente
 * 
 * PROVEEDORES:
 * GET    /api/clientes-proveedores/proveedores             - Listar proveedores
 * GET    /api/clientes-proveedores/proveedores/{id}        - Obtener proveedor por ID
 * POST   /api/clientes-proveedores/proveedores             - Crear proveedor
 * PUT    /api/clientes-proveedores/proveedores/{id}        - Actualizar proveedor
 * 
 * CUENTAS POR COBRAR:
 * GET    /api/clientes-proveedores/cuentas-cobrar/cliente/{id} - CxC por cliente
 * GET    /api/clientes-proveedores/cuentas-cobrar/vencidas     - CxC vencidas
 * POST   /api/clientes-proveedores/cuentas-cobrar              - Crear CxC
 * 
 * CUENTAS POR PAGAR:
 * GET    /api/clientes-proveedores/cuentas-pagar/vencidas   - CxP vencidas
 * POST   /api/clientes-proveedores/cuentas-pagar            - Crear CxP
 * 
 * REPORTES:
 * GET    /api/clientes-proveedores/resumen                  - Resumen general
 */
@RestController
@RequestMapping("/api/clientes-proveedores")
@RequiredArgsConstructor
@Slf4j
public class ClientesProveedoresController {

    private final ClientesProveedoresService clientesProveedoresService;

    // ========== GESTIÓN DE CLIENTES ==========

    /**
     * GET /api/clientes-proveedores/clientes
     * Obtener todos los clientes activos
     */
    @GetMapping("/clientes")
    public ResponseEntity<ApiResponse<List<ClienteDTO>>> obtenerTodosLosClientes() {
        log.info("Solicitud para obtener todos los clientes");
        
        List<ClienteDTO> clientes = clientesProveedoresService.obtenerTodosLosClientes();
        
        ApiResponse<List<ClienteDTO>> response = new ApiResponse<>(
            true,
            "Clientes obtenidos exitosamente",
            clientes
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/clientes-proveedores/clientes/{id}
     * Obtener cliente por ID
     */
    @GetMapping("/clientes/{id}")
    public ResponseEntity<ApiResponse<ClienteDTO>> obtenerClientePorId(@PathVariable Long id) {
        log.info("Solicitud para obtener cliente con ID: {}", id);
        
        ClienteDTO cliente = clientesProveedoresService.obtenerClientePorId(id);
        
        ApiResponse<ClienteDTO> response = new ApiResponse<>(
            true,
            "Cliente encontrado",
            cliente
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/clientes-proveedores/clientes/buscar?nombre=cliente
     * Buscar clientes por nombre
     */
    @GetMapping("/clientes/buscar")
    public ResponseEntity<ApiResponse<List<ClienteDTO>>> buscarClientesPorNombre(
            @RequestParam String nombre) {
        log.info("Solicitud para buscar clientes que contengan: {}", nombre);
        
        List<ClienteDTO> clientes = clientesProveedoresService.buscarClientesPorNombre(nombre);
        
        ApiResponse<List<ClienteDTO>> response = new ApiResponse<>(
            true,
            "Búsqueda completada",
            clientes
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/clientes-proveedores/clientes/con-saldo
     * Obtener clientes con saldo pendiente
     */
    @GetMapping("/clientes/con-saldo")
    public ResponseEntity<ApiResponse<List<ClienteDTO>>> obtenerClientesConSaldo() {
        log.info("Solicitud para obtener clientes con saldo pendiente");
        
        List<ClienteDTO> clientes = clientesProveedoresService.obtenerClientesConSaldo();
        
        ApiResponse<List<ClienteDTO>> response = new ApiResponse<>(
            true,
            "Clientes con saldo obtenidos exitosamente",
            clientes
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/clientes-proveedores/clientes
     * Crear un nuevo cliente
     */
    @PostMapping("/clientes")
    public ResponseEntity<ApiResponse<ClienteDTO>> crearCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        log.info("Solicitud para crear nuevo cliente con código: {}", clienteDTO.getCodigo());
        
        ClienteDTO nuevoCliente = clientesProveedoresService.crearCliente(clienteDTO);
        
        ApiResponse<ClienteDTO> response = new ApiResponse<>(
            true,
            "Cliente creado exitosamente",
            nuevoCliente
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/clientes-proveedores/clientes/{id}
     * Actualizar un cliente existente
     */
    @PutMapping("/clientes/{id}")
    public ResponseEntity<ApiResponse<ClienteDTO>> actualizarCliente(
            @PathVariable Long id, 
            @Valid @RequestBody ClienteDTO clienteDTO) {
        log.info("Solicitud para actualizar cliente con ID: {}", id);
        
        ClienteDTO clienteActualizado = clientesProveedoresService.actualizarCliente(id, clienteDTO);
        
        ApiResponse<ClienteDTO> response = new ApiResponse<>(
            true,
            "Cliente actualizado exitosamente",
            clienteActualizado
        );
        
        return ResponseEntity.ok(response);
    }

    // ========== GESTIÓN DE PROVEEDORES ==========

    /**
     * GET /api/clientes-proveedores/proveedores
     * Obtener todos los proveedores activos
     */
    @GetMapping("/proveedores")
    public ResponseEntity<ApiResponse<List<ProveedorDTO>>> obtenerTodosLosProveedores() {
        log.info("Solicitud para obtener todos los proveedores");
        
        List<ProveedorDTO> proveedores = clientesProveedoresService.obtenerTodosLosProveedores();
        
        ApiResponse<List<ProveedorDTO>> response = new ApiResponse<>(
            true,
            "Proveedores obtenidos exitosamente",
            proveedores
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/clientes-proveedores/proveedores/{id}
     * Obtener proveedor por ID
     */
    @GetMapping("/proveedores/{id}")
    public ResponseEntity<ApiResponse<ProveedorDTO>> obtenerProveedorPorId(@PathVariable Long id) {
        log.info("Solicitud para obtener proveedor con ID: {}", id);
        
        ProveedorDTO proveedor = clientesProveedoresService.obtenerProveedorPorId(id);
        
        ApiResponse<ProveedorDTO> response = new ApiResponse<>(
            true,
            "Proveedor encontrado",
            proveedor
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/clientes-proveedores/proveedores
     * Crear un nuevo proveedor
     */
    @PostMapping("/proveedores")
    public ResponseEntity<ApiResponse<ProveedorDTO>> crearProveedor(@Valid @RequestBody ProveedorDTO proveedorDTO) {
        log.info("Solicitud para crear nuevo proveedor con código: {}", proveedorDTO.getCodigo());
        
        ProveedorDTO nuevoProveedor = clientesProveedoresService.crearProveedor(proveedorDTO);
        
        ApiResponse<ProveedorDTO> response = new ApiResponse<>(
            true,
            "Proveedor creado exitosamente",
            nuevoProveedor
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/clientes-proveedores/proveedores/{id}
     * Actualizar un proveedor existente
     */
    @PutMapping("/proveedores/{id}")
    public ResponseEntity<ApiResponse<ProveedorDTO>> actualizarProveedor(
            @PathVariable Long id, 
            @Valid @RequestBody ProveedorDTO proveedorDTO) {
        log.info("Solicitud para actualizar proveedor con ID: {}", id);
        
        ProveedorDTO proveedorActualizado = clientesProveedoresService.actualizarProveedor(id, proveedorDTO);
        
        ApiResponse<ProveedorDTO> response = new ApiResponse<>(
            true,
            "Proveedor actualizado exitosamente",
            proveedorActualizado
        );
        
        return ResponseEntity.ok(response);
    }

    // ========== CUENTAS POR COBRAR ==========

    /**
     * GET /api/clientes-proveedores/cuentas-cobrar/cliente/{id}
     * Obtener cuentas por cobrar de un cliente específico
     */
    @GetMapping("/cuentas-cobrar/cliente/{id}")
    public ResponseEntity<ApiResponse<List<CuentaPorCobrarDTO>>> obtenerCuentasPorCobrarPorCliente(
            @PathVariable Long id) {
        log.info("Solicitud para obtener cuentas por cobrar del cliente ID: {}", id);
        
        List<CuentaPorCobrarDTO> cuentas = clientesProveedoresService.obtenerCuentasPorCobrarPorCliente(id);
        
        ApiResponse<List<CuentaPorCobrarDTO>> response = new ApiResponse<>(
            true,
            "Cuentas por cobrar del cliente obtenidas exitosamente",
            cuentas
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/clientes-proveedores/cuentas-cobrar/vencidas
     * Obtener cuentas por cobrar vencidas
     */
    @GetMapping("/cuentas-cobrar/vencidas")
    public ResponseEntity<ApiResponse<List<CuentaPorCobrarDTO>>> obtenerCuentasPorCobrarVencidas() {
        log.info("Solicitud para obtener cuentas por cobrar vencidas");
        
        List<CuentaPorCobrarDTO> cuentas = clientesProveedoresService.obtenerCuentasPorCobrarVencidas();
        
        ApiResponse<List<CuentaPorCobrarDTO>> response = new ApiResponse<>(
            true,
            "Cuentas por cobrar vencidas obtenidas exitosamente",
            cuentas
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/clientes-proveedores/cuentas-cobrar
     * Crear una nueva cuenta por cobrar
     */
    @PostMapping("/cuentas-cobrar")
    public ResponseEntity<ApiResponse<CuentaPorCobrarDTO>> crearCuentaPorCobrar(
            @Valid @RequestBody CuentaPorCobrarDTO cuentaDTO) {
        log.info("Solicitud para crear cuenta por cobrar - Cliente ID: {}, Monto: {}", 
                cuentaDTO.getClienteId(), cuentaDTO.getMontoOriginal());
        
        CuentaPorCobrarDTO nuevaCuenta = clientesProveedoresService.crearCuentaPorCobrar(cuentaDTO);
        
        ApiResponse<CuentaPorCobrarDTO> response = new ApiResponse<>(
            true,
            "Cuenta por cobrar creada exitosamente",
            nuevaCuenta
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ========== CUENTAS POR PAGAR ==========

    /**
     * GET /api/clientes-proveedores/cuentas-pagar/vencidas
     * Obtener cuentas por pagar vencidas
     */
    @GetMapping("/cuentas-pagar/vencidas")
    public ResponseEntity<ApiResponse<List<CuentaPorPagarDTO>>> obtenerCuentasPorPagarVencidas() {
        log.info("Solicitud para obtener cuentas por pagar vencidas");
        
        List<CuentaPorPagarDTO> cuentas = clientesProveedoresService.obtenerCuentasPorPagarVencidas();
        
        ApiResponse<List<CuentaPorPagarDTO>> response = new ApiResponse<>(
            true,
            "Cuentas por pagar vencidas obtenidas exitosamente",
            cuentas
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/clientes-proveedores/cuentas-pagar
     * Crear una nueva cuenta por pagar
     */
    @PostMapping("/cuentas-pagar")
    public ResponseEntity<ApiResponse<CuentaPorPagarDTO>> crearCuentaPorPagar(
            @Valid @RequestBody CuentaPorPagarDTO cuentaDTO) {
        log.info("Solicitud para crear cuenta por pagar - Proveedor ID: {}, Monto: {}", 
                cuentaDTO.getProveedorId(), cuentaDTO.getMontoOriginal());
        
        CuentaPorPagarDTO nuevaCuenta = clientesProveedoresService.crearCuentaPorPagar(cuentaDTO);
        
        ApiResponse<CuentaPorPagarDTO> response = new ApiResponse<>(
            true,
            "Cuenta por pagar creada exitosamente",
            nuevaCuenta
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ========== REPORTES ==========

    /**
     * GET /api/clientes-proveedores/resumen
     * Generar resumen general de clientes y proveedores
     */
    @GetMapping("/resumen")
    public ResponseEntity<ApiResponse<ResumenClientesProveedoresDTO>> generarResumen() {
        log.info("Solicitud para generar resumen de clientes y proveedores");
        
        ResumenClientesProveedoresDTO resumen = clientesProveedoresService.generarResumen();
        
        ApiResponse<ResumenClientesProveedoresDTO> response = new ApiResponse<>(
            true,
            "Resumen de clientes y proveedores generado exitosamente",
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