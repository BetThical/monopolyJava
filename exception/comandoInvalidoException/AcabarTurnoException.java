package exception.comandoInvalidoException;

public class AcabarTurnoException extends ComandoInvalidoException {
    public AcabarTurnoException() {
        super("No puedes acabar turno por estar en deuda");
    }
}