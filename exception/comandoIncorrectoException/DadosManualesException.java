package exception.comandoIncorrectoException;

public class DadosManualesException extends ComandoIncorrectoException{
    public DadosManualesException() {
        super("lanzar dados [x]+[y]   /   m[x]");
    }
}
