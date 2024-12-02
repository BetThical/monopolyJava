package exception;

// Clase base para las excepciones de Monopoly.
// Lanza una excepción con el mensaje que se le pase.
// Tipos: comando incorrecto, comando inválido y no encontrado.
public class MonopolyException extends Exception {

    public MonopolyException(String mensaje) {
        super(mensaje);
    }
}
