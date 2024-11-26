package exception.comandoInvalidoException;

public class NumeroJugadoresException extends ComandoInvalidoException {
    public NumeroJugadoresException() {
        super("El número de jugadores debe ser entre 2 y 6.");
    }
    
}
