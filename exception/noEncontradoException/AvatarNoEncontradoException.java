package exception.noEncontradoException;

public class AvatarNoEncontradoException extends NoEncontradoException {
    public AvatarNoEncontradoException(String nombre) {
        super("el avatar", nombre);
    }
    
}
