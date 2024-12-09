package monopoly;

import partida.*;

public class Transporte extends Propiedad {


    // Constructores:
    public Transporte(String nombre, int posicion, Jugador duenho, float valor) {
        super(nombre, posicion, duenho, valor, valor / 2f, valor);
    }


    @Override
    public String infoCasilla(Jugador banca) {
        return "Tipo: Transporte\n" + "Propietario: " + getDuenho().getNombre() + "\n" + "Valor: " + getValor() + "\n"
                + "Hipoteca: " + getHipoteca() + "\n" + "Factor de transporte: " + getAlquiler() + "\nHipotecada: " + (getHipotecada() ? "si" : "no") + "\n";
    }
    @Override
    public float calcularCoste(int tirada) {
        float coste;
        coste = (getAlquiler() * (0.25f * getDuenho().getNumTrans()));
        // System.out.printf("impuesto:%f, numtrans:%d, total:%f", getImpuesto(),
        // duenho.getNumTrans(), coste);
        return coste;
    }

    @Override
    public String casEnVenta(){
        return "Nombre: " + getNombre() + ", tipo: transporte" + ", valor: " + getValor() + "€.\n";
    }
}