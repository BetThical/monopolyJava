package exception.comandoInvalidoException;

// DadosException: Uso del comando 'lanzar dados' cuando no se puede lanzar los dados (ya has lanzado los dados y no fueron dobles, debes pa)
public class DadosException extends ComandoInvalidoException {

    public DadosException(String motivo) {
        super("No puedes lanzar los dados, " + motivo);
    }
}
