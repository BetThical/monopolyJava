package exception.comandoInvalidoException;

// AcabarTurnoException: Uso del comando 'acabar turno' cuando se debe tomar otra acción antes: pagar una deuda, lanzar los dados, coger una carta...
public class AcabarTurnoException extends ComandoInvalidoException {
    public AcabarTurnoException(String motivo) {
        super("No puedes acabar el turno en este momento. (" + motivo + ").");
    }
}