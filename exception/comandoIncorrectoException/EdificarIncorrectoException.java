package exception.comandoIncorrectoException;

// EdificioNoValidoException: Uso incorrecto del comando 'edificar/destruir' con parámetros mal pasados o inexistentes.
public class EdificarIncorrectoException extends ComandoIncorrectoException {
    public EdificarIncorrectoException() {
        super("[edificar/destruir] [casa/hotel/piscina/pista]");
    }

}
