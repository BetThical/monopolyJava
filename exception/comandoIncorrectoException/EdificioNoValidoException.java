package exception.comandoIncorrectoException;

// EdificioNoValidoException: Uso incorrecto del comando 'edificar/destruir' con parámetros mal pasados o inexistentes.
public class EdificioNoValidoException extends ComandoIncorrectoException {
    public EdificioNoValidoException() {
        super("[edificar/destruir] [casa/hotel/piscina/pista]");
    }

}
