package monopoly;

import java.util.*;
import partida.*;

public final class Menu {

    // Atributos
    private ArrayList<Jugador> jugadores; // Jugadores de la partida.
    private ArrayList<Avatar> avatares; // Avatares en la partida.
    private int turno = 0; // Índice correspondiente a la posición en el arrayList del jugador (y el
                           // avatar) que tienen el turno
    private int lanzamientos = 0; // Variable para contar el número de lanzamientos de un jugador en un turno.
    private final Tablero tablero; // Tablero en el que se juega.
    private Dado dado1; // Dos dados para lanzar y avanzar casillas.
    private Dado dado2;
    private final Jugador banca; // El jugador banca.
    private boolean acabarPartida; // Booleano para comprobar si hai que acabar la partida.
    private final Scanner sc = new Scanner(System.in);
    private Edificio e;

    public Jugador getBanca() {
        return banca;
    }

    public Menu() {
        this.banca = new Jugador();
        this.tablero = new Tablero(getBanca());

    }

    public void titulo() {

        System.out.println(
                "      ___          ___          ___          ___          ___          ___          ___   ___     ");
        System.out.println(
                "     /\\__\\        /\\  \\        /\\__\\        /\\  \\        /\\  \\        /\\  \\        /\\__\\ |\\__\\    ");
        System.out.println(
                "    /::|  |      /::\\  \\      /::|  |      /::\\  \\      /::\\  \\      /::\\  \\      /:/  / |:|  |   ");
        System.out.println(
                "   /:|:|  |     /:/\\:\\  \\    /:|:|  |     /:/\\:\\  \\    /:/\\:\\  \\    /:/\\:\\  \\    /:/  /  |:|  |   ");
        System.out.println(
                "  /:/|:|__|__  /:/  \\:\\  \\  /:/|:|  |__  /:/  \\:\\  \\  /::\\~\\:\\  \\  /:/  \\:\\  \\  /:/  /   |:|__|__ ");
        System.out.println(
                " /:/ |::::\\__/\\:/__/ \\:\\__/\\:/ |:| /\\__/\\:/__/ \\:\\__/\\:/\\:\\ \\:\\__/\\:/__/ \\:\\__/\\:/__/    /::::\\__\\");
        System.out.println(
                " \\/__/~~/:/  /\\:\\  \\ /:/  /\\/__|:|/:/  /\\:\\  \\ /:/  /\\/__\\:\\/:/  /\\:\\  \\ /:/  /\\:\\  \\   /:/~~/~   ");
        System.out.println(
                "       /:/  /  \\:\\  /:/  /     |:/:/  /  \\:\\  /:/  /      \\::/  /  \\:\\  /:/  /  \\:\\  \\ /:/  /     ");
        System.out.println(
                "      /:/  /    \\:\\/:/  /      |::/  /    \\:\\/:/  /        \\/__/    \\:\\/:/  /    \\:\\  \\/__/      ");
        System.out.println(
                "     /:/  /      \\::/  /       /:/  /      \\::/  /                   \\::/  /      \\:\\__\\          ");
        System.out.println(
                "     \\/__/        \\/__/        \\/__/        \\/__/                     \\/__/        \\/__/          ");
        System.out.println(
                "                                ___       ___          ___          ___                           ");
        System.out.println(
                "                               /\\  \\     /\\  \\        /\\  \\        /\\  \\                          ");
        System.out.println(
                "                              /::\\  \\    \\:\\  \\      /::\\  \\      /::\\  \\                         ");
        System.out.println(
                "                            /::\\~\\:\\  \\   /::\\  \\  _\\:\\~\\ \\  \\  /::\\~\\:\\  \\                       ");
        System.out.println(
                "                           /:/\\:\\ \\:\\__/ /:/\\:\\__/\\/ \\:\\ \\ \\__/\\:/\\:\\ \\:\\__/                      ");
        System.out.println(
                "                           \\:\\~\\:\\ \\/__//:/  \\/__/\\:\\ \\:\\ \\/__/\\:\\~\\:\\ \\/__/                      ");
        System.out.println(
                "                            \\:\\ \\:\\__\\ /:/  /      \\:\\ \\:\\__\\   \\:\\ \\:\\__\\                        ");
        System.out.println(
                "                             \\:\\ \\/__/ \\/__/        \\:\\/:/  /    \\:\\ \\/__/                        ");
        System.out.println(
                "                              \\:\\__\\                 \\::/  /      \\:\\__\\                          ");
        System.out.println(
                "                               \\/__/                  \\/__/        \\/__/                          ");

        System.out.println("\n\n\n                                Pulse ENTER para iniciar una partida.\n\n");
        sc.nextLine();

    }

    // Método para inciar una partida: crea los jugadores y avatares.
    public void iniciarPartida(Tablero t) {
        int numJugadores = 0;
        jugadores = new ArrayList<>();
        avatares = new ArrayList<>();

        dado1 = new Dado();
        dado2 = new Dado();

        while (numJugadores < 2 || numJugadores > 6) {
            try {
                System.out.println("Introduzca el número de jugadores (2-6):");
                numJugadores = sc.nextInt();
            } catch (Exception e) {
                numJugadores = 0;
                sc.nextLine();

            }
        }

        sc.nextLine();
        for (int i = 1; (i <= numJugadores); i++) {
            anhadirJugador();
        }

        System.out.println("Partida iniciada con " + numJugadores + " jugadores.");
        System.out.println("Empieza la partida: " + obtenerJugadorTurno().getNombre() + ".");

    }

    public void loopJugable() {
        try (sc) {
            String comando = "";
            while (!acabarPartida) {
                tablero.imprimirTablero();

                if (obtenerJugadorTurno().limiteCarcel() && comando.equals("a")) { // a comprobación é solo ao inicio do
                    // turno
                    if (!obtenerJugadorTurno().pagarMulta()) {
                        acabarPartida = true;
                        break;
                    }
                }
                comando = sc.nextLine();
                analizarComando(comando);

            }
            System.out.println("\nLa partida ha terminado! El jugador " + obtenerJugadorTurno().getNombre()
                    + " ha declarado la bancarrota.");
        }

    }

    private void anhadirJugador() {

        Casilla casillaInicio = tablero.getCasilla(0);
        System.out.println("\nIntroduce el nombre del jugador " + (obtenerNumeroDeJugadores() + 1) + ": ");
        String nombre = sc.nextLine();
        System.out.println("Elige el tipo de avatar para " + nombre + " (por ejemplo: coche, sombrero, perro):");
        String tipoAvatar = sc.nextLine();
        Jugador jugador = new Jugador(nombre, tipoAvatar, casillaInicio, avatares);
        jugador.sumarFortuna((float) Valor.FORTUNA_INICIAL);
        jugadores.add(jugador);
        avatares.add(jugador.getAvatar());
        casillaInicio.anhadirAvatar(jugador.getAvatar());
        System.out.println("Jugador " + nombre + " con avatar " + tipoAvatar + " creado.");

    }

    /*
     * Método que interpreta el comando introducido y toma la accion
     * correspondiente.
     * Parámetro: cadena de caracteres (el comando).
     */
    private void analizarComando(String comando) {

        Jugador jugador = obtenerJugadorTurno();
        Casilla casilla = obtenerJugadorTurno().getAvatar().getLugar();
        // añadir jugador
        if (comando.equals("añadir jugador")) {
            if (jugadores.size() >= 6)
                System.out.println("Número de jugadores máximo alcanzado.");

            else
                anhadirJugador();
        }

        // jugador
        else if (comando.equals("jugador")) {
            System.out.println(
                    "Jugador actual: " + jugador.getNombre() + ", con avatar &" + jugador.getAvatar().getID() + ".");
        }

        // lanzar dados
        else if (comando.equals("lanzar dados")
                && (lanzamientos == 0 || dado1.getValorPrevio() == dado2.getValorPrevio())) {
            lanzarDados();
            lanzamientos++;
        } else if (comando.equals("lanzar dados")) {
            System.out.println("Śolo se pueden lanzar los dados una vez por turno, a no ser que saques dobles.");
        }

        // comprar
        else if (comando.equals("comprar")) {
            comprar(casilla.getNombre());
        }

        // acabar turno
        else if ((comando.equals("acabar turno")) // quitei o de lanzamientos!=0 por comodidad
                ) {
            acabarTurno();
            System.out.println("Turno de " + obtenerJugadorTurno().getNombre() + ".");
        } else if (comando.equals("acabar turno")) {
            System.out.println("Debes lanzar los dados.");
        }

        // salir carcel
        else if (comando.equals("salir carcel") && jugador.getEnCarcel()) {
            salirCarcel();
        } else if (comando.equals("salir carcel")) {
            System.out.println("No estás en la cárcel.");
        }

        // listar avatares
        else if (comando.equals("listar avatares")) {
            listarAvatares();
        }

        // listar jugadores
        else if (comando.equals("listar jugadores")) {
            listarJugadores();
        }

        // ver tablero
        else if (comando.equals("ver tablero")) {
            // No hace nada

        }

        else if (comando.contains("describir")) {
            // describir jugador <jugador>
            if (comando.contains("describir jugador "))
                descJugador(comando.replace("describir jugador ", ""));

            else if (comando.contains("describir avatar "))
                descAvatar(comando.replace("describir avatar ", ""));

            else
                descCasilla(comando.replace("describir ", ""));

        }

        // listar enventa
        else if (comando.equals("listar enventa")) {
            listarVenta();
        }

        //declarar bancarrota
        else if (comando.equals("bancarrota")){
            bancarrota(banca);
        }

        /*
         * DEBUG
         */
        else if (comando.contains("m ")) { // movimiento manual (debug)
            try {
                lanzarDados(Integer.parseInt(comando.replace("m ", "")));
                lanzamientos++;
            } catch (NumberFormatException e) {
                System.out.println("Uso del comando: m [cantidad de casillas]");
            }
        }

        else if (comando.contains("f ")) { // fortuna manual (debug)

            try {
                jugador.sumarFortuna(Float.parseFloat(comando.replace("f ", "")));
                System.out.println("Nueva fortuna: " + jugador.getFortuna());
            } catch (NumberFormatException e) {
                System.out.println("Uso del comando: f [fortuna]");
            }
        }

        else if (comando.contains("edificar ")) {
            Set<String> palabrasValidas = new HashSet<>();
            palabrasValidas.add("casa");
            palabrasValidas.add("hotel");
            palabrasValidas.add("piscina");
            palabrasValidas.add("pista");
            comando = comando.replace("edificar ", "").toLowerCase();
            if (!palabrasValidas.contains(comando)) {
                System.out.println("Edificios válidos: casa, hotel, piscina, pista.");
            } else {
                e = new Edificio(comando);
                if (casilla.puedeConstruir(e, jugador)) {
                    System.out.println("Has comprado un(a) " + comando + " en " + casilla.getNombre() + ", por "
                            + casilla.valorEdificio(e.getTipo()) + ".");
                    casilla.anhadirEdificio(e);
                }
            }
        } 
        else if (comando.contains("destruir ")) {
            Set<String> palabrasValidas = new HashSet<>();
            palabrasValidas.add("casa");
            palabrasValidas.add("hotel");
            palabrasValidas.add("piscina");
            palabrasValidas.add("pista");
            comando = comando.replace("destruir ", "").toLowerCase();
            if (!palabrasValidas.contains(comando)) {
                System.out.println("Edificios válidos: casa, hotel, piscina, pista.");
            } else {
                System.out.println("Has vendido un(a) " + comando + " en " + casilla.getNombre() + ", por "
                + casilla.valorEdificio(comando)/2f + ".");                
                casilla.destruirEdificio(comando);
                jugador.sumarFortuna(casilla.valorEdificio(comando)/2f);
            }
        } 
        else
            System.out.println("Comando inválido.");

    }

    /*
     * Método que realiza las acciones asociadas al comando 'describir jugador'.
     * Parámetro: nombre del jugador
     */
    private void descJugador(String nombre) {
        Jugador jugador = getJugador(nombre);
        if (!(jugador == null)) {
            System.out.println("Nombre: " + jugador.getNombre());
            System.out.println("Avatar: " + jugador.getAvatar().getID());
            System.out.println("Fortuna: " + jugador.getFortuna());
            System.out.println("Propiedades: ");
            for (int j = 0; j < jugador.getPropiedades().size(); j++) {
                System.out.print(" ||" + jugador.getPropiedades().get(j).getNombre() + "|| ");
            }
            System.out.println("");
            System.out.println("Hipotecas: ");
           /*  ArrayList<Edificio> edificios = jugador.getEdificios();
            if (!edificios.isEmpty()) {
                System.out.println("- Edificios:\n");
                for (int i = 0; i < edificios.size(); i++) {
                    System.out.println("   · " + edificios.get(i).getTipo());
                }
            }*/
        } else
            System.out.println("No existe un jugador con ese nombre.");
    }

    private void descJugador(Jugador jugador) {
        if (!(jugador == null)) {
            System.out.println("Nombre: " + jugador.getNombre());
            System.out.println("Avatar: " + jugador.getAvatar().getID());
            System.out.println("Fortuna: " + jugador.getFortuna());
            System.out.println("Propiedades: ||");
            for (int j = 0; j < jugador.getPropiedades().size(); j++) {
                System.out.print(jugador.getPropiedades().get(j).getNombre() + " || ");
            }
            System.out.println("");
            System.out.println("Hipotecas: ");
            System.out.println("Edificios: ");
            System.out.println("");
        } else
            System.out.println("No existe el jugador.");
    }

    /*
     * Método que realiza las acciones asociadas al comando 'describir avatar'.
     * Parámetro: id del avatar a describir.
     */
    private void descAvatar(String ID) {
        Avatar avatar = getAvatar(ID);
        if (!(avatar == null)) {
            System.out.println("- ID: " + avatar.getID());
            System.out.println("- Tipo: " + avatar.getTipo());
            System.out.println("- Casilla: " + avatar.getLugar().getNombre());
            System.out.println("- Jugador: " + avatar.getJugador().getNombre());
            System.out.println("");

        } else
            System.out.println("No existe un avatar con ese ID.");
    }

    /*
     * Método que realiza las acciones asociadas al comando 'describir
     * nombre_casilla'.
     * Parámetros: nombre de la casilla a describir.
     */
    private void descCasilla(String nombre) {
        Casilla casilla = tablero.getCasilla(nombre);
        if (!(casilla == (null))) {
            System.out.println(casilla.infoCasilla(banca));
        } else
            System.out.println("No existe la casilla \'" + nombre + "\'.");
    }

    // Método que ejecuta todas las acciones relacionadas con el comando 'lanzar
    // dados'.
    private void lanzarDados() {

        Jugador jugador = obtenerJugadorTurno();

        Avatar avatar = jugador.getAvatar();

        int tirada1 = dado1.hacerTirada();
        int tirada2 = dado2.hacerTirada();
        System.out.println("Has sacado: " + tirada1 + " y " + tirada2 + ".");

        int valor_tiradas = tirada1 + tirada2;
        if (tirada1 == tirada2) {
            System.out.println("Dobles!");
        }

        else if (jugador.getEnCarcel()) {

            System.out.println("Continúas en la carcel.");
            jugador.sumarTiradaCarcel();
            return;
        }

        if (jugador.getEnCarcel() && (tirada2 == tirada1)) {
            System.out.println("Sales de la carcel y vuelves a tirar.");
            jugador.salirCarcel();

        }

        Casilla casillainicio = avatar.getLugar();

        avatar.moverAvatar(tablero.getPosiciones(), valor_tiradas);

        Casilla casillafinal = avatar.getLugar();

        System.out.println("El avatar " + avatar.getID() + " avanza " + (valor_tiradas) + " posiciones, desde "
                + casillainicio.getNombre() + " hasta " + casillafinal.getNombre() + ".");

        if (!casillafinal.evaluarCasilla(jugador, banca, valor_tiradas)) {
            System.out.println("El jugador " + jugador.getNombre() + " no puede pagar sus deudas!");
            acabarPartida = true;
        }

        if (casillafinal.getPosicion() == 31) {
            jugador.getAvatar().setLugar(tablero.getPosiciones(), 10);
            jugador.setEnCarcel(true);
        }

        if (avatar.get4Voltas() == true) {
            boolean condicion = true;
            for (int i = 0; i < jugadores.size(); i++) {
                if (jugadores.get(i).getVueltas() < jugador.getVueltas()) {
                    condicion = false;
                }
            }
            if (condicion == true) {
                System.out.println(("Todos los jugadores han dado 4 vueltas! El precio de las propiedades aumenta."));
                tablero.aumentarCoste(banca);
            }
        }

    }

    private void lanzarDados(int valor_tiradas) {
        Jugador jugador = obtenerJugadorTurno();

        Avatar avatar = jugador.getAvatar();
        Casilla casillainicio = avatar.getLugar();

        avatar.moverAvatar(tablero.getPosiciones(), valor_tiradas);

        Casilla casillafinal = avatar.getLugar();
        System.out.println("El avatar " + avatar.getID() + " avanza " + (valor_tiradas) + " posiciones, desde "
                + casillainicio.getNombre() + " hasta " + casillafinal.getNombre() + ".");

        if (!casillafinal.evaluarCasilla(jugador, banca, valor_tiradas)) {
            System.out.println("El jugador " + jugador.getNombre() + " no puede pagar sus deudas!");
            acabarPartida = true;
        }

        if (casillafinal.getPosicion() == 31) {
            jugador.getAvatar().setLugar(tablero.getPosiciones(), 10);
            jugador.setEnCarcel(true);
        }
        if (avatar.get4Voltas() == true) {
            boolean condicion = true;
            for (int i = 0; i < jugadores.size(); i++) {
                if (jugadores.get(i).getVueltas() < jugador.getVueltas()) {
                    condicion = false;
                }
            }
            if (condicion == true) {
                System.out.println(("Todos los jugadores han dado 4 vueltas! El precio de los solares aumenta."));
                tablero.aumentarCoste(banca);
            }
        }

    }

    /*
     * Método que ejecuta todas las acciones realizadas con el comando 'comprar
     * nombre_casilla'.
     * Parámetro: cadena de caracteres con el nombre de la casilla.
     */
    private void comprar(String nombre) {
        Jugador jugador = obtenerJugadorTurno();
        Casilla casilla = obtenerJugadorTurno().getAvatar().getLugar();

        if (casilla.esComprable(jugador, banca)) {
            System.out.println(
                    jugador.getNombre() + " compra la propiedad " + nombre + " por " + casilla.getValor() + ".");
            casilla.comprarCasilla(jugador, banca);
        } else {
            System.out.println("No puedes comprar esta casilla.");
        }
    }

    // Método que ejecuta todas las acciones relacionadas con el comando 'salir
    // carcel'.
    private boolean salirCarcel() {
        if (lanzamientos == 0)
            return obtenerJugadorTurno().pagarMulta();
        else
            System.out.println("Sólo puedes pagar la multa al inicio del turno.");
        return true;
    }

    // Método que realiza las acciones asociadas al comando 'listar enventa'.
    private void listarVenta() {
        banca.getPropiedades();
        Casilla casilla_aux;
        for (int i = 0; i < 40; i++) {
            casilla_aux = tablero.getCasilla(i);
            if ((casilla_aux.getTipo().equals("solar") || casilla_aux.getTipo().equals("transporte")
                    || casilla_aux.getTipo().equals("servicio")) && casilla_aux.getduenhoJugador() == banca) {
                System.out.println(casilla_aux.casEnVenta());
            }
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar jugadores'.
    private void listarJugadores() {
        for (int i = 0; i < obtenerNumeroDeJugadores(); i++) {
            descJugador(jugadores.get(i));
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar avatares'.
    private void listarAvatares() {
        for (int i = 0; i < obtenerNumeroDeAvatares(); i++) {
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

    public Avatar getAvatar(String id) {
        for (int i = 0; i < avatares.size(); i++) {
            if (avatares.get(i).getID().equals(id))
                return avatares.get(i);
        }
        return null;
    }

    public Jugador getJugador(String nombre) {
        for (int i = 0; i < jugadores.size(); i++) {
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

    private void bancarrota(Jugador jugador){
        Jugador jugadorTurno = obtenerJugadorTurno();
        ArrayList<Casilla> array_propiedades;
        Casilla casilla = obtenerJugadorTurno().getAvatar().getLugar();

        array_propiedades = jugadorTurno.getPropiedades();
        
        for (int i=0; i<array_propiedades.size(); i++){
            
            if (jugador == banca){
                array_propiedades.get(i).getEdificios().clear();
            }
            array_propiedades.get(i).setDuenho(jugador);
        }
        jugadores.remove(jugadorTurno);
        avatares.remove(jugadorTurno.getAvatar());
        casilla.getAvatares().remove(jugadorTurno.getAvatar());
        
        
    }
}
