package monopoly;

import partida.*;

public abstract class Accion extends Casilla {

    // Constructores:
    public Accion(String nombre, int posicion, Jugador duenho, String tipo) {
        super(nombre, posicion, duenho, tipo);
    }

}