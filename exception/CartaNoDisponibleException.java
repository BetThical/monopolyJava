package exception;

public class CartaNoDisponibleException extends Exception {
    public CartaNoDisponibleException() {
        super("No puedes coger una carta en este momento.");
    }

    public CartaNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
