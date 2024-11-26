package exception.comandoInvalidoException;

import exception.MonopolyException;
import monopoly.Valor;
// Comando invalido: Excepciones causadas por usar un comando cuando no se puede utilizar (coger carta sin cartas, pagar sin tener fondos...)

public class ComandoInvalidoException extends MonopolyException {

    public ComandoInvalidoException(String mensaje) {
        super(Valor.LIGHT_RED + "No puedes usar este comando: " + mensaje + Valor.RESET);
    }

}
