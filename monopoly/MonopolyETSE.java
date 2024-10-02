package monopoly;


public class MonopolyETSE {

    public static void main(String[] args) {
        
        String comando;

        System.out.println("hello world\n");
        Menu menu = new Menu();

        Tablero tablero = new Tablero(menu.getBanca());

        menu.iniciarPartida();
        tablero.imprimirTablero();
        menu.loopJugable();
        
    }
    
}
