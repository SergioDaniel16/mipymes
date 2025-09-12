package edu.gt.umg.mipymes.contable.mipymes_contable_api.repository;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.Proveedor;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.Proveedor.TipoProveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    /**
     * Buscar proveedor por código
     */
    Optional<Proveedor> findByCodigo(String codigo);

    /**
     * Verificar si existe un proveedor con el código
     */
    boolean existsByCodigo(String codigo);

    /**
     * Obtener proveedores activos
     */
    List<Proveedor> findByActivoTrueOrderByNombre();

    /**
     * Buscar proveedores por nombre
     */
    List<Proveedor> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);

    /**
     * Obtener proveedores por tipo
     */
    List<Proveedor> findByTipoProveedorAndActivoTrueOrderByNombre(TipoProveedor tipoProveedor);

    /**
     * Obtener proveedores con saldo pendiente
     */
    @Query("SELECT p FROM Proveedor p WHERE p.saldoActual > 0 AND p.activo = true ORDER BY p.nombre")
    List<Proveedor> obtenerProveedoresConSaldo();

    /**
     * Obtener total de cuentas por pagar
     */
    @Query("SELECT SUM(p.saldoActual) FROM Proveedor p WHERE p.activo = true")
    BigDecimal obtenerTotalCuentasPorPagar();
}