package monopoly;

import java.util.*;
import partida.*;


public class Menu {

    //Atributos
    private ArrayList<Jugador> jugadores; //Jugadores de la partida.
    private ArrayList<Avatar> avatares; //Avatares en la partida.
    private int turno = 0; //Índice correspondiente a la posición en el arrayList del jugador (y el avatar) que tienen el turno
    private int lanzamientos; //Variable para contar el número de lanzamientos de un jugador en un turno.
    private Tablero tablero; //Tablero en el que se juega.
    private Dado dado1; //Dos dados para lanzar y avanzar casillas.
    private Dado dado2;
    private Jugador banca; //El jugador banca.
    private boolean tirado; //Booleano para comprobar si el jugador que tiene el turno ha tirado o no.
    private boolean solvente; //Booleano para comprobar si el jugador que tiene el turno es solvente, es decir, si ha pagado sus deudas.
    private boolean acabarPartida; //Booleano para comprobar si hai que acabar la partida.

    public Jugador getBanca(){
        return banca;
    }

    public Menu(){
        this.banca = new Jugador();
        this.tablero = new Tablero(getBanca());

        
    }

    // Método para inciar una partida: crea los jugadores y avatares.
    public void iniciarPartida(Tablero t) {



        Scanner sc = new Scanner(System.in);

        jugadores = new ArrayList<>();
        avatares = new ArrayList<>();
        ArrayList<Avatar> avCreados = new ArrayList<>();

        System.out.println("Introduce el número de jugadores:");
        int numJugadores = sc.nextInt();
        sc.nextLine();

        Casilla casillaInicio = tablero.getCasilla(0);

        for (int i = 1; i <= numJugadores; i++) {
            System.out.println("Introduce el nombre del jugador " + i + ":");
            String nombre = sc.nextLine();

            System.out.println("Elige el tipo de avatar para " + nombre + " (por ejemplo: coche, sombrero, perro):");
            String tipoAvatar = sc.nextLine();

            Jugador jugador = new Jugador(nombre, tipoAvatar, casillaInicio, avCreados);
            jugadores.add(jugador);

            avCreados.add(jugador.getAvatar());
            casillaInicio.anhadirAvatar(jugador.getAvatar());
            System.out.println("Jugador " + nombre + " con avatar " + tipoAvatar + " creado.");

        }

        tablero.SetCasilla(casillaInicio, 0);
        System.out.println("Partida iniciada con " + numJugadores + " jugadores.");

    }

    public void loopJugable(){
        String comando;
        Scanner sc = new Scanner(System.in);
        while (1==1){
            tablero.imprimirTablero();
            
            comando = sc.nextLine();

            analizarComando(comando);
            
        }
    }
    
    /*Método que interpreta el comando introducido y toma la accion correspondiente.
    * Parámetro: cadena de caracteres (el comando).
    */
    private void analizarComando(String comando) {

        if (comando.equals("lanzar dados") &&( lanzamientos == 0 || dado1.getValorPrevio() == dado2.getValorPrevio())){
            lanzarDados();
            lanzamientos ++;
        }
        else if (comando.equals("lanzar dados")){
            System.out.println("Śolo se pueden lanzar los dados una vez por turno, a no ser que saques dobles.");
        }

        if (comando.equals("acabar turno") &&(lanzamientos != 0 || dado1.getValorPrevio() == dado2.getValorPrevio())){
            acabarTurno();

        }
        else if (comando.equals("acabar turno")){
            System.out.println("Debes lanzar los dados.");
        }
        

    }

    /*Método que realiza las acciones asociadas al comando 'describir jugador'.
    * Parámetro: comando introducido
     */
    private void descJugador(String[] partes) {
    }

    /*Método que realiza las acciones asociadas al comando 'describir avatar'.
    * Parámetro: id del avatar a describir.
    */
    private void descAvatar(String ID) {
    }

    /* Método que realiza las acciones asociadas al comando 'describir nombre_casilla'.
    * Parámetros: nombre de la casilla a describir.
    */
    private void descCasilla(String nombre) {
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
    private void lanzarDados() {

        Jugador jugador = obtenerJugadorTurno();

        Avatar avatar = jugador.getAvatar();

        dado1 = new Dado();

        dado2 = new Dado();

        int tirada1 = dado1.hacerTirada();

        int tirada2 = dado2.hacerTirada();

        int valor_tiradas = tirada1 + tirada2;

        
        if (jugador.getEnCarcel() && (tirada2 != tirada1)) {

            System.out.println("Continúas en la carcel.");
            return;

        } else if (jugador.getEnCarcel() && (tirada2 == tirada1)) {

            tirada1 = dado1.hacerTirada();

            tirada2 = dado2.hacerTirada();

            valor_tiradas = tirada1 + tirada2; //Volvemos a tirar los dados

            System.out.println("Sales de la carcel y vuelves a tirar.");

        }

        Casilla casillainicio = avatar.getLugar();

        avatar.moverAvatar(tablero.getPosiciones(), valor_tiradas);

        Casilla casillafinal = avatar.getLugar();

        if (casillafinal.getPosicion() == 31){
            jugador.getAvatar().moverAvatar(tablero.getPosiciones(), 20);
            jugador.setEnCarcel(true);
            System.out.println("El avatar " + jugador.getAvatar().getId() + " va a la cárcel.");
        }
        else
            System.out.println("El avatar " + avatar.getId() + " avanza " + (tirada1+tirada2) + " posiciones, desde " + casillainicio.getNombre() + " hasta " + casillafinal.getNombre() + ".");


        if (!casillafinal.evaluarCasilla(jugador, banca, valor_tiradas)){
            System.out.printf("El jugador " + jugador.getNombre() + " no puede pagar sus deudas!");
            acabarPartida = true;
        }
    }

    /*Método que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
    * Parámetro: cadena de caracteres con el nombre de la casilla.
     */
    private void comprar(String nombre) {
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'. 
    private void salirCarcel() {
    }

    // Método que realiza las acciones asociadas al comando 'listar enventa'.
    private void listarVenta() {
    }

    // Método que realiza las acciones asociadas al comando 'listar jugadores'.
    private void listarJugadores(){

        for (int i=0;i<obtenerNumeroDeJugadores(); i++){
            
            System.out.println("Nombre: " + jugadores.get(i).getNombre());
            System.out.println("Avatar: " + jugadores.get(i).getAvatar().getID());
            System.out.println("Fortuna: " + jugadores.get(i).getFortuna());
            System.out.println("Propiedades: ||");
            for (int j=0; j<jugadores.get(i).getPropiedades().size(); j++){
                System.out.print(jugadores.get(i).getPropiedades().get(j).getNombre() + " || ");
            }
            System.out.println("");
            System.out.println("Hipotecas: ");
            System.out.println("Edificios: ");
        }

        System.out.println("");
    }

    // Método que realiza las acciones asociadas al comando 'listar avatares'.
    private void listarAvatares() {
    }

    public Jugador obtenerJugadorTurno() {
        return jugadores.get(turno);
    }



    public int obtenerNumeroDeJugadores() {
        return jugadores.size();
    }

    // Método que realiza las acciones asociadas al comando 'acabar turno'.
    private void acabarTurno() {
        turno = (turno + 1) % obtenerNumeroDeJugadores();
        lanzamientos = 0;
    }
}
