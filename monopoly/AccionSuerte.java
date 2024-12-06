package monopoly;

import partida.*;

public class AccionSuerte extends Accion{

    // Constructores:
    public AccionSuerte(String nombre, int posicion, Jugador duenho) {
        super(nombre, posicion, duenho);
    }

    @Override
    public String infoCasilla(Jugador banca){
        return "Tipo: Suerte";
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual.puedeCogerCarta() == 0) {
            Juego.consola.imprimir("El jugador " + actual.getNombre()
                    + " ha caído en una casilla de suerte. Puede sacar una carta.");
            actual.setCartaDisponible(2);
        }
        return true;
    }
}