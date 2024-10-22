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

        /*           System.out.println("Posición actual: " + posicionactual);
          System.out.println("Valor de la tirada: " + valorTirada);
          System.out.println("Nueva posición: " + nuevaposicion);
          System.out.println("Lado calculado: " + lado);
          System.out.println("Casilla calculada: " + casilla);
          
         */
        Casilla nuevaCasilla = casillas.get(lado).get(casilla);
        nuevaCasilla.anhadirAvatar(this);
        vecesCaidasCasilla[nuevaposicion] += 1;
        this.lugar = nuevaCasilla;
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

    public int getVecesCaidasEnCasilla(int posicion){
        return vecesCaidasCasilla[posicion];
    }

}
