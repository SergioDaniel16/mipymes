package edu.gt.umg.mipymes.contable.mipymes_contable_api.repository;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Buscar producto por código
     */
    Optional<Producto> findByCodigo(String codigo);

    /**
     * Verificar si existe un producto con el código
     */
    boolean existsByCodigo(String codigo);

    /**
     * Obtener productos activos ordenados por nombre
     */
    List<Producto> findByActivoTrueOrderByNombre();

    /**
     * Buscar productos por nombre (búsqueda parcial)
     */
    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);

    /**
     * Buscar productos por categoría
     */
    List<Producto> findByCategoriaAndActivoTrueOrderByNombre(String categoria);

    /**
     * Obtener productos con stock mínimo
     */
    @Query("SELECT p FROM Producto p WHERE p.existencia <= p.stockMinimo AND p.activo = true ORDER BY p.nombre")
    List<Producto> obtenerProductosEnStockMinimo();

    /**
     * Obtener productos con existencia
     */
    @Query("SELECT p FROM Producto p WHERE p.existencia > 0 AND p.activo = true ORDER BY p.nombre")
    List<Producto> obtenerProductosConExistencia();

    /**
     * Obtener valor total del inventario
     */
    @Query("SELECT SUM(p.precioCompra * p.existencia) FROM Producto p WHERE p.activo = true")
    java.math.BigDecimal obtenerValorTotalInventario();

    /**
     * Contar productos por categoría
     */
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.categoria = :categoria AND p.activo = true")
    Long contarProductosPorCategoria(@Param("categoria") String categoria);

    /**
     * Obtener todas las categorías
     */
    @Query("SELECT DISTINCT p.categoria FROM Producto p WHERE p.categoria IS NOT NULL AND p.activo = true ORDER BY p.categoria")
    List<String> obtenerCategorias();
}