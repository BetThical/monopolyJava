package monopoly;

import java.util.ArrayList;
import partida.Jugador;

// Objeto carta: contiene sólo el texto de la acción que se ejecuta al sacar la carta.
// El código asociado a la acción se encuentra en la clase Juego.
public abstract class Carta {

    private final String texto;
    protected final Integer id; //?

    public Carta(Integer id, String texto) {
        this.id = id;
        this.texto = texto;
    }

    public String getCarta() {
        return texto;
    }

    public void accionCarta(ArrayList<Jugador> jugadores, Jugador jugador, Tablero tablero) {
    }
}
