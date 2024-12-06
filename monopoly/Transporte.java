package monopoly;

import partida.*;

public class Transporte extends Propiedad{

    // Atributos:
    private float hipoteca;
    private float impuesto;

    // Constructores:
    public Transporte(String nombre, int posicion, Jugador duenho, float valor){
        super(nombre, posicion, duenho, "transporte", valor);
        this.impuesto = (Valor.SUMA_VUELTA / 200);
        this.hipoteca = valor / 2f;
    }


}