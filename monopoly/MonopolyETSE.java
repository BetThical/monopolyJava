package monopoly;

public class MonopolyETSE {

    public static void main(String[] args) {

        Menu menu = new Menu();
        menu.titulo();

        Tablero tablero = new Tablero(menu.getBanca());
        menu.iniciarPartida(tablero);
        menu.loopJugable();

    }

}
