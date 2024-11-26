package exception.noEncontradoException;

public class GrupoNoEncontradoException extends NoEncontradoException{
    public GrupoNoEncontradoException(String nombre) {
        super("el grupo", nombre);
    }
}
