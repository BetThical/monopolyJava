package exception;

public class ListarIncorrectoException extends ComandoException {
    public ListarIncorrectoException() {
        super("Uso: listar [jugadores|edificios|avatares|venta]");
    }
}
