package partida;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import monopoly.*;

public class Avatar {

    // Atributos
    private String id; // Identificador: una letra generada aleatoriamente.
    private String tipo; // Sombrero, Esfinge, Pelota, Coche
    private Jugador jugador; // Un jugador al que pertenece ese avatar.
    private Casilla lugar; // Los avatares se sitúan en casillas del tablero.
    private boolean ultimoMovementoFuiVoltaMultiploDe4;
    private int tiradasCoche;
    private boolean haComprado;
    private int[] vecesCaidasCasilla;

    // Constructor vacío
    public Avatar() {
    }

    /*
     * Constructor principal. Requiere éstos parámetros:
     * Tipo del avatar, jugador al que pertenece, lugar en el que estará ubicado, y
     * un arraylist con los
     * avatares creados (usado para crear un ID distinto del de los demás avatares).
     */
    public Avatar(String tipo, Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) {
        this.tipo = tipo;
        this.jugador = jugador;
        this.lugar = lugar;
        this.generarId(avCreados);
        this.vecesCaidasCasilla = new int[40];
        this.tiradasCoche = 0;
    }

    public int getTiradasCoche() {
        return tiradasCoche;
    }

    // A continuación, tenemos otros métodos útiles para el desarrollo del juego.
    /*
     * Método que permite mover a un avatar a una casilla concreta. Parámetros:
     * - Un array con las casillas del tablero. Se trata de un arrayList de
     * arrayList de casillas (uno por lado).
     * - Un entero que indica el numero de casillas a moverse (será el valor sacado
     * en la tirada de los dados).
     * EN ESTA VERSIÓN SUPONEMOS QUE valorTirada siemrpe es positivo.
     */
    public void moverAvatar(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {
        ultimoMovementoFuiVoltaMultiploDe4 = false;
        int posicionactual = lugar.getPosicion();

        lugar.eliminarAvatar(this);
        if (posicionactual + valorTirada > 40) {
            System.out.println("Pasas por salida y cobras " + Valor.SUMA_VUELTA + ".");
            jugador.sumarVuelta();
            if (jugador.getVueltas() % 4 == 0) {
                ultimoMovementoFuiVoltaMultiploDe4 = true;
            }
        }
        int nuevaposicion = (posicionactual + valorTirada - 1) % 40;

        int lado = (nuevaposicion / 10);

        int casilla = (nuevaposicion % 10);
        Casilla nuevaCasilla = casillas.get(lado).get(casilla);
        nuevaCasilla.anhadirAvatar(this);
        vecesCaidasCasilla[nuevaposicion] += 1;
        this.lugar = nuevaCasilla;
    }

    public void moverPelota(ArrayList<ArrayList<Casilla>> casillas, int valorTirada, Jugador banca) {
        ultimoMovementoFuiVoltaMultiploDe4 = false;
        int posicionActual = lugar.getPosicion();
        lugar.eliminarAvatar(this);

        boolean avanzar = valorTirada > 4;
        int movimientosRestantes = avanzar ? valorTirada : -valorTirada;
        movimientosRestantes--;
        int posicion = posicionActual;
        boolean detenerMovimiento = false;
        if (posicion + valorTirada > 40) {
            System.out.println("Pasas por salida y cobras " + Valor.SUMA_VUELTA + ".");
            jugador.sumarVuelta();
            if (jugador.getVueltas() % 4 == 0) {
                ultimoMovementoFuiVoltaMultiploDe4 = true;
            }
        }
        while (!detenerMovimiento && Math.abs(movimientosRestantes) > 0) {
            // Mover hacia adelante o hacia atrás
            if (avanzar) {
                posicion = (posicion + 1) % 40;
                movimientosRestantes--;
            } else {
                posicion = (posicion - 1 + 40) % 40;
                movimientosRestantes++; // Aquí se suma para contar el movimiento hacia atrás
            }

            Casilla casillaActual = casillas.get(posicion / 10).get(posicion % 10);

            // Si es casilla impar desde 4, y casilla no es ir a cárcel, ejecutar paradas
            if ((posicion % 10) % 2 != 0 && posicion >= 4) {
                System.out.println(
                        "El avatar " + this.getID() + " se detiene en la casilla " + casillaActual.getNombre());

                if (!casillaActual.evaluarCasilla(jugador, banca, valorTirada)) {
                    System.out.println("El jugador " + jugador.getNombre() + " no puede pagar sus deudas!");
                    return;
                }

                if (casillaActual.getPosicion() == 31) { // Ir a Cárcel
                    jugador.getAvatar().setLugar(casillas, 10);
                    jugador.setEnCarcel(true);
                    return;
                }
            }
        }

        // Al final del bucle, actualizar la posición final y añadir el avatar a la
        // casilla
        Casilla casillaFinal = casillas.get(posicion / 10).get(posicion % 10);
        casillaFinal.anhadirAvatar(this);
        vecesCaidasCasilla[posicion] += 1;
        this.lugar = casillaFinal;
        // System.out.println("El avatar " + this.getID() + " avanza " + valorTirada + "
        // posiciones, desde "
        // + lugar.getNombre() + " hasta " + casillaFinal.getNombre() + ".");
    }

    public void moverCoche(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {
        ultimoMovementoFuiVoltaMultiploDe4 = false;
        int posicionactual = lugar.getPosicion();

        if (valorTirada <= 4) {
            System.out.println("Comes merda durante 2 turnos.");
            jugador.calarCoche();
            valorTirada *= -1;
        }
        lugar.eliminarAvatar(this);
        if (posicionactual + valorTirada > 40) {
            System.out.println("Pasas por salida y cobras " + Valor.SUMA_VUELTA + ".");
            jugador.sumarVuelta();
            if (jugador.getVueltas() % 4 == 0) {
                ultimoMovementoFuiVoltaMultiploDe4 = true;
            }
        }

        int nuevaposicion = (posicionactual + valorTirada - 1) % 40;
        if (nuevaposicion < 0)
            nuevaposicion += 40;
        int lado = (nuevaposicion / 10);

        int casilla = (nuevaposicion % 10);
        Casilla nuevaCasilla = casillas.get(lado).get(casilla);
        nuevaCasilla.anhadirAvatar(this);
        vecesCaidasCasilla[nuevaposicion] += 1;
        this.lugar = nuevaCasilla;
    }

    public void setHaCompradoEnTirada(Boolean t) {
        haComprado = t;
    }

    public Boolean haCompradoEnTirada() {
        return haComprado;
    }

    // Método para reiniciar tiradas cuando sea necesario
    public void resetearTiradasCoche() {
        this.tiradasCoche = 0;
    }

    /*
     * Método que permite generar un ID para un avatar. Sólo lo usamos en esta clase
     * (por ello es privado).
     * El ID generado será una letra mayúscula. Parámetros:
     * - Un arraylist de los avatares ya creados, con el objetivo de evitar que se
     * generen dos ID iguales.
     */
    private void generarId(ArrayList<Avatar> avCreados) {

        char letra_aleatoria;
        boolean IdUnico;

        do {
            int valor = ThreadLocalRandom.current().nextInt(65, 91);
            letra_aleatoria = (char) (valor);
            IdUnico = true;

            for (Avatar avatar : avCreados) {
                if (avatar.getID().equals(String.valueOf(letra_aleatoria))) {
                    IdUnico = false;
                    break;
                }
            }

        } while (!IdUnico);

        this.id = String.valueOf(letra_aleatoria);
    }

    public String getID() {
        return id;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public Casilla getLugar() {
        return lugar; // Devuelve la casilla donde se encuentra el avatar
    }

    public void mover(Casilla nuevaCasilla) {
        this.lugar = nuevaCasilla;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setLugar(Casilla nuevaCasilla) { // Mueve el avatar a una casilla
        lugar.eliminarAvatar(this);
        this.lugar = nuevaCasilla;
    }

    public void setLugar(ArrayList<ArrayList<Casilla>> casillas, int posicion) { // Mueve el avatar a una posición del
                                                                                 // tablero
        lugar.eliminarAvatar(this);
        int lado = (posicion / 10);
        int casilla = (posicion % 10);
        vecesCaidasCasilla[posicion] += 1;

        Casilla nuevaCasilla = casillas.get(lado).get(casilla);
        nuevaCasilla.anhadirAvatar(this);
        lugar = nuevaCasilla;

    }

    public boolean get4Voltas() { // Devuelve True si el jugador acaba de completar 4 vueltas en su último
                                  // movimiento.
        return ultimoMovementoFuiVoltaMultiploDe4;
    }

    public int getVecesCaidasEnCasilla(int posicion) {
        return vecesCaidasCasilla[posicion];
    }

}
