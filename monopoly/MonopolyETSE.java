package monopoly;

public class MonopolyETSE {

    public static void main(String[] args) {

        Juego juego = new Juego();
        juego.titulo();

        Tablero tablero = new Tablero(menu.getBanca());
        juego.iniciarPartida(tablero);
        juego.loopJugable();

    }

}
