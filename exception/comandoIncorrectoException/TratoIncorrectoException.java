package exception.comandoIncorrectoException;

// TratoIncorrectoException: Uso incorrecto del comando 'trato' con parámetros mal pasados o inexistentes.
public class TratoIncorrectoException extends ComandoIncorrectoException {

    public TratoIncorrectoException() {
        super("trato [jugador]: cambiar (casilla y dinero, casilla y dinero. \nAviso: no puedes cambiar dinero por dinero.");
    }
}
