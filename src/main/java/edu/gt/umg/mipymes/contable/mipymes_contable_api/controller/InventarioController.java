package edu.gt.umg.mipymes.contable.mipymes_contable_api.controller;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.dto.*;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.service.InventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para el Módulo de Inventario
 * 
 * Endpoints disponibles:
 * 
 * PRODUCTOS:
 * GET    /api/inventario/productos                - Listar productos
 * GET    /api/inventario/productos/{id}           - Obtener producto por ID
 * GET    /api/inventario/productos/codigo/{codigo} - Obtener producto por código
 * GET    /api/inventario/productos/buscar         - Buscar productos por nombre
 * GET    /api/inventario/productos/stock-minimo   - Productos en stock mínimo
 * POST   /api/inventario/productos                - Crear producto
 * PUT    /api/inventario/productos/{id}           - Actualizar producto
 * 
 * MOVIMIENTOS:
 * GET    /api/inventario/movimientos              - Últimos movimientos
 * GET    /api/inventario/movimientos/producto/{id} - Movimientos por producto
 * POST   /api/inventario/movimientos              - Registrar movimiento
 * 
 * REPORTES:
 * GET    /api/inventario/reporte                  - Reporte de inventario
 */
@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
@Slf4j
public class InventarioController {

    private final InventarioService inventarioService;

    // ========== GESTIÓN DE PRODUCTOS ==========

    /**
     * GET /api/inventario/productos
     * Obtener todos los productos activos
     */
    @GetMapping("/productos")
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> obtenerTodosLosProductos() {
        log.info("Solicitud para obtener todos los productos");
        
        List<ProductoDTO> productos = inventarioService.obtenerTodosLosProductos();
        
        ApiResponse<List<ProductoDTO>> response = new ApiResponse<>(
            true,
            "Productos obtenidos exitosamente",
            productos
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/inventario/productos/{id}
     * Obtener producto por ID
     */
    @GetMapping("/productos/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> obtenerProductoPorId(@PathVariable Long id) {
        log.info("Solicitud para obtener producto con ID: {}", id);
        
        ProductoDTO producto = inventarioService.obtenerProductoPorId(id);
        
        ApiResponse<ProductoDTO> response = new ApiResponse<>(
            true,
            "Producto encontrado",
            producto
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/inventario/productos/codigo/{codigo}
     * Obtener producto por código
     */
    @GetMapping("/productos/codigo/{codigo}")
    public ResponseEntity<ApiResponse<ProductoDTO>> obtenerProductoPorCodigo(@PathVariable String codigo) {
        log.info("Solicitud para obtener producto con código: {}", codigo);
        
        ProductoDTO producto = inventarioService.obtenerProductoPorCodigo(codigo);
        
        ApiResponse<ProductoDTO> response = new ApiResponse<>(
            true,
            "Producto encontrado",
            producto
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/inventario/productos/buscar?nombre=producto
     * Buscar productos por nombre
     */
    @GetMapping("/productos/buscar")
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> buscarProductosPorNombre(
            @RequestParam String nombre) {
        log.info("Solicitud para buscar productos que contengan: {}", nombre);
        
        List<ProductoDTO> productos = inventarioService.buscarProductosPorNombre(nombre);
        
        ApiResponse<List<ProductoDTO>> response = new ApiResponse<>(
            true,
            "Búsqueda completada",
            productos
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/inventario/productos/stock-minimo
     * Obtener productos en stock mínimo
     */
    @GetMapping("/productos/stock-minimo")
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> obtenerProductosEnStockMinimo() {
        log.info("Solicitud para obtener productos en stock mínimo");
        
        List<ProductoDTO> productos = inventarioService.obtenerProductosEnStockMinimo();
        
        ApiResponse<List<ProductoDTO>> response = new ApiResponse<>(
            true,
            "Productos en stock mínimo obtenidos exitosamente",
            productos
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/inventario/productos
     * Crear un nuevo producto
     */
    @PostMapping("/productos")
    public ResponseEntity<ApiResponse<ProductoDTO>> crearProducto(@Valid @RequestBody ProductoDTO productoDTO) {
        log.info("Solicitud para crear nuevo producto con código: {}", productoDTO.getCodigo());
        
        ProductoDTO nuevoProducto = inventarioService.crearProducto(productoDTO);
        
        ApiResponse<ProductoDTO> response = new ApiResponse<>(
            true,
            "Producto creado exitosamente",
            nuevoProducto
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/inventario/productos/{id}
     * Actualizar un producto existente
     */
    @PutMapping("/productos/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> actualizarProducto(
            @PathVariable Long id, 
            @Valid @RequestBody ProductoDTO productoDTO) {
        log.info("Solicitud para actualizar producto con ID: {}", id);
        
        ProductoDTO productoActualizado = inventarioService.actualizarProducto(id, productoDTO);
        
        ApiResponse<ProductoDTO> response = new ApiResponse<>(
            true,
            "Producto actualizado exitosamente",
            productoActualizado
        );
        
        return ResponseEntity.ok(response);
    }

    // ========== MOVIMIENTOS DE INVENTARIO ==========

    /**
     * GET /api/inventario/movimientos
     * Obtener últimos movimientos de inventario
     */
    @GetMapping("/movimientos")
    public ResponseEntity<ApiResponse<List<MovimientoInventarioDTO>>> obtenerUltimosMovimientos() {
        log.info("Solicitud para obtener últimos movimientos de inventario");
        
        List<MovimientoInventarioDTO> movimientos = inventarioService.obtenerUltimosMovimientos();
        
        ApiResponse<List<MovimientoInventarioDTO>> response = new ApiResponse<>(
            true,
            "Movimientos obtenidos exitosamente",
            movimientos
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/inventario/movimientos/producto/{id}
     * Obtener movimientos de un producto específico
     */
    @GetMapping("/movimientos/producto/{id}")
    public ResponseEntity<ApiResponse<List<MovimientoInventarioDTO>>> obtenerMovimientosPorProducto(
            @PathVariable Long id) {
        log.info("Solicitud para obtener movimientos del producto ID: {}", id);
        
        List<MovimientoInventarioDTO> movimientos = inventarioService.obtenerMovimientosPorProducto(id);
        
        ApiResponse<List<MovimientoInventarioDTO>> response = new ApiResponse<>(
            true,
            "Movimientos del producto obtenidos exitosamente",
            movimientos
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/inventario/movimientos
     * Registrar un nuevo movimiento de inventario
     */
    @PostMapping("/movimientos")
    public ResponseEntity<ApiResponse<MovimientoInventarioDTO>> registrarMovimiento(
            @Valid @RequestBody CrearMovimientoInventarioDTO movimientoDTO) {
        log.info("Solicitud para registrar movimiento de inventario - Producto ID: {}, Tipo: {}", 
                movimientoDTO.getProductoId(), movimientoDTO.getTipoMovimiento());
        
        MovimientoInventarioDTO movimiento = inventarioService.registrarMovimiento(movimientoDTO);
        
        ApiResponse<MovimientoInventarioDTO> response = new ApiResponse<>(
            true,
            "Movimiento de inventario registrado exitosamente",
            movimiento
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ========== REPORTES ==========

    /**
     * GET /api/inventario/reporte
     * Generar reporte completo de inventario
     */
    @GetMapping("/reporte")
    public ResponseEntity<ApiResponse<ReporteInventarioDTO>> generarReporteInventario() {
        log.info("Solicitud para generar reporte de inventario");
        
        ReporteInventarioDTO reporte = inventarioService.generarReporteInventario();
        
        ApiResponse<ReporteInventarioDTO> response = new ApiResponse<>(
            true,
            "Reporte de inventario generado exitosamente",
            reporte
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