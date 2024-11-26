package exception.comandoInvalidoException;

public class CambiarModoException extends ComandoInvalidoException {
    public CambiarModoException() {
        super("Sólo puedes cambiar de modo antes de lanzar los dados.");
    }

}
