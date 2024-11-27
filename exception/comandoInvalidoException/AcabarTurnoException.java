package exception.comandoInvalidoException;

public class AcabarTurnoException extends ComandoInvalidoException {
    public AcabarTurnoException(String motivo) {
        super("No puedes acabar el turno en este momento. (" + motivo + ").");
    }
}