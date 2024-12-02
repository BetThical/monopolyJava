package exception.comandoIncorrectoException;

// DescribirIncorrectoException: Uso incorrecto del comando 'describir' con parámetros mal pasados o inexistentes.
public class DescribirIncorrectoException extends ComandoIncorrectoException {

    public DescribirIncorrectoException() {
        super("describir [casilla], describir jugador [jugador], describir avatar [avatar]");

    }

}
