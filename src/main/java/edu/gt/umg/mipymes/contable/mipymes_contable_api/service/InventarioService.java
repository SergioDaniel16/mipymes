package edu.gt.umg.mipymes.contable.mipymes_contable_api.service;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.dto.*;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.*;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.repository.*;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para el manejo de Inventario
 * - Gestión de productos
 * - Control de movimientos de inventario
 * - Reportes de inventario
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class InventarioService {

    private final ProductoRepository productoRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;

    // ========== GESTIÓN DE PRODUCTOS ==========

    /**
     * Obtener todos los productos activos
     */
    public List<ProductoDTO> obtenerTodosLosProductos() {
        log.info("Obteniendo todos los productos activos");
        List<Producto> productos = productoRepository.findByActivoTrueOrderByNombre();
        return convertirProductosADTO(productos);
    }

    /**
     * Obtener producto por ID
     */
    public ProductoDTO obtenerProductoPorId(Long id) {
        log.info("Buscando producto con ID: {}", id);
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new CuentaNotFoundException("Producto no encontrado con ID: " + id));
        return convertirProductoADTO(producto);
    }

    /**
     * Obtener producto por código
     */
    public ProductoDTO obtenerProductoPorCodigo(String codigo) {
        log.info("Buscando producto con código: {}", codigo);
        Producto producto = productoRepository.findByCodigo(codigo)
            .orElseThrow(() -> new CuentaNotFoundException("Producto no encontrado con código: " + codigo));
        return convertirProductoADTO(producto);
    }

    /**
     * Crear un nuevo producto
     */
    @Transactional
    public ProductoDTO crearProducto(ProductoDTO productoDTO) {
        log.info("Creando nuevo producto con código: {}", productoDTO.getCodigo());

        // Validar que el código no exista
        if (productoRepository.existsByCodigo(productoDTO.getCodigo())) {
            throw new CodigoYaExisteException("Ya existe un producto con código: " + productoDTO.getCodigo());
        }

        // Convertir a entidad y guardar
        Producto producto = convertirDTOAProducto(productoDTO);
        Producto productoGuardado = productoRepository.save(producto);

        log.info("Producto creado exitosamente con ID: {}", productoGuardado.getId());
        return convertirProductoADTO(productoGuardado);
    }

    /**
     * Actualizar un producto existente
     */
    @Transactional
    public ProductoDTO actualizarProducto(Long id, ProductoDTO productoDTO) {
        log.info("Actualizando producto con ID: {}", id);

        Producto productoExistente = productoRepository.findById(id)
            .orElseThrow(() -> new CuentaNotFoundException("Producto no encontrado con ID: " + id));

        // Validar código único
        if (!productoExistente.getCodigo().equals(productoDTO.getCodigo()) && 
            productoRepository.existsByCodigo(productoDTO.getCodigo())) {
            throw new CodigoYaExisteException("Ya existe un producto con código: " + productoDTO.getCodigo());
        }

        // Actualizar campos
        actualizarCamposProducto(productoExistente, productoDTO);
        Producto productoActualizado = productoRepository.save(productoExistente);

        log.info("Producto actualizado exitosamente");
        return convertirProductoADTO(productoActualizado);
    }

    /**
     * Buscar productos por nombre
     */
    public List<ProductoDTO> buscarProductosPorNombre(String nombre) {
        log.info("Buscando productos que contengan: {}", nombre);
        List<Producto> productos = productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre);
        return convertirProductosADTO(productos);
    }

    /**
     * Obtener productos en stock mínimo
     */
    public List<ProductoDTO> obtenerProductosEnStockMinimo() {
        log.info("Obteniendo productos en stock mínimo");
        List<Producto> productos = productoRepository.obtenerProductosEnStockMinimo();
        return convertirProductosADTO(productos);
    }

    // ========== MOVIMIENTOS DE INVENTARIO ==========

    /**
     * Registrar movimiento de inventario
     */
    @Transactional
    public MovimientoInventarioDTO registrarMovimiento(CrearMovimientoInventarioDTO movimientoDTO) {
        log.info("Registrando movimiento de inventario para producto ID: {}", movimientoDTO.getProductoId());

        // Obtener producto
        Producto producto = productoRepository.findById(movimientoDTO.getProductoId())
            .orElseThrow(() -> new CuentaNotFoundException("Producto no encontrado con ID: " + movimientoDTO.getProductoId()));

        // Validar existencia para salidas
        if (movimientoDTO.getTipoMovimiento().esSalida() && 
            producto.getExistencia() < movimientoDTO.getCantidad()) {
            throw new IllegalArgumentException("No hay suficiente existencia. Disponible: " + producto.getExistencia());
        }

        // Crear movimiento
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setTipoMovimiento(movimientoDTO.getTipoMovimiento());
        movimiento.setCantidad(movimientoDTO.getCantidad());
        movimiento.setPrecioUnitario(movimientoDTO.getPrecioUnitario());
        movimiento.setExistenciaAnterior(producto.getExistencia());
        movimiento.setObservaciones(movimientoDTO.getObservaciones());
        movimiento.setNumeroDocumento(movimientoDTO.getNumeroDocumento());
        movimiento.setCreadoPor(movimientoDTO.getCreadoPor());

        // Actualizar existencia del producto
        Integer nuevaExistencia;
        if (movimientoDTO.getTipoMovimiento().esEntrada()) {
            nuevaExistencia = producto.getExistencia() + movimientoDTO.getCantidad();
        } else {
            nuevaExistencia = producto.getExistencia() - movimientoDTO.getCantidad();
        }

        movimiento.setExistenciaNueva(nuevaExistencia);
        producto.setExistencia(nuevaExistencia);

        // Guardar movimiento y actualizar producto
        MovimientoInventario movimientoGuardado = movimientoInventarioRepository.save(movimiento);
        productoRepository.save(producto);

        log.info("Movimiento registrado exitosamente. Nueva existencia: {}", nuevaExistencia);
        return convertirMovimientoADTO(movimientoGuardado);
    }

    /**
     * Obtener movimientos por producto
     */
    public List<MovimientoInventarioDTO> obtenerMovimientosPorProducto(Long productoId) {
        log.info("Obteniendo movimientos para producto ID: {}", productoId);
        List<MovimientoInventario> movimientos = movimientoInventarioRepository.findByProductoIdOrderByFechaMovimientoDesc(productoId);
        return convertirMovimientosADTO(movimientos);
    }

    /**
     * Obtener últimos movimientos
     */
    public List<MovimientoInventarioDTO> obtenerUltimosMovimientos() {
        log.info("Obteniendo últimos movimientos de inventario");
        List<MovimientoInventario> movimientos = movimientoInventarioRepository.obtenerUltimosMovimientos();
        return convertirMovimientosADTO(movimientos);
    }

    // ========== REPORTES ==========

    /**
     * Generar reporte de inventario
     */
    public ReporteInventarioDTO generarReporteInventario() {
        log.info("Generando reporte de inventario");

        List<Producto> todosLosProductos = productoRepository.findByActivoTrueOrderByNombre();
        List<Producto> productosStockMinimo = productoRepository.obtenerProductosEnStockMinimo();
        BigDecimal valorTotal = productoRepository.obtenerValorTotalInventario();

        ReporteInventarioDTO reporte = new ReporteInventarioDTO();
        reporte.setFechaCorte(LocalDate.now());
        reporte.setEmpresa("Almacén El Planeador - Horacio Porras");
        reporte.setProductos(convertirProductosADTO(todosLosProductos));
        reporte.setTotalProductos(todosLosProductos.size());
        reporte.setProductosEnStockMinimo(productosStockMinimo.size());
        reporte.setValorTotalInventario(valorTotal != null ? valorTotal : BigDecimal.ZERO);
        reporte.setProductosStockMinimo(convertirProductosADTO(productosStockMinimo));

        return reporte;
    }

    // ========== MÉTODOS AUXILIARES ==========

    private List<ProductoDTO> convertirProductosADTO(List<Producto> productos) {
        return productos.stream()
            .map(this::convertirProductoADTO)
            .collect(Collectors.toList());
    }

    private ProductoDTO convertirProductoADTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setCodigo(producto.getCodigo());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecioCompra(producto.getPrecioCompra());
        dto.setPrecioVenta(producto.getPrecioVenta());
        dto.setExistencia(producto.getExistencia());
        dto.setStockMinimo(producto.getStockMinimo());
        dto.setUnidadMedida(producto.getUnidadMedida());
        dto.setActivo(producto.getActivo());
        dto.setCategoria(producto.getCategoria());
        dto.setProveedor(producto.getProveedor());
        dto.setFechaCreacion(producto.getFechaCreacion());
        dto.setFechaModificacion(producto.getFechaModificacion());
        
        dto.calcularCampos(); // Calcular campos derivados
        return dto;
    }

    private Producto convertirDTOAProducto(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setCodigo(dto.getCodigo());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecioCompra(dto.getPrecioCompra());
        producto.setPrecioVenta(dto.getPrecioVenta());
        producto.setExistencia(dto.getExistencia() != null ? dto.getExistencia() : 0);
        producto.setStockMinimo(dto.getStockMinimo() != null ? dto.getStockMinimo() : 0);
        producto.setUnidadMedida(dto.getUnidadMedida());
        producto.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        producto.setCategoria(dto.getCategoria());
        producto.setProveedor(dto.getProveedor());
        return producto;
    }

    private void actualizarCamposProducto(Producto producto, ProductoDTO dto) {
        producto.setCodigo(dto.getCodigo());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecioCompra(dto.getPrecioCompra());
        producto.setPrecioVenta(dto.getPrecioVenta());
        producto.setStockMinimo(dto.getStockMinimo());
        producto.setUnidadMedida(dto.getUnidadMedida());
        producto.setActivo(dto.getActivo());
        producto.setCategoria(dto.getCategoria());
        producto.setProveedor(dto.getProveedor());
    }

    private List<MovimientoInventarioDTO> convertirMovimientosADTO(List<MovimientoInventario> movimientos) {
        return movimientos.stream()
            .map(this::convertirMovimientoADTO)
            .collect(Collectors.toList());
    }

    private MovimientoInventarioDTO convertirMovimientoADTO(MovimientoInventario movimiento) {
        MovimientoInventarioDTO dto = new MovimientoInventarioDTO();
        dto.setId(movimiento.getId());
        dto.setProductoId(movimiento.getProducto().getId());
        dto.setProductoNombre(movimiento.getProducto().getNombre());
        dto.setProductoCodigo(movimiento.getProducto().getCodigo());
        dto.setTipoMovimiento(movimiento.getTipoMovimiento());
        dto.setCantidad(movimiento.getCantidad());
        dto.setPrecioUnitario(movimiento.getPrecioUnitario());
        dto.setExistenciaAnterior(movimiento.getExistenciaAnterior());
        dto.setExistenciaNueva(movimiento.getExistenciaNueva());
        dto.setObservaciones(movimiento.getObservaciones());
        dto.setNumeroDocumento(movimiento.getNumeroDocumento());
        dto.setFechaMovimiento(movimiento.getFechaMovimiento());
        dto.setCreadoPor(movimiento.getCreadoPor());
        
        dto.calcularCampos(); // Calcular campos derivados
        return dto;
    }
}