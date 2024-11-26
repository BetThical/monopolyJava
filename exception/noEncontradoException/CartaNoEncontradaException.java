package exception.noEncontradoException;

public class CartaNoEncontradaException extends NoEncontradoException {
    public CartaNoEncontradaException(Integer numero) {
        super("la carta", numero.toString());
    }
}