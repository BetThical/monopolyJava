package exception.comandoIncorrectoException;

// DadosManualesException: Se lanza tras un uso incorrecto del comando 'lanzar dados' o 'm' con parámetros mal pasados o inexistentes.
public class DadosManualesException extends ComandoIncorrectoException {

    public DadosManualesException() {
        super("lanzar dados [x]+[y]   /   m[x]");
    }
}
