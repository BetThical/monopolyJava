package exception.noEncontradoException;

public class JugadorNoEncontradoException extends NoEncontradoException {
    public JugadorNoEncontradoException(String nombre) {
        super("el jugador", nombre);
    }
    
}
