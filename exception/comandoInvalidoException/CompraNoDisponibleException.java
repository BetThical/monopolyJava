package exception.comandoInvalidoException;

// Compra no disponible: uso del comando 'comprar' cuando no se puede comprar la casilla (ya tiene dueño, no es comprable, etc.)
public class CompraNoDisponibleException extends ComandoInvalidoException {

    public CompraNoDisponibleException(String Motivo) {
        super("No puedes comprar esta casilla. (" + Motivo + ").");
    }
}
