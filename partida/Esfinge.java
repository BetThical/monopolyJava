package partida;

import java.util.ArrayList;

import exception.comandoInvalidoException.AvatarNoImplementadoException;
import monopoly.Casilla;


public class Esfinge extends Avatar {

    public Esfinge(Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) {
        super("esfinge", jugador, lugar, avCreados);
    }

    @Override
    public void moverEnAvanzado(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) throws AvatarNoImplementadoException {
        throw new AvatarNoImplementadoException("esfinge");
    }
}
