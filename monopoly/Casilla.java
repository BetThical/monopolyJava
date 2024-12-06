package monopoly;

import java.util.ArrayList;

import partida.*;

public abstract class Casilla {

    // Atributos:
    private String nombre; // Nombre de la casilla
    // Impostos).
    private int posicion; // Posición que ocupa la casilla en el tablero (entero entre 1 y 40).

    private ArrayList<Avatar> avatares; // Avatares que están situados en la casilla.
    private int visitas; // Número de veces que se ha visitado la casilla.

    // Constructores:
    public Casilla() {
    }

    protected Casilla(String nombre, int posicion) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.avatares = new ArrayList<>();
    }

    // Métodos:


    public boolean estaAvatar(Avatar av) {
        return avatares.contains(av);
    }
    // Método utilizado para añadir un avatar al array de avatares en casilla.
    public void anhadirAvatar(Avatar av) {
        if (!estaAvatar(av)) {
            avatares.add(av);
        }
    }

    // Método utilizado para eliminar un avatar del array de avatares en casilla.
    public void eliminarAvatar(Avatar av) {
        if (estaAvatar(av)) {
            avatares.remove(av);
        }
    }

    public int getVisitas() {
        return visitas;
    }
    // Pepito

    /*
     * Método para evaluar qué hacer en una casilla concreta. Parámetros:
     * - Jugador cuyo avatar está en esa casilla.
     * - La banca (para ciertas comprobaciones).
     * - El valor de la tirada: para determinar impuesto a pagar en casillas de
     * servicios.
     * Valor devuelto: true en caso de ser solvente (es decir, de cumplir las
     * deudas), y false
     * en caso de no cumplirlas.
     */

    public abstract boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada);

    /*
     * Método para mostrar información sobre una casilla.
     * Devuelve una cadena con información específica de cada tipo de casilla.
     * Banca para poder imprimir el bote.
     */
    public abstract String infoCasilla(Jugador banca);

    // GETTERS
    public String getNombre() {
        return nombre;
    }

    public int getPosicion() {
        return posicion;
    }

    public ArrayList<Avatar> getAvatares() {
        return avatares;
    }

    // SETTERS
    public void setPosicion(int p) {
        this.posicion = p;
    }

    public void addVisitas(int p) {
        this.visitas += p;
    }

}