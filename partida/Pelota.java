package partida;

import java.util.ArrayList;
import monopoly.Casilla;
import monopoly.Juego;


public class Pelota extends Avatar {
    private final int[] movimientosPelota;

    public Pelota(Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) {
        super("pelota", jugador, lugar, avCreados);
        this.movimientosPelota = new int[5];
    }

    public int siguienteMovPelota(boolean gastarMovimiento) {
        for (int i = 0; i < movimientosPelota.length; i++) {
            if (movimientosPelota[i] != 0) {
                int mov = movimientosPelota[i];
                if (gastarMovimiento) {
                    movimientosPelota[i] = 0;
                }
                return mov;
            }
        }
        return 0;
    }

    public void avanzar(ArrayList<ArrayList<Casilla>> casillas, Jugador banca) { //movimiento parcial asociado al movimiento de la pelota. banca se necesita para evaluar casilla
        int mov = siguienteMovPelota(true);
        moverAvatar(casillas, mov, true);

        int movimientosRestantes = 0;
        for (int movimiento : movimientosPelota) {
            movimientosRestantes += movimiento;
        }
        getLugar().evaluarCasilla(getJugador(), banca, mov);
        if (movimientosRestantes == 0) {
            Juego.consola.imprimir("No quedan más movimientos de pelota.");
        } else {
            Juego.consola.imprimir("Quedan " + movimientosRestantes + " casillas que moverse con pelota.");
        }
    }

    public void resetMovPelota() {
        for (int i = 0; i < movimientosPelota.length; i++) {
            movimientosPelota[i] = 0;
        }
    }
    @Override
    public void moverEnAvanzado(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {
        // se genera un array (cola) con la lista de movimientos parciales que debe realizar la pelota
        // cada vez que el jugador llame a 'avanzar', se avanzará la posición que indique el primero de la cola
        //el movimiento acaba cuando termine la cola

        resetMovPelota();
        int i;
        if (valorTirada > 4) {
            movimientosPelota[0] = 5;
            valorTirada -= 5;
            for (i = 1; valorTirada > 1; ++i) {
                movimientosPelota[i] = 2;
                valorTirada -= 2;
            }

        } else {
            movimientosPelota[0] = -1;
            valorTirada *= -1;
            valorTirada += 1;
            for (i = 1; valorTirada < -1; i++) {
                movimientosPelota[i] = -2;
                valorTirada += 2;
            }
        }

        if (valorTirada != 0) {
            movimientosPelota[i] = valorTirada;
        }

    }
}
