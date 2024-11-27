package exception.comandoInvalidoException;

public class EdificioNoPermitidoException extends ComandoInvalidoException {
    public EdificioNoPermitidoException(String Motivo) {
        super("No puedes construir ese edificio. (" + Motivo + ").");
    }
}
