package exception.comandoInvalidoException;

// SalirCarcelException: Intento de salir de la cárcel cuando no se puede pagar la multa (no tienes suficiente dinero, no estás en la cárcel...)
public class SalirCarcelException extends ComandoInvalidoException {

    public SalirCarcelException(String motivo) {
        super("No puedes pagar tu multa (" + motivo + ").");
    }

}
