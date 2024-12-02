package exception.comandoInvalidoException;

// AvanzarException: Uso del comando 'avanzar' cuando no se puede avanzar (no estás en modo pelota, no te quedan movimientos...)
public class AvanzarException extends ComandoInvalidoException {
    public AvanzarException(String motivo) {
        super("No puedes avanzar ahora mismo. (" + motivo + ").");
    }

}
