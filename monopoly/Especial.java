package monopoly;

import partida.*;

public class Especial extends Casilla {


    // Constructores:
    public Especial(String nombre, int posicion, Jugador duenho) {
        super(nombre, posicion);
    }

    @Override
    public String infoCasilla(Jugador banca) {
        return "Tipo: Especial (" + getNombre() + ")";
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (getNombre().equals("Parking")) { // Parking
            actual.cobrarBote(banca);
        }
        if (getNombre().equals("IrCarcel")) { // Casilla 30 (ir a carcel)
            actual.setDebeIrACarcel(true);
        }
        return true;
    }
    

}