package exception.comandoInvalidoException;

// FondosInsuficientesException: Intento de realizar una acción que requiere más dinero del que se tiene.
public class FondosInsuficientesException extends ComandoInvalidoException {

    public FondosInsuficientesException() {
        super("No tienes suficiente dinero para realizar esta acción.");
    }
}
