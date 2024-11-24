package monopoly;

public class MonopolyETSE {

    public static void main(String[] args) {

        Juego menu = new Juego();
        menu.titulo();

        Tablero tablero = new Tablero(menu.getBanca());
        menu.iniciarPartida(tablero);
        menu.loopJugable();

    }

}
