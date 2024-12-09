package monopoly;

import partida.*;

public class Servicio extends Propiedad {


    // Constructores:
    public Servicio(String nombre, int posicion, Jugador duenho, float valor) {
        super(nombre, posicion, duenho, valor, valor / 2f, Valor.SUMA_VUELTA/200);
    }

    @Override
    public String infoCasilla(Jugador banca) {
        return "Nombre: " + getNombre() + "\nTipo: servicio" + "\nValor: " + getValor() + "€.\nHipotecada: " + (getHipotecada() ? "si" : "no") + "\n";
    }

    @Override
    public float calcularCoste(int tirada) {
        float coste;
        if (getDuenho().getNumServ() == 1) {
            coste = (getAlquiler() * 4 * tirada);
        } else {
            coste = (getAlquiler() * 10 * tirada);
        }
        return coste;
    }

    @Override
    public String casEnVenta(){
        return "Nombre: " + getNombre() + ", tipo: servicio" + ", valor: " + getValor() + "€.\n";
    }
}