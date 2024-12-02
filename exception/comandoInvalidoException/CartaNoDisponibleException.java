package exception.comandoInvalidoException;

// CartaNoDisponibleException: Uso del comando 'coger carta' cuando no has caído en una casilla correspondiente, o ya has cogido la carta.
public class CartaNoDisponibleException extends ComandoInvalidoException {
    public CartaNoDisponibleException() {
        super("No puedes coger una carta en este momento.");
    }
}
