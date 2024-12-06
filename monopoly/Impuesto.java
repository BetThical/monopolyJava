package monopoly;

import partida.*;

public class Impuesto extends Casilla{

    // Atributos:
    private float impuesto;

    // Constructores:
    public Impuesto(String nombre, int posicion, Jugador duenho, float impuesto) {
        super(nombre, posicion);
        this.impuesto = impuesto;
    }
    @Override
    public String infoCasilla(Jugador banca){
        return "Tipo: Impuesto\nImpuesto: " + impuesto;
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        banca.añadirAlBote(impuesto);
        return actual.pagar(impuesto);
    }


}