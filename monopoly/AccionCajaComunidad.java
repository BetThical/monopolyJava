package monopoly;

import partida.*;

public class AccionCajaComunidad extends Accion{

    // Constructores:
    public AccionCajaComunidad(String nombre, int posicion, Jugador duenho) {
        super(nombre, posicion, duenho, "comunidad");
    }
}