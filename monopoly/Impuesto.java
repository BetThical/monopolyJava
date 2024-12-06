package monopoly;

import partida.*;

public class Impuesto extends Casilla{

    // Atributos:
    private float impuesto;

    // Constructores:
    public Impuesto(String nombre, int posicion, Jugador duenho, float impuesto) {
        super(nombre, posicion, duenho, "imposto");
        this.impuesto = impuesto;
    }


}