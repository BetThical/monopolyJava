package exception;

public class CartaNoDisponibleException extends ComandoException {
    public CartaNoDisponibleException() {
        super("No puedes coger una carta en este momento.");
    }
}
