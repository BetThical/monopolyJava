package exception.comandoIncorrectoException;
import exception.MonopolyException;
import monopoly.Valor;

// Comando incorrecto: incluye errores de sintaxis incorrecta de comandos, comandos sin argumentos, comandos con argumentos incorrectos, etc.

public class ComandoIncorrectoException extends MonopolyException {
    public ComandoIncorrectoException(String mensaje) {
        super(Valor.YELLOW + "Uso del comando: " + mensaje + Valor.RESET);
    }
    
}
