package exception.comandoInvalidoException;

// NumeroJugadoresException: Uso de 'añadir jugador' cuando ya hay 6 jugadores.
public class NumeroJugadoresException extends ComandoInvalidoException {
    public NumeroJugadoresException() {
        super("El número de jugadores debe ser entre 2 y 6.");
    }
    
}
