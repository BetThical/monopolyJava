package monopoly;

import java.util.ArrayList;
import partida.Avatar;
import partida.Jugador;

public class CartaSuerte extends Carta {
    public CartaSuerte(Integer id, String texto) {
        super(id, texto);
    }
    @Override
    public void accionCarta(ArrayList<Jugador> jugadores, Jugador jugador, Tablero tablero) {
        Avatar avatar = jugador.getAvatar();
        Jugador banca = tablero.getBanca();

        Casilla casillaInicial = avatar.getLugar();
        switch (id) {
            case 1:
                if (avatar.getLugar().getPosicion() <= 6) // trans1 es la casilla 6
                {
                    avatar.moverAvatar(tablero.getPosiciones(), 6 - avatar.getLugar().getPosicion(), true);
                } else {
                    avatar.moverAvatar(tablero.getPosiciones(),
                            tablero.getNumCasillas() - avatar.getLugar().getPosicion() + 6, true);
                }
                break;

            case 2:

                if (avatar.getLugar().getPosicion() <= 27) // Solar15 es la casilla 27
                {
                    avatar.moverAvatar(tablero.getPosiciones(), 27 - avatar.getLugar().getPosicion(), false);
                } else {
                    avatar.moverAvatar(tablero.getPosiciones(),
                            tablero.getNumCasillas() - avatar.getLugar().getPosicion() + 27, false);
                }
                break;

            case 3:

                jugador.sumarFortuna(500000);
                break;

            case 4:

                if (avatar.getLugar().getPosicion() <= 7) // solar3 es la casilla 7
                {
                    avatar.moverAvatar(tablero.getPosiciones(), 7 - avatar.getLugar().getPosicion(), true);
                } else {
                    avatar.moverAvatar(tablero.getPosiciones(),
                            tablero.getNumCasillas() - avatar.getLugar().getPosicion() + 7, true);
                }
                break;

            case 5:

                jugador.encarcelar(tablero.getPosiciones());
                break;

            case 6:

                jugador.sumarFortuna(1000000);
                break;

        }

        if (avatar.getLugar() != casillaInicial) { // si se ha movido
            avatar.getLugar().evaluarCasilla(jugador, banca, 0);
        }
        if (avatar.get4Voltas() == true) {
            boolean condicion = true;
            for (int i = 0; i < jugadores.size(); i++) {
                if (jugadores.get(i).getVueltas() < jugador.getVueltas()) {
                    condicion = false;
                }
            }
            if (condicion == true) {
                tablero.aumentarCoste(banca);
            }
        }

    }
}

