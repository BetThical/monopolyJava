package monopoly;

import partida.*;

public class AccionCajaComunidad extends Accion{

    // Constructores:
    public AccionCajaComunidad(String nombre, int posicion, Jugador duenho) {
        super(nombre, posicion, duenho);
    }

    @Override
    public String infoCasilla(Jugador banca){
        return "Tipo: Caja de Comunidad";
    }
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual.puedeCogerCarta() == 0) {
            Juego.consola.imprimir("El jugador " + actual.getNombre()
                    + " ha caído en una casilla de comunidad. Puede sacar una carta.");
            actual.setCartaDisponible(1);
        }
        return true;
    }

}