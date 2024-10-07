package monopoly;

import java.util.*;
import partida.*;


public class Menu {

    //Atributos
    private ArrayList<Jugador> jugadores; //Jugadores de la partida.
    private ArrayList<Avatar> avatares; //Avatares en la partida.
    private int turno = 0; //Índice correspondiente a la posición en el arrayList del jugador (y el avatar) que tienen el turno
    private int lanzamientos=0; //Variable para contar el número de lanzamientos de un jugador en un turno.
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

        dado1 = new Dado();
        dado2 = new Dado();
        
        System.out.println("Introduce el número de jugadores:");
        int numJugadores = sc.nextInt();
        sc.nextLine();

        Casilla casillaInicio = tablero.getCasilla(0);

        for (int i = 1; i <= numJugadores; i++) {
            System.out.println("Introduce el nombre del jugador " + i + ":");
            String nombre = sc.nextLine();

            System.out.println("Elige el tipo de avatar para " + nombre + " (por ejemplo: coche, sombrero, perro):");
            String tipoAvatar = sc.nextLine();

            Jugador jugador = new Jugador(nombre, tipoAvatar, casillaInicio, avatares);
            jugador.sumarFortuna((float)Valor.FORTUNA_INICIAL);
            jugadores.add(jugador);
            
            avatares.add(jugador.getAvatar());
            casillaInicio.anhadirAvatar(jugador.getAvatar());
            System.out.println("Jugador " + nombre + " con avatar " + tipoAvatar + " creado.");
        }

        tablero.SetCasilla(casillaInicio, 0);
        System.out.println("Partida iniciada con " + numJugadores + " jugadores.");
        System.out.println("Empieza la partida: " + obtenerJugadorTurno().getNombre() + ".");

        

    }

    public void loopJugable(){
        String comando = "";
        Scanner sc = new Scanner(System.in);
        while (!acabarPartida){
            tablero.imprimirTablero();
            
            if (obtenerJugadorTurno().limiteCarcel() && comando.equals("a")){ //a comprobación é solo ao inicio do turno
                if (!obtenerJugadorTurno().pagarMulta()){
                    acabarPartida = true;
                    break;
                }
            }
            comando = sc.nextLine();
            analizarComando(comando);
        }
        System.out.println("\nLa partida ha terminado! El jugador " + obtenerJugadorTurno().getNombre() + " ha declarado la bancarrota.");

    }
    
    /*Método que interpreta el comando introducido y toma la accion correspondiente.
    * Parámetro: cadena de caracteres (el comando).
    */
    private void analizarComando(String comando) {

        Jugador jugador = obtenerJugadorTurno();
        Casilla casilla = obtenerJugadorTurno().getAvatar().getLugar();
        
        //jugador
        if (comando.equals("jugador")){
            System.out.println("Jugador actual: " + jugador.getNombre() + ", con avatar &" + jugador.getAvatar().getID() + ".");

        }
        
        //lanzar dados
        if ( comando.equals("lanzar dados") &&(lanzamientos == 0 || dado1.getValorPrevio() == dado2.getValorPrevio())){
            lanzarDados();
            lanzamientos ++;
        }
        else if (comando.equals("lanzar dados")){
            System.out.println("Śolo se pueden lanzar los dados una vez por turno, a no ser que saques dobles.");
        }

        //comprar
        if (comando.equals("comprar")){
            comprar(casilla.getNombre());
        }
        
        //acabar turno
        if ((comando.equals("acabar turno") &&(lanzamientos != 0)) && (dado1.getValorPrevio() != dado2.getValorPrevio())){
            acabarTurno();
            System.out.println("Turno de " + obtenerJugadorTurno().getNombre() + ".");
            return;
        }
        else if (comando.equals("acabar turno")){
            System.out.println("Debes lanzar los dados.");
        }
        
        //salir carcel
        if (comando.equals("salir carcel") && jugador.getEnCarcel()){
            jugador.pagarMulta();
        }
        else if (comando.equals("salir carcel")){
            System.out.println("No estás en la cárcel.");
        }
       
        //listar avatares
        if (comando.equals("listar avatares")){
            listarAvatares();
        }
        
        //listar jugadores
        if (comando.equals("listar jugadores")){
            listarJugadores();
        }
        
        if (comando.contains("describir")){
            //describir jugador`
            if (comando.contains("describir jugador ")){
                    descJugador(comando.replace("describir jugador ", ""));
                    
            }
            //describir avatar
            else if (comando.contains("describir avatar ")){
                descAvatar(comando.replace("describir avatar ", ""));
            }
            //describir <casilla>
            else {
                descCasilla(comando.replace("describir  ", ""));
            }
        }  
        
/*
 *      DEBUG
 */
        if (comando.contains("m ")){ //movimiento manual (debug)
            lanzarDados(Integer.parseInt(comando.replace("m ", "")));
        }

        if (comando.contains("f ")){ //fortuna manual (debug)
            jugador.sumarFortuna(Float.parseFloat(comando.replace("f ", "")));
            System.out.println("Nueva fortuna: " + jugador.getFortuna());
        }
    }
    

    /*Método que realiza las acciones asociadas al comando 'describir jugador'.
    * Parámetro: nombre del jugador
     */
    //private void descJugador(String[] partes) { TODO: por que recibe o comando enteiro???


    private void descJugador(String nombre) {
        Jugador jugador = getJugador(nombre);
        if (!(jugador == null)){
            System.out.println("Nombre: " + jugador.getNombre());
            System.out.println("Avatar: " + jugador.getAvatar().getID());
            System.out.println("Fortuna: " + jugador.getFortuna());
            System.out.println("Propiedades: ");
            for (int j=0; j<jugador.getPropiedades().size(); j++){
                System.out.print(" ||" + jugador.getPropiedades().get(j).getNombre() + "|| ");
            }
            System.out.println("");
            System.out.println("Hipotecas: ");
            System.out.println("Edificios: ");
            System.out.println("");
        }
        else 
            System.out.println("No existe un avatar con ese ID.");
    }

    
    private void descJugador(Jugador jugador) {
        if (!(jugador == null)){
            System.out.println("Nombre: " + jugador.getNombre());
            System.out.println("Avatar: " + jugador.getAvatar().getID());
            System.out.println("Fortuna: " + jugador.getFortuna());
            System.out.println("Propiedades: ||");
            for (int j=0; j<jugador.getPropiedades().size(); j++){
                System.out.print(jugador.getPropiedades().get(j).getNombre() + " || ");
            }
            System.out.println("");
            System.out.println("Hipotecas: ");
            System.out.println("Edificios: ");
            System.out.println("");
        }
        else 
            System.out.println("No existe un avatar con ese ID.");
    }

    /*Método que realiza las acciones asociadas al comando 'describir avatar'.
    * Parámetro: id del avatar a describir.
    */
    private void descAvatar(String ID) {
        Avatar avatar = getAvatar(ID);
        if (!(avatar==null)){
            System.out.println("- ID: " + avatar.getID());
            System.out.println("- Tipo: " + avatar.getTipo());
            System.out.println("- Casilla: " + avatar.getLugar().getNombre());
            System.out.println("- Jugador: " + avatar.getJugador().getNombre());
            System.out.println("");

        }
        else 
            System.out.println("No existe un avatar con ese ID.");
    }

    /* Método que realiza las acciones asociadas al comando 'describir nombre_casilla'.
    * Parámetros: nombre de la casilla a describir.
    */
    private void descCasilla(String nombre) {
        Casilla casilla = tablero.getCasilla(nombre);
        if (!(casilla ==(null))){
            System.out.println(casilla.infoCasilla(banca));
        }
        else System.out.println("No existe la casilla \'"+nombre+"\'.");
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
    private void lanzarDados() {


        Jugador jugador = obtenerJugadorTurno();

        Avatar avatar = jugador.getAvatar();

        int tirada1 = dado1.hacerTirada();
        int tirada2 = dado2.hacerTirada();
        System.out.println("Has sacado: " + tirada1 + " y " + tirada2+ ".");

        int valor_tiradas = tirada1 + tirada2;
        if (tirada1 == tirada2){
            System.out.println("Dobles!");}

        else if (jugador.getEnCarcel()) {

            System.out.println("Continúas en la carcel.");
            jugador.sumarTiradaCarcel();
            return;}

        if (jugador.getEnCarcel() && (tirada2 == tirada1)) {

            System.out.println("Sales de la carcel y vuelves a tirar.");
            jugador.salirCarcel();

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
            System.out.println("El avatar " + avatar.getId() + " avanza " + (valor_tiradas) + " posiciones, desde " + casillainicio.getNombre() + " hasta " + casillafinal.getNombre() + ".");


        if (!casillafinal.evaluarCasilla(jugador, banca, valor_tiradas)){
            System.out.println("El jugador " + jugador.getNombre() + " no puede pagar sus deudas!");
            acabarPartida = true;
        }
    }

    private void lanzarDados(int valor_tiradas){
        Jugador jugador = obtenerJugadorTurno();

        Avatar avatar = jugador.getAvatar();
        Casilla casillainicio = avatar.getLugar();

        avatar.moverAvatar(tablero.getPosiciones(), valor_tiradas);

        Casilla casillafinal = avatar.getLugar();

        if (casillafinal.getPosicion() == 31){
            jugador.getAvatar().moverAvatar(tablero.getPosiciones(), 20);
            jugador.setEnCarcel(true);
            System.out.println("El avatar " + jugador.getAvatar().getId() + " va a la cárcel.");
        }
        else
            System.out.println("El avatar " + avatar.getId() + " avanza " + (valor_tiradas) + " posiciones, desde " + casillainicio.getNombre() + " hasta " + casillafinal.getNombre() + ".");


        if (!casillafinal.evaluarCasilla(jugador, banca, valor_tiradas)){
            System.out.printf("El jugador " + jugador.getNombre() + " no puede pagar sus deudas!");
            acabarPartida = true;
        }

    }

    /*Método que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
    * Parámetro: cadena de caracteres con el nombre de la casilla.
     */
    private void comprar(String nombre) {
        Jugador jugador = obtenerJugadorTurno();
        Casilla casilla = obtenerJugadorTurno().getAvatar().getLugar();
        
        if (casilla.esComprable(jugador, banca)){
            System.out.println(jugador.getNombre() + " compra la propiedad " + nombre + " por " + casilla.getValor() + ".");
            casilla.comprarCasilla(jugador, banca);
        }
        else {
            System.out.println("No puedes comprar esta casilla.");
        }
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'. 
    private boolean salirCarcel() {
        if(obtenerJugadorTurno().getFortuna() > (Valor.SUMA_VUELTA*0.25)){
            obtenerJugadorTurno().sumarGastos((float)(Valor.SUMA_VUELTA*0.25));
            return true;
        }
        return false;
    }

    // Método que realiza las acciones asociadas al comando 'listar enventa'.
    private void listarVenta() {
    }
    
    
    // Método que realiza las acciones asociadas al comando 'listar jugadores'.
    private void listarJugadores(){
        for (int i=0;i<obtenerNumeroDeJugadores(); i++){ 
            descJugador(jugadores.get(i));
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar avatares'.
    private void listarAvatares() {
        for (int i=0;i<obtenerNumeroDeAvatares(); i++){ 
            descAvatar(avatares.get(i).getID());

        }
    }

    public Jugador obtenerJugadorTurno() {
        return jugadores.get(turno);
    }

    public int obtenerNumeroDeJugadores() {
        return jugadores.size();
    }

    public int obtenerNumeroDeAvatares() {
        return avatares.size();
    }

    public Avatar getAvatar(String id){
        for (int i=0; i<avatares.size(); i++){
            if (avatares.get(i).getID().equals(id))
                return avatares.get(i);
        }
        return null;
    }

    public Jugador getJugador(String nombre){
        for (int i=0; i<jugadores.size(); i++){
            if (jugadores.get(i).getNombre().equals(nombre))
                return jugadores.get(i);
        }
        return null;
    }
    
    // Método que realiza las acciones asociadas al comando 'acabar turno'.
    private void acabarTurno() {
        turno = (turno + 1) % obtenerNumeroDeJugadores();
        lanzamientos = 0;
    }
}
