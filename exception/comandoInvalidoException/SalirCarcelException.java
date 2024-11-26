package exception.comandoInvalidoException;

public class SalirCarcelException extends ComandoInvalidoException {
    public SalirCarcelException(String motivo) {
        super("No puedes pagar tu multa (" + motivo + ").");
    }
    
}
