package monopoly;

import partida.*;

public class Servicio extends Propiedad{

    // Atributos:
    private float hipoteca;
    private float impuesto;

    // Constructores:
    public Servicio(String nombre, int posicion, Jugador duenho, float valor) {
        super(nombre, posicion, duenho, "servicio", valor);
        this.impuesto = (Valor.SUMA_VUELTA / 200);
        this.hipoteca = valor / 2f;
    }


}