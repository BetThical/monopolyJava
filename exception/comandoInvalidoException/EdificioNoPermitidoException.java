package exception.comandoInvalidoException;

// EdificioNoPermitidoException: Uso del comando 'edificar' cuando no se puede construir el edificio (no tienes suficiente dinero, no puedes construir en esa casilla...)
public class EdificioNoPermitidoException extends ComandoInvalidoException {
    
    public EdificioNoPermitidoException(String Motivo) {
        super("No puedes construir ese edificio. (" + Motivo + ").");
    }
}
