package exception.noEncontradoException;

public class TratoNoEncontradoException extends NoEncontradoException {
    public TratoNoEncontradoException(String nombre) {
        super("el trato", nombre);
    }

}
