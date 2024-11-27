package exception.noEncontradoException;

import exception.MonopolyException;
import monopoly.Valor;

// No encontrado: incluye errores donde se llama a una casilla, jugador, avatar etc. que no existe.

public class NoEncontradoException extends MonopolyException {
    public NoEncontradoException(String tipo, String input) {
        super(Valor.LIGHT_RED + "No se ha encontrado " + tipo + " \'" + input + "\'." + Valor.RESET);
        // ejemplo: No se ha encontrado el jugador 'pepe'.
        // el artículo lo proporciona el tipo para incluir el género correctamente (el
        // jugador, la casilla, el avatar...)
    }

}
