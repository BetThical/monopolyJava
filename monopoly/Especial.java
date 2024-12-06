package monopoly;

import partida.*;

public class Especial extends Casilla {

    // Atributos:
    private float valor;

    // Constructores:
    public Especial(String nombre, int posicion, Jugador duenho) {
        super(nombre, posicion, duenho, "especial");
        this.valor = 0; // para el parking
    }


}