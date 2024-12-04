package partida;

import java.util.ArrayList;
import monopoly.Casilla;
import monopoly.Juego;

public class Coche extends Avatar {

    private int tiradasCoche;
    private boolean haComprado;
    private int cocheCalado; // número de turnos que el coche no puede tirar

    public Coche(Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) {
        super("coche", jugador, lugar, avCreados);
        this.tiradasCoche = 0;
    }

    @Override
    public void moverEnAvanzado(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {

        if (valorTirada <= 4) {
            Juego.consola.imprimir("No puedes tirar durante dos turnos.");
            calarCoche();
            valorTirada *= -1;
        }
        moverAvatar(casillas, valorTirada, true);
    }

    public void setHaCompradoEnTirada(Boolean t) {
        haComprado = t;
    }

    public Boolean haCompradoEnTirada() {
        return haComprado;
    }

    public int getTiradasCoche() {
        return tiradasCoche;
    }

    public void calarCoche() {
        cocheCalado = 3;
    }

    public void reducirCocheCalado() {
        cocheCalado--;
    }

    public int getCocheCalado() {
        return cocheCalado;
    }
    // Método para reiniciar tiradas cuando sea necesario
    public void resetearTiradasCoche() {
        this.tiradasCoche = 0;
    }
}
