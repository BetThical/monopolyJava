package monopoly;

import partida.*;

public class AccionSuerte extends Accion{

    // Constructores:
    public AccionSuerte(String nombre, int posicion, Jugador duenho) {
        super(nombre, posicion, duenho, "suerte");
    }

}