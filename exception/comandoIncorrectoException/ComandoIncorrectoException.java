package exception.comandoIncorrectoException;

import exception.MonopolyException;
import monopoly.Valor; //para imprimir en color

// Comando incorrecto: incluye errores de sintaxis incorrecta de comandos, comandos sin argumentos, comandos con argumentos incorrectos, etc.
// Ejemplo: "comprar" sin argumentos, lanzar dados manual con sintaxis inválida...
public class ComandoIncorrectoException extends MonopolyException {

    public ComandoIncorrectoException(String mensaje) {
        super(Valor.YELLOW + "Uso del comando: " + mensaje + Valor.RESET);
        // la clase derivada debe llamar a super() con el uso correcto del comando
    }

}
