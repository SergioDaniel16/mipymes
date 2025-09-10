package edu.gt.umg.mipymes.contable.mipymes_contable_api.repository;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.Cuenta;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.Cuenta.TipoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operaciones de base de datos de Cuentas
 * JpaRepository nos da métodos básicos como save(), findById(), findAll(), delete()
 */
@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    /**
     * Buscar cuenta por código único
     * Spring Data JPA genera automáticamente la consulta SQL
     */
    Optional<Cuenta> findByCodigo(String codigo);

    /**
     * Buscar cuentas por tipo (ACTIVO, PASIVO, etc.)
     */
    List<Cuenta> findByTipo(TipoCuenta tipo);

    /**
     * Buscar cuentas por tipo y que estén activas
     */
    List<Cuenta> findByTipoAndActivaTrue(TipoCuenta tipo);

    /**
     * Buscar cuentas activas ordenadas por código
     */
    List<Cuenta> findByActivaTrueOrderByCodigo();

    /**
     * Buscar cuentas por nombre (búsqueda parcial, ignora mayúsculas/minúsculas)
     */
    List<Cuenta> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Verificar si existe una cuenta con un código específico
     */
    boolean existsByCodigo(String codigo);

    /**
     * Consulta personalizada: obtener el catálogo de cuentas completo
     * Esta consulta usa JPQL (similar a SQL pero orientado a objetos)
     */
    @Query("SELECT c FROM Cuenta c WHERE c.activa = true ORDER BY c.codigo")
    List<Cuenta> obtenerCatalogoActivo();

    /**
     * Consulta nativa SQL: obtener resumen por tipo de cuenta
     * Útil para reportes
     */
    @Query(value = """
        SELECT 
            tipo,
            COUNT(*) as total_cuentas,
            SUM(saldo) as saldo_total
        FROM cuentas 
        WHERE activa = true 
        GROUP BY tipo 
        ORDER BY tipo
        """, nativeQuery = true)
    List<Object[]> obtenerResumenPorTipo();

    /**
     * Buscar cuentas con saldo diferente de cero
     */
    @Query("SELECT c FROM Cuenta c WHERE c.saldo <> 0 AND c.activa = true ORDER BY c.codigo")
    List<Cuenta> obtenerCuentasConSaldo();
}