package exception.noEncontradoException;

public class CasillaNoEncontradaException extends NoEncontradoException {
    public CasillaNoEncontradaException(String nombre) {
        super("la casilla", nombre);
    }
    
}
