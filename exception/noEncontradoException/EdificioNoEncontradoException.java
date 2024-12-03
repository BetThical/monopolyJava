package exception.noEncontradoException;

public class EdificioNoEncontradoException extends NoEncontradoException {
    public EdificioNoEncontradoException(String nombre) {
        super("un edificio de tipo", nombre);
    }
    
}
