package monopoly;

public class MonopolyETSE {

    public static void main(String[] args) {

        Menu menu = new Menu();
        menu.titulo();

        Tablero tablero = new Tablero(menu.juego.getBanca());
        menu.juego.iniciarPartida(tablero);
        menu.loopJugable();

    }

}
