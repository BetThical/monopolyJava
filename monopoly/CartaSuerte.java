package monopoly;

import java.util.ArrayList;
import partida.Avatar;
import partida.Jugador;

public class CartaSuerte extends Carta {

    public CartaSuerte(Integer id, String texto) { // El constructor de CartaSuerte es idéntico al de carta.
        super(id, texto);
    }

    @Override // Método que implementa la acción de la carta de suerte. Sobreescribe el método de la clase padre.
    public void accionCarta(ArrayList<Jugador> jugadores, Jugador jugador, Tablero tablero) {
        Avatar avatar = jugador.getAvatar();
        Jugador banca = tablero.getBanca();
        Casilla casillaInicial = avatar.getLugar();
        int posInicial = avatar.getLugar().getPosicion();
        switch (id) {
            case 1: // Ve al Transportes1 y coge un avión. Si pasas por la casilla de Salida, cobra la cantidad habitual.
                // trans1 es la casilla 6
                avatar.moverAvatar(tablero.getPosiciones(), Math.abs(6 - posInicial), true);
                break;

            case 2: // Decides hacer un viaje de placer. Avanza hasta Solar15 directamente, sin pasar por la casilla de Salida y
                // sin cobrar la cantidad habitual.
                avatar.moverAvatar(tablero.getPosiciones(), Math.abs(27 - posInicial), false);
                break;

            case 3: // Vendes tu billete de avión para Solar17 en una subasta por Internet. Cobra 500000€.
                jugador.sumarFortuna(500000);
                break;

            case 4: // Ve a Solar3. Si pasas por la casilla de Salida, cobra la cantidad habitual.
                // solar3 es la casilla 7
                avatar.moverAvatar(tablero.getPosiciones(), Math.abs(7 - posInicial), true);
                break;

            case 5: // Los acreedores te persiguen por impago. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida
                // y sin cobrar la cantidad habitual.
                jugador.encarcelar(tablero.getPosiciones());
                break;

            case 6: // ¡Has ganado el bote de la lotería! Recibe 1000000€.
                jugador.sumarFortuna(1000000);
                break;
        }

        if (avatar.getLugar() != casillaInicial) { // si se ha movido
            avatar.getLugar().evaluarCasilla(jugador, banca, 0);
        }
    }
}
