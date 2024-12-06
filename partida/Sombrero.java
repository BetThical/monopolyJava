package partida;

import java.util.ArrayList;

import exception.comandoInvalidoException.AvatarNoImplementadoException;
import monopoly.Casilla;


public class Sombrero extends Avatar {

    public Sombrero(Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) {
        super("sombrero", jugador, lugar, avCreados);
    }

    @Override
    public void moverEnAvanzado(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) throws AvatarNoImplementadoException {
        throw new AvatarNoImplementadoException("sombrero");
    }
}
