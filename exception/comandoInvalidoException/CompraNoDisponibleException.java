package exception.comandoInvalidoException;

public class CompraNoDisponibleException extends ComandoInvalidoException {

    public CompraNoDisponibleException(String Motivo) {
        super("No puedes comprar esta casilla. (" + Motivo + ").");
    }
}
