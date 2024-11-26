package exception.comandoIncorrectoException;

public class ListarIncorrectoException extends ComandoIncorrectoException {
    public ListarIncorrectoException() {
        super("listar [jugadores|edificios|avatares|venta]");
    }
}
