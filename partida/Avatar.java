package partida;

import monopoly.*;

import java.util.ArrayList;


public class Avatar {

    //Atributos
    private String id; //Identificador: una letra generada aleatoriamente.
    private String tipo; //Sombrero, Esfinge, Pelota, Coche
    private Jugador jugador; //Un jugador al que pertenece ese avatar.
    private Casilla lugar; //Los avatares se sitúan en casillas del tablero.

    //Constructor vacío
    public Avatar() {
    }

    /*Constructor principal. Requiere éstos parámetros:
    * Tipo del avatar, jugador al que pertenece, lugar en el que estará ubicado, y un arraylist con los
    * avatares creados (usado para crear un ID distinto del de los demás avatares).
     */
    public Avatar(String tipo, Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) {
        this.tipo = tipo;
        this.jugador = jugador;
        this.lugar = lugar;
        this.generarId(avCreados);
    }

    public String getId() {
        return this.id;
    }
    //A continuación, tenemos otros métodos útiles para el desarrollo del juego.
    /*Método que permite mover a un avatar a una casilla concreta. Parámetros:
    * - Un array con las casillas del tablero. Se trata de un arrayList de arrayList de casillas (uno por lado).
    * - Un entero que indica el numero de casillas a moverse (será el valor sacado en la tirada de los dados).
    * EN ESTA VERSIÓN SUPONEMOS QUE valorTirada siemrpe es positivo.
     */
    public void moverAvatar(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {

        int posicionactual = lugar.getPosicion();

        int nuevaposicion = (posicionactual + valorTirada) % 40;

        int lado = (nuevaposicion / 10);

        int casilla = (nuevaposicion % 10) - 1;

        System.out.println("Posición actual: " + posicionactual);
        System.out.println("Valor de la tirada: " + valorTirada);
        System.out.println("Nueva posición: " + nuevaposicion);
        System.out.println("Lado calculado: " + lado);
        System.out.println("Casilla calculada: " + casilla);

        Casilla nuevaCasilla = casillas.get(lado).get(casilla);

        this.lugar = nuevaCasilla;
    }

    /*Método que permite generar un ID para un avatar. Sólo lo usamos en esta clase (por ello es privado).
    * El ID generado será una letra mayúscula. Parámetros:
    * - Un arraylist de los avatares ya creados, con el objetivo de evitar que se generen dos ID iguales.
     */
    private void generarId(ArrayList<Avatar> avCreados) {
        this.id = String.valueOf((char) (avCreados.size() + 64 + 1)); //as letras asígnanse por orden, o primeiro avatar ten A, o segundo B ...
    }

    public String getID(){
        return id;
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

    public void setLugar(Casilla nuevaCasilla) {
        this.lugar = nuevaCasilla;
    }
}
