package exception.comandoInvalidoException;

public class CartaNoDisponibleException extends ComandoInvalidoException {
    public CartaNoDisponibleException() {
        super("No puedes coger una carta en este momento.");
    }
}
