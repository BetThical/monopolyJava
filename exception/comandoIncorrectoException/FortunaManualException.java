package exception.comandoIncorrectoException;

// FortunaManualException: Se lanza tras un uso incorrecto del comando 'f' con parámetros mal pasados o inexistentes.
public class FortunaManualException extends ComandoIncorrectoException{
    public FortunaManualException() {
        super("fortuna [jugador] [x]");
    }
}
