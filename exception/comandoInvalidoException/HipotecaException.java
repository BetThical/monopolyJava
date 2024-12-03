package exception.comandoInvalidoException;

// HipotecaException: Uso de 'hipotecar' o 'deshipotecar' cuando no es posible realizar la acción.
public class HipotecaException extends ComandoInvalidoException {

    public HipotecaException(String acción, String motivo) {
        super("No puedes " + acción + " la casilla, " + motivo);
    }
}
