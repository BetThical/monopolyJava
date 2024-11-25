package exception;

public class CambiarModoException extends ComandoException {
    public CambiarModoException() {
        super("Sólo puedes cambiar de modo antes de lanzar los dados.");
    }

}
