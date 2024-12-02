package exception.comandoIncorrectoException;

// EntradaNoNumericaException: fallo al intentar pasar a int una entrada que no es numérica.
public class EntradaNoNumericaException extends ComandoIncorrectoException {

    public EntradaNoNumericaException() {
        super("Introduzca un número válido.");
    }
}
