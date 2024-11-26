package exception.comandoInvalidoException;

public class FondosInsuficientesException extends ComandoInvalidoException{
    public FondosInsuficientesException() {
        super("No tienes suficiente dinero para realizar esta acción.");
    }
}
