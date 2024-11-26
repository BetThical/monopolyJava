package exception.comandoIncorrectoException;

public class EdificioNoValidoException extends ComandoIncorrectoException {
    public EdificioNoValidoException() {
        super("[edificar/destruir] [casa/hotel/piscina/pista]");
    }

}
