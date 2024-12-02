package exception.comandoIncorrectoException;

// ListarIncorrectoException: Uso incorrecto del comando 'listar' con parámetros mal pasados o inexistentes.
public class ListarIncorrectoException extends ComandoIncorrectoException {
    public ListarIncorrectoException() {
        super("listar [jugadores|edificios|avatares|venta]");
    }
}
