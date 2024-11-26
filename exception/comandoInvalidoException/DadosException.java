package exception.comandoInvalidoException;


public class DadosException extends ComandoInvalidoException {
    public DadosException(String motivo) {
        super("No puedes lanzar los dados, " + motivo);
    }
}
