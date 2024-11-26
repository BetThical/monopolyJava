package exception.comandoIncorrectoException;

public class FortunaManualException extends ComandoIncorrectoException{
    public FortunaManualException() {
        super("fortuna [jugador] [x]");
    }
}
