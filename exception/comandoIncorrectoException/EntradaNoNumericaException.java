package exception.comandoIncorrectoException;

public class EntradaNoNumericaException extends ComandoIncorrectoException {

    public EntradaNoNumericaException() {
        super("Introduzca un número válido.");
    }
}
