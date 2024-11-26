package exception.comandoInvalidoException;

public class AvanzarException extends ComandoInvalidoException {
    public AvanzarException(String motivo) {
        super("No puedes avanzar ahora mismo. (" + motivo + ").");
    }

}
