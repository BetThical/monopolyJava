package exception.comandoInvalidoException;

// CambiarModoException: Uso del comando 'cambiar modo' cuando no se puede cambiar de modo (ya has lanzado los dados)
public class CambiarModoException extends ComandoInvalidoException {
    public CambiarModoException() {
        super("Sólo puedes cambiar de modo antes de lanzar los dados.");
    }

}
