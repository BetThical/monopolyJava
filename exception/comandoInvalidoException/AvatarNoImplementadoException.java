package exception.comandoInvalidoException;

public class AvatarNoImplementadoException extends ComandoInvalidoException {
    public AvatarNoImplementadoException(String nombreAvatar) {
        super("El avatar " + nombreAvatar + " no está implementado.");
    }
    
}
