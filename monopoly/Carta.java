package monopoly;

import java.util.ArrayList;
import partida.Jugador;

// Carta: clase abstracta que representa una carta del juego. Extendida por CartaComunidad y CartaSuerte, que implementan sus propias acciones.

public abstract class Carta {

    private final String texto; // Descripción de la carta
    protected final Integer id; // Identificador de la carta, que determina la acción que se realizará. Del 1 al 6.

    public Carta(Integer id, String texto) {
        this.id = id;
        this.texto = texto;
    }

    public String getTextoCarta() {
        return texto;
    }

    public void accionCarta(ArrayList<Jugador> jugadores, Jugador jugador, Tablero tablero) { // Método abstracto que se implementará en las clases hijas.
        // Necesita 'jugadores' por la acción de pagar a todos los jugadores, y 'tablero' para moverse a casillas determinadas.
    }
}
