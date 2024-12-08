package monopoly;

import exception.comandoInvalidoException.HipotecaException;
import partida.*;

public abstract class Propiedad extends Casilla {

    // Atributos:
    protected float valor; // precio de compra
    private Jugador duenho;
    private final float hipoteca;
    private final float alquiler; // precio al aer en la casilla, sin modificadores (como edificios y tirada)
    private boolean hipotecada=false;
    private float rentable;

    // Constructores:
    public Propiedad(String nombre, int posicion, Jugador duenho, float valor, float hipoteca, float alquiler) {
        super(nombre, posicion);
        this.duenho = duenho;
        this.valor = valor;
        this.hipoteca = hipoteca;
        this.alquiler = alquiler;
    }

    // Métodos:

    // Calcula la cantidad a pagarle a otro jugador si se cae en una casilla de su
    // propiedad.
    // Funciona para solares, transporte y servicios.
    // Parámetro: tirada, para las casillas de servicio
    public abstract float calcularCoste(int tirada);

        public void hipotecar(Jugador jugador) throws HipotecaException {
        if (!perteneceAJugador(jugador)) {
            throw new HipotecaException("hipotecar", "no eres dueño de " + getNombre() + ".");
        }
        if (hipotecada) {
            throw new HipotecaException("hipotecar", "ya está hipotecada.");
        }
        setHipotecada(true);
        duenho.sumarFortuna(hipoteca);
    }

    public boolean perteneceAJugador(Jugador jugador) {
        return duenho.equals(jugador);
    }

    public boolean getHipotecada(){
        return hipotecada;
    }

    public void deshipotecar(Jugador jugador) throws HipotecaException {
        if (!perteneceAJugador(jugador)) {
            throw new HipotecaException("deshipotecar", "no eres dueño de " + getNombre() + ".");

        }
        if (!hipotecada) {
            throw new HipotecaException("deshipotecar", getNombre() + " no está hipotecada.");

        }

        if (hipoteca * 1.1f > jugador.getFortuna()) {
            throw new HipotecaException("deshipotecar", "no tienes los fondos necesarios (" + hipoteca * 1.1f + "€).");

        }
        setHipotecada(false);
        duenho.sumarGastos(hipoteca * 1.1f);
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
            Solar solar = (Solar) this;
            if (solar.getGrupo().esDuenhoGrupo(solicitante)) {
                System.out
                        .println("El jugador " + solicitante.getNombre() + " es dueño de todas las casillas del grupo "
                                + solar.getGrupo().getColor() + "!");
            }
        }
        if (solicitante.getPuedeComprar()) {
            solicitante.setPuedeComprar(false);
        }
    }

        /*
     * Método para mostrar información de una casilla en venta.
     * Valor devuelto: texto con esa información.
     */
    public abstract String casEnVenta();

    public float getValor() {
        return valor;
    }

    public void setDuenho(Jugador jugador) {
        this.duenho = jugador;
    }

    public float getHipoteca(){
        return hipoteca;
    }
    public void setHipotecada(boolean hipotecada){
        this.hipotecada=hipotecada;
    }
    public float getAlquiler() {
        return alquiler;
    }
    public Jugador getDuenho(){
        return duenho;
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

    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (esComprable(actual, banca)
                && !(!actual.getPuedeComprar() && actual.getAvatar().getTipo().equals("coche")
                        && actual.getMovEspecial())) {
            String respuesta = Juego.consola
                    .leer("El jugador " + actual.getNombre() + " puede comprar esta casilla, por "
                            + getValor()
                            + " euros. Comprar? (Y/N) ")
                    .toUpperCase(); // Captura la respuesta del jugador (en mayúsculas)

            if (respuesta.equals("Y")) {
                comprarCasilla(actual, banca); // Llama al método para realizar la compra
                Juego.consola.imprimir(
                        "El jugador " + actual.getNombre() + " ha comprado la casilla " + getNombre() + ".");
            } else if (respuesta.equals("N")) {
                Juego.consola.imprimir("El jugador " + actual.getNombre() + " ha decidido no comprar la casilla.");
            } else {
                Juego.consola.imprimir("Respuesta inválida. Por favor, introduce 'Y' o 'N'.");
            }
        }

        if (getDuenho() != banca && getDuenho() != actual) {
            if (hipotecada) {
                Juego.consola.imprimir("El dueño es " + duenho.getNombre() + ", pero la propiedad está hipotecada.");
                return true;
            }
            sumarRentable(calcularCoste(tirada));
            return actual.pagar(calcularCoste(tirada), duenho, true);
        }
        return true;

    }

    public float GetRentabilidad() {
        return rentable;
    }

    public void sumarRentable(float rentable) {
        this.rentable += rentable;
    }

}