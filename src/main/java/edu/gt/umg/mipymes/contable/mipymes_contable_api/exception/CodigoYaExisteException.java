// ========== CodigoYaExisteException.java ==========
package edu.gt.umg.mipymes.contable.mipymes_contable_api.exception;

/**
 * Excepción cuando se intenta crear una cuenta con un código que ya existe
 */
public class CodigoYaExisteException extends RuntimeException {
    public CodigoYaExisteException(String message) {
        super(message);
    }
}
