package monopoly;

import java.util.ArrayList;
import partida.Avatar;
import partida.Jugador;

public class CartaComunidad extends Carta {
    public CartaComunidad(Integer id, String texto) {
        super(id, texto);
    }
    
    @Override
    public void accionCarta(ArrayList<Jugador> jugadores, Jugador jugador, Tablero tablero) {
        Avatar avatar = jugador.getAvatar();
        Jugador banca = tablero.getBanca();
        Casilla casillaInicial = avatar.getLugar();
        switch (id) {
            case 1:

                jugador.sumarGastos(500000);
                if (jugador.getFortuna() < 0) {
                    jugador.setFortunaPrevia((500000 + jugador.getFortuna()));
                    jugador.setEnDeuda(banca);
                }
                break;

            case 2:
                jugador.encarcelar(tablero.getPosiciones());
                break;

            case 3:

                avatar.moverAvatar(tablero.getPosiciones(),
                        tablero.getNumCasillas() - avatar.getLugar().getPosicion() + 1, true);
                break;

            case 4:

                jugador.sumarFortuna(2000000);
                break;

            case 5:

                jugador.sumarGastos(1000000);
                if (jugador.getFortuna() < 0) {
                    jugador.setFortunaPrevia((1000000 + jugador.getFortuna()));
                    jugador.setEnDeuda(banca);
                }
                break;

            case 6:

                for (int i = 0; i < jugadores.size(); i++) {
                    if (jugadores.get(i) != jugador) {
                        jugador.sumarGastos(200000);
                        jugadores.get(i).sumarFortuna(200000);

                        if (jugador.getFortuna() < 0) {
                            jugador.setFortunaPrevia((200000 + jugador.getFortuna()));
                            // do glosario de dubidas: Na carta de comunidade 6 (Alquilas a tus compañeros
                            // una villa en Solar7 durante una semana. Paga 200000€ a cada jugador),
                            // se o xogador non tén diñeiro para afrontar este pago e decide declararse en
                            // bancarrota, toda a súa fortuna e propiedades pasan á banca.
                            jugador.setEnDeuda(banca);
                            break;
                        }
                    }
                }

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

