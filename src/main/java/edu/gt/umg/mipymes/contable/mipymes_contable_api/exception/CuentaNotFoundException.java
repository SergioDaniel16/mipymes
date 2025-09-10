
// ========== CuentaNotFoundException.java ==========
package edu.gt.umg.mipymes.contable.mipymes_contable_api.exception;

/**
 * Excepción cuando no se encuentra una cuenta
 */
public class CuentaNotFoundException extends RuntimeException {
    public CuentaNotFoundException(String message) {
        super(message);
    }
}
