package monopoly;

import java.util.ArrayList;
import partida.Avatar;
import partida.Jugador;

public class CartaComunidad extends Carta {

    public CartaComunidad(Integer id, String texto) { // El constructor de CartaComunidad es idéntico al de carta
        super(id, texto);
    }

    @Override // Método que implementa la acción de la carta de comunidad. Sobreescribe el método de la clase padre.
    public void accionCarta(ArrayList<Jugador> jugadores, Jugador jugador, Tablero tablero) {
        Avatar avatar = jugador.getAvatar();
        Jugador banca = tablero.getBanca();
        Casilla casillaInicial = avatar.getLugar();

        switch (id) {

            case 1: // Paga 500000€ por un fin de semana en un balneario de 5 estrellas.
                jugador.sumarGastos(500000);
                if (jugador.getFortuna() < 0) { // si se queda en deuda
                    jugador.setFortunaPrevia((500000 + jugador.getFortuna()));
                    jugador.setEnDeuda(banca);
                }
                break;

            case 2: // Te investigan por fraude de identidad. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida y sin cobrar la cantidad habitual.
                jugador.encarcelar(tablero.getPosiciones());
                break;

            case 3: // Colócate en la casilla de Salida. Cobra la cantidad habitual.
                avatar.moverAvatar(tablero.getPosiciones(),
                        tablero.getNumCasillas() - avatar.getLugar().getPosicion() + 1, true);
                break;

            case 4: // Tu compañía de Internet obtiene beneficios. Recibe 2000000€.
                jugador.sumarFortuna(2000000);
                break;

            case 5: // Paga 1000000€ por invitar a todos tus amigos a un viaje a Solar142.
                jugador.sumarGastos(1000000);
                if (jugador.getFortuna() < 0) { // si se queda en deuda
                    jugador.setFortunaPrevia((1000000 + jugador.getFortuna()));
                    jugador.setEnDeuda(banca);
                }
                break;

            case 6: // Alquilas a tus compañeros una villa en Solar7 durante una semana. Paga 200000€ a cada jugador.
                for (int i = 0; i < jugadores.size(); i++) {
                    if (jugadores.get(i) != jugador) {
                        jugador.sumarGastos(200000);
                        jugadores.get(i).sumarFortuna(200000);
                        if (jugador.getFortuna() < 0) { // sigue el comportamiento descrito en el glosario de dudas
                            jugador.setFortunaPrevia((200000 + jugador.getFortuna()));
                            jugador.setEnDeuda(banca);
                            break;
                        }
                    }
                }

                break;
        }

        if (avatar.getLugar() != casillaInicial) { // si se ha movido
            avatar.getLugar().evaluarCasilla(jugador, banca, 0);
            // el valor de '0' es arbitrario, ya que no se utiliza en la evaluación de la casilla (ninguna carta te lleva a servicios)
        }

    }
}
