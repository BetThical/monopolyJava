package exception.comandoInvalidoException;

public class TratoInvalidoException extends ComandoInvalidoException {
    public TratoInvalidoException(String motivo) {
        super("No se puede realizar el trato (" + motivo + ").");
    }
}
