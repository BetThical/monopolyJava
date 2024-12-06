package monopoly;

import java.util.ArrayList;
import partida.*;

public abstract class Propiedad extends Casilla{

    // Atributos:
    protected float valor;
    private String tipo;
    private Jugador duenho;
    private ArrayList<Edificio> edificios;
    private Grupo grupo;

    // Constructores:
    public Propiedad(String nombre, int posicion, Jugador duenho, String tipo, float valor){
        super(nombre, posicion, duenho, tipo);
        this.valor = valor;
        //System.out.println("Propiedad creada: " + nombre + ", valor = " + getValor());
    }


    // Métodos:

    // Calcula la cantidad a pagarle a otro jugador si se cae en una casilla de su
    // propiedad.
    // Funciona para solares, transporte y servicios.
    // Parámetro: tirada, para las casillas de servicio
    public float calcular_coste(int tirada) {
        float coste;
        switch (tipo) {
            case "solar": {
                coste = getImpuesto();
                if (getGrupo().esDuenhoGrupo(duenho)) {
                    coste *= 2;
                }
                coste += alquilerEdificios();
                return coste;
            }
            case "transporte": {
                coste = (getImpuesto() * (0.25f * duenho.getNumTrans()));
                // System.out.printf("impuesto:%f, numtrans:%d, total:%f", getImpuesto(),
                // duenho.getNumTrans(), coste);
                return coste;
            }

            default: {
                // servicio
                if (duenho.getNumServ() == 1) {
                    coste = (getImpuesto() * 4 * tirada);
                } else {
                    coste = (getImpuesto() * 10 * tirada);
                }
            }
        }
        return coste;
    }

    // Devuelve True si el jugador comprador puede comprar la casilla.
    // Requisitos: el comprador tiene suficiente dinero y la casilla es un solar,
    // transporte o servicio sin dueño.
    public boolean esComprable(Jugador comprador, Jugador banca) {
        return (duenho == banca) &&
           (valor <= comprador.getFortuna());
    }


    /*
     * Método usado para comprar una casilla determinada. Parámetros:
     * - Jugador que solicita la compra de la casilla.
     * - Banca del monopoly (es el dueño de las casillas no compradas aún).
     */
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        banca.sumarFortuna(valor);
        solicitante.sumarGastosProp(valor);
        solicitante.sumarGastos(valor);

        this.duenho = solicitante;
        solicitante.anhadirPropiedad(this);

        if (this instanceof Solar) {
            if (getGrupo().esDuenhoGrupo(solicitante)) {
                System.out
                        .println("El jugador " + solicitante.getNombre() + " es dueño de todas las casillas del grupo "
                                + getGrupo().getColor() + "!");
            }
        }
        if (solicitante.getPuedeComprar()) {
            solicitante.setPuedeComprar(false);
        }
    }



    public float getValor() {
        return valor;
    }

    /*
     * Método para añadir valor a una casilla. Utilidad:
     * - Sumar valor a la casilla de parking.
     * - Sumar valor a las casillas de solar al no comprarlas tras cuatro vueltas de
     * todos los jugadores.
     * Este método toma como argumento la cantidad a añadir del valor de la casilla.
     */
    public void sumarValor(float suma) {
        this.valor += suma;
    }


    public float setValor() {
        return this.valor;
    }


}