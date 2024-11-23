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
    private int dobles_seguidos = 0; // Variable para contar el número de dobles seguidos de un jugador en un turno.
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
            } catch (Exception ex) {
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
                Jugador jugador = obtenerJugadorTurno();
                tablero.imprimirTablero();

                if (jugador.getFortuna() < 0) {
                    if (jugador.getEnDeuda() == null) {
                        jugador.setEnDeuda(banca);
                    }
                    System.out.println(
                            Valor.RED + "[AVISO]:" + Valor.RESET
                            + " actualmente estás en deuda (" + obtenerJugadorTurno().getFortuna()
                            + "). Debes destruir edificios, hipotecar propiedades o declarar la bancarrota.");
                }
                if (jugador.limiteCarcel() && comando.equals("a")) { // a comprobación é solo ao inicio do
                    // turno
                    if (!jugador.pagarMulta()) {
                        jugador.setEnDeuda(banca);
                        break;
                    }
                }
                comando = sc.nextLine();
                analizarComando(comando);

                if (jugadores.size() < 2) {
                    System.out.println("El único jugador que queda es " + obtenerJugadorTurno().getNombre() + "!");
                    acabarPartida = true;
                }

            }
            System.out.println("La partida ha terminado! El ganador es " + obtenerJugadorTurno().getNombre() + ".");
        }

    }

    private void anhadirJugador() {

        Casilla casillaInicio = tablero.getCasilla(0);
        System.out.println("\nIntroduce el nombre del jugador " + (obtenerNumeroDeJugadores() + 1) + ": ");
        String nombre = sc.nextLine();
        System.out.println("Elige el tipo de avatar para " + nombre + " (por ejemplo: coche, pelota):");
        String tipoAvatar = sc.nextLine();
        if (tipoAvatar.equals("a") || tipoAvatar.equals("c")) {
            System.out.println(Valor.YELLOW + "El avatar seleccionado es el coche." + Valor.RESET);
            tipoAvatar = "coche";
        }
        if (tipoAvatar.equals("b") || tipoAvatar.equals("p")) {
            System.out.println(Valor.YELLOW + "El avatar seleccionado es la pelota." + Valor.RESET);
            tipoAvatar = "pelota";
        }
        Jugador jugador = new Jugador(nombre, tipoAvatar, casillaInicio, avatares);
        jugador.sumarFortuna((float) Valor.FORTUNA_INICIAL);
        jugadores.add(jugador);
        avatares.add(jugador.getAvatar());
        casillaInicio.anhadirAvatar(jugador.getAvatar());
        System.out.println("Jugador " + nombre + " con avatar " + tipoAvatar + " creado.");

    }

    public void funcionesCartas(Avatar avatar, Tablero tablero, int id) {
        Jugador jugador = avatar.getJugador();

        switch (id) {
            case 1:

                if (avatar.getLugar().getPosicion() <= 6) //trans1 es la casilla 6
                {
                    avatar.moverAvatar(tablero.getPosiciones(), 6 - avatar.getLugar().getPosicion(), true);
                } else {
                    avatar.moverAvatar(tablero.getPosiciones(), 40 - avatar.getLugar().getPosicion() + 6, true);
                }
                break;

            case 2:

                if (avatar.getLugar().getPosicion() <= 27) //Solar15 es la casilla 27
                {
                    avatar.moverAvatar(tablero.getPosiciones(), 27 - avatar.getLugar().getPosicion(), false);
                } else {
                    avatar.moverAvatar(tablero.getPosiciones(), 40 - avatar.getLugar().getPosicion() + 27, false);
                }
                break;

            case 3:

                jugador.sumarFortuna(500000);
                break;

            case 4:

                if (avatar.getLugar().getPosicion() <= 7) //solar3 es la casilla 7
                {
                    avatar.moverAvatar(tablero.getPosiciones(), 7 - avatar.getLugar().getPosicion(), true);
                } else {
                    avatar.moverAvatar(tablero.getPosiciones(), 40 - avatar.getLugar().getPosicion() + 7, true);
                }
                break;

            case 5:

                encarcelar(jugador);
                break;

            case 6:

                jugador.sumarFortuna(1000000);
                break;

            case 7:

                jugador.sumarGastos(500000);
                if (jugador.getFortuna() < 0) {
                    jugador.setFortunaPrevia((500000 + jugador.getFortuna()));
                    System.out.println("No tienes suficiente dinero. Quedas en deuda con el banco.");
                    jugador.setEnDeuda(banca);
                }
                break;

            case 8:
                System.out.println("Vas a la cárcel.");
                encarcelar(jugador);

                break;

            case 9:

                avatar.moverAvatar(tablero.getPosiciones(), 40 - avatar.getLugar().getPosicion() + 1, true);
                break;

            case 10:

                jugador.sumarFortuna(2000000);
                break;

            case 11:

                jugador.sumarGastos(1000000);
                if (jugador.getFortuna() < 0) {
                    jugador.setFortunaPrevia((1000000 + jugador.getFortuna()));
                    System.out.println("No tienes suficiente dinero. Quedas en deuda con el banco.");
                    jugador.setEnDeuda(banca);
                }
                break;

            case 12:

                for (int i = 0; i < jugadores.size(); i++) {
                    if (jugadores.get(i) != jugador) {
                        jugador.sumarGastos(200000);
                        jugadores.get(i).sumarFortuna(200000);

                        if (jugador.getFortuna() < 0) {
                            jugador.setFortunaPrevia((200000 + jugador.getFortuna()));
                            System.out.println("No tienes suficiente dinero. Quedas en deuda con el banco.");
                            // do glosario de dubidas: Na carta de comunidade 6 (Alquilas a tus compañeros una villa en Solar7 durante una semana. Paga 200000€ a cada jugador), 
                            // se o xogador non tén diñeiro para afrontar este pago e decide declararse en bancarrota, toda a súa fortuna e propiedades pasan á banca.
                            jugador.setEnDeuda(banca);
                            break;
                        }
                    }
                }

                break;
        }
        Casilla casillafinal = avatar.getLugar();

        if (!casillafinal.evaluarCasilla(jugador, banca, 0)) { //tirada non importa porque ningunha carta che manda a servicio
            System.out.println("El jugador " + jugador.getNombre() + " no puede pagar sus deudas!");
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
            if (jugadores.size() >= 6) {
                System.out.println("Número de jugadores máximo alcanzado.");
            } else {
                anhadirJugador();
            }
        } else if (comando.equals("cambiar modo")) {
            if (lanzamientos > 0) {
                System.out.println("No puedes cambiar de modo después de lanzar los dados.");
            } else {
                if (jugador.getMovEspecial()) {
                    System.out.println("Cambio a modo estándar.");
                    jugador.setMovEspecial(false);
                } else {
                    System.out.println("Cambio a modo avanzado [" + jugador.getAvatar().getTipo() + "].");
                    jugador.setMovEspecial(true);

                }
            }
        } else if (comando.contains("listar edificios ")) {
            comando = comando.replace("listar edificios ", "");
            try {
                Grupo grupo = tablero.getGrupoNombre(comando);
                grupo.descEdificios();
            } catch (Exception ex) {
                System.out.println("grupo invalido (" + comando + ")");
            }

        } else if (comando.equals("listar edificios")) {
            for (Grupo grupo : tablero.getGrupos()) {
                grupo.descEdificios();
            }
        } // jugador
        else if (comando.equals("jugador")) {
            System.out.println(
                    "Jugador actual: " + jugador.getNombre() + ", con avatar &" + jugador.getAvatar().getID() + ".");
        } // lanzar dados
        else if (comando.equals("lanzar dados")) { //lanzar dados random
            if (puedeLanzarDados()) {
                lanzarDados();
            }
        } else if (comando.contains("lanzar dados ")) { // lanzar dados random
            if (puedeLanzarDados()) {
                try {
                    comando = comando.replace("lanzar dados ", "");
                    String[] valores = comando.split("\\+");
                    int tirada1 = Integer.parseInt(valores[0]);
                    int tirada2 = Integer.parseInt(valores[1]);
                    lanzarDados(tirada1, tirada2);
                } catch (NumberFormatException ex) {
                    System.out.println("Uso del comando: lanzar dados [tirada1]+[tirada2]");
                }
            }
        } else if (comando.equals("avanzar")) {
            if (!jugador.getAvatar().getTipo().equals("pelota") || !jugador.getMovEspecial()) {
                System.out.println("El comando avanzar solo está disponible para el avatar pelota en modo avanzado.");
            } else if (puedeAvanzar()) {
                jugador.getAvatar().avanzar(tablero.getPosiciones(), banca);
            }
            else{
                System.out.println("No puedes avanzar.");
            }
        } else if (comando.equals("comprar")) {
            if (!jugador.getPuedeComprar() && jugador.getAvatar().getTipo().equals("coche") && jugador.getMovEspecial()) {
                System.out.println(
                        "Al realizar el movimiento especial del coche, sólo puedes comprar una vez por turno.");
            } else {
                comprar(casilla.getNombre());
            }
        } // acabar turno
        else if ((comando.equals("acabar turno")) // quitei o de lanzamientos!=0 por comodidadRcom
                ) {
            if (jugador.getFortuna() < 0) {
                System.out.println(
                        "Actualmente estás en deuda. Debes destruir edificios, hipotecar propiedades o declarar la bancarrota.");
            } else {
                acabarTurno();
                if (jugador.getCocheCalado() > 0) {
                    jugador.reducirCocheCalado();
                }
                System.out.println("Turno de " + obtenerJugadorTurno().getNombre() + ".");
            }
        } else if (comando.equals("acabar turno")) {
            System.out.println("Debes lanzar los dados.");
        } // salir carcel
        else if (comando.equals("salir carcel") && jugador.getEnCarcel()) {
            salirCarcel();
        } else if (comando.equals("salir carcel")) {
            System.out.println("No estás en la cárcel.");
        } // listar avatares
        else if (comando.equals("listar avatares")) {
            listarAvatares();
        } // listar jugadores
        else if (comando.equals("listar jugadores")) {
            listarJugadores();
        } // ver tablero
        else if (comando.equals("ver tablero")) {
            // No hace nada

        } else if (comando.contains("describir")) {
            // describir jugador <jugador>
            if (comando.contains("describir jugador ")) {
                descJugador(comando.replace("describir jugador ", ""));
            } else if (comando.contains("describir avatar ")) {
                descAvatar(comando.replace("describir avatar ", ""));
            } else {
                descCasilla(comando.replace("describir ", ""));
            }

        } // listar enventa
        else if (comando.equals("listar enventa")) {
            listarVenta();
        } // declarar bancarrota
        else if (comando.equals("bancarrota")) {
            if (jugador.getEnDeuda() == null) {
                jugador.setEnDeuda(banca);
            }
            if (jugador.getEnDeuda() == null) {
                jugador.setEnDeuda(banca);
            }
            System.out.println("Las propiedades y fortuna de " + jugador.getNombre() + " pasan a pertenecer a "
                    + jugador.getEnDeuda().getNombre() + ".");
            bancarrota(jugador.getEnDeuda());
        } /*
         * DEBUG
         */ else if (comando.contains("m ")) { // movimiento manual (debug)
            try {
                lanzarDados(Integer.parseInt(comando.replace("m ", "")), 0);
                lanzamientos++;
            } catch (NumberFormatException ex) {
                System.out.println("Uso del comando: m [cantidad de casillas]");
            }
        } else if (comando.contains("f ")) { // fortuna manual (debug)
            try {
                String[] parts = comando.split(" ");
                float amount = Float.parseFloat(parts[1]);
                if (parts[1].startsWith("+")) {
                    jugador.sumarFortuna(amount);
                } else if (parts[1].startsWith("-")) {
                    jugador.sumarFortuna(amount);
                } else {
                    jugador.setFortuna(amount);
                }
                System.out.println("Nueva fortuna: " + jugador.getFortuna());
            } catch (NumberFormatException ex) {
                System.out.println("Uso del comando: f [+/-][fortuna]");
            }
        } else if (comando.contains("edificar ")) {
            Set<String> palabrasValidas = new HashSet<>();
            palabrasValidas.add("casa");
            palabrasValidas.add("hotel");
            palabrasValidas.add("piscina");
            palabrasValidas.add("pista");
            palabrasValidas.add("4casas");
            comando = comando.replace("edificar ", "").toLowerCase();
            if (!palabrasValidas.contains(comando)) {
                System.out.println("Edificios válidos: casa, hotel, piscina, pista.");
            } else {
                if (comando.equals("4casas")) {
                    comando = "casa";
                    for (int i = 0; i < 4; i++) {
                        e = new Edificio(comando, casilla);
                        if (casilla.puedeConstruir(e, jugador)) {
                            System.out.println("Has comprado un(a) " + comando + " en " + casilla.getNombre() + ", por "
                                    + casilla.valorEdificio(e.getTipo()) + ".");
                            casilla.anhadirEdificio(e);
                        }
                    }
                } else {
                    e = new Edificio(comando, casilla);
                    if (casilla.puedeConstruir(e, jugador)) {
                        System.out.println("Has comprado un(a) " + comando + " en " + casilla.getNombre() + ", por "
                                + casilla.valorEdificio(e.getTipo()) + ".");
                        casilla.anhadirEdificio(e);
                    }
                }
            }

        } else if (comando.contains("destruir ")) {
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
                        + casilla.valorEdificio(comando) / 2f + ".");
                casilla.destruirEdificio(comando);
                jugador.sumarFortuna(casilla.valorEdificio(comando) / 2f);
            }
        } else if (comando.contains("deshipotecar ")) {
            comando = comando.replace("deshipotecar ", "");
            Casilla aHipotecar;
            aHipotecar = tablero.getCasilla(comando);
            if (aHipotecar == null) {
                System.out.println("Casilla inválida.");
            } else {
                if (aHipotecar.puedeDeshipotecar(jugador)) {
                    aHipotecar.deshipotecar();
                }
            }
        } else if (comando.contains("hipotecar ")) {
            comando = comando.replace("hipotecar ", "");
            Casilla aHipotecar;
            aHipotecar = tablero.getCasilla(comando);
            if (aHipotecar == null) {
                System.out.println("Casilla inválida.");
            } else {
                if (aHipotecar.puedeHipotecar(jugador)) {
                    aHipotecar.hipotecar();
                }
            }
        } else if (comando.equals("estadisticas")) {
            estadisticas();
        } else if (comando.contains("estadisticas ")) {
            comando = comando.replace("estadisticas ", "");
            Jugador jugadorstats = getJugador(comando);
            jugadorstats.estadisticas();

        } else if (comando.contains("carta")) {

            if (jugador.puedeCogerCarta() == 2) {

                HashMap<Integer, Carta> suerte = tablero.getSuerte();

                for (int i = 1; i <= suerte.size(); i++) {
                    Carta carta = suerte.get(i);
                    if (carta != null) {
                        System.out.println(i + ": " + carta.getCarta());
                    } else {
                        System.out.println(i + ": Carta no disponible.");
                    }
                }
                System.out.println("Escoge una carta por su numero: ");
                int opc = sc.nextInt();
                sc.nextLine(); // Consume newline
                System.out.println("Carta seleccionada: " + suerte.get(opc).getCarta());
                funcionesCartas(jugador.getAvatar(), tablero, opc);
                jugador.setCartaDisponible(0);

            } else if (jugador.puedeCogerCarta() == 1) {
                HashMap<Integer, Carta> comunidad = tablero.getComunidad();
                for (int i = 7; i <= 12; i++) {
                    Carta carta = comunidad.get(i);
                    if (carta != null) {
                        System.out.println(i + ": " + carta.getCarta());
                    } else {
                        System.out.println(i + ": Carta no disponible.");
                    }
                }
                System.out.println("Escoge una carta por su numero: ");
                int opc = sc.nextInt();
                sc.nextLine(); // Consume newline
                System.out.println("Carta seleccionada: " + comunidad.get(opc).getCarta());

                funcionesCartas(jugador.getAvatar(), tablero, opc);
                jugador.setCartaDisponible(0);

            } else {
                System.out.println("No puedes coger cartas.");
            }

        } else {
            System.out.println("Comando inválido.");
        }

    }

    /*
     * Método que realiza las acciones asociadas al comando /describir jugador'.
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
                System.out.print("  ||" + jugador.getPropiedades().get(j).getNombre());
                if (jugador.getPropiedades().get(j).getHipotecada()) {
                    System.out.print("[H]");
                }
                System.out.print("||");
            }
            System.out.println("");
            System.out.println("Hipotecas: ");
            /*
             * ArrayList<Edificio> edificios = jugador.getEdificios();
             * if (!edificios.isEmpty()) {
             * System.out.println("- Edificios:\n");
             * for (int i = 0; i < edificios.size(); i++) {
             * System.out.println("   · " + edificios.get(i).getTipo());
             * }
             * }
             */
        } else {
            System.out.println("No existe un jugador con ese nombre.");
        }
    }

    private void descJugador(Jugador jugador) {
        if (!(jugador == null)) {
            System.out.println("Nombre: " + jugador.getNombre());
            System.out.println("Avatar: " + jugador.getAvatar().getID());
            System.out.println("Fortuna: " + jugador.getFortuna());
            System.out.println("Propiedades:");
            for (int j = 0; j < jugador.getPropiedades().size(); j++) {
                System.out.print("  ||" + jugador.getPropiedades().get(j).getNombre());
                if (jugador.getPropiedades().get(j).getHipotecada()) {
                    System.out.print("[H]");
                }
                System.out.print("||");
            }
            System.out.println("");
            System.out.println("Hipotecas: ");
            System.out.println("Edificios: ");
            System.out.println("");
        } else {
            System.out.println("No existe el jugador.");
        }
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

        } else {
            System.out.println("No existe un avatar con ese ID.");
        }
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
        } else {
            System.out.println("No existe la casilla \'" + nombre + "\'.");
        }
    }

    private boolean puedeLanzarDados() {
        Jugador jugador = obtenerJugadorTurno();
        Avatar avatar = jugador.getAvatar();

        if (jugador.getEnCarcel()) {
            if (jugador.getTiradasCarcel() > 2) {
                System.out.println("Has pasado 3 turnos en la cárcel. Debes pagar la multa.");
                return false;
            } else if (lanzamientos > 0) {
                System.out.println("Sólo puedes intentar salir de la cárcel al inicio de tu turno, una vez por turno");
                return false;
            }
        }
        if (lanzamientos > 0 && dobles_seguidos == 0 && (!avatar.getTipo().equals("coche") || !jugador.getMovEspecial())) {
            System.out.println("Ya has lanzado los dados en este turno.");
            return false;
        }
        if (jugador.getCocheCalado() > 0) {
            System.out.println("Por una previa tirada con el coche, no puedes tirar durante " + jugador.getCocheCalado() + " turnos.");
            return false;
        }
        if (avatar.getTipo().equals("coche") && jugador.getMovEspecial() && lanzamientos > 3) {
            if (dobles_seguidos == 0) {
                System.out.println("Ya has lanzado los dados 4 veces en este turno.");
                return false;
            }
            if (lanzamientos > 4) {
                System.out.println("Sacar dobles en la última tirada del coche te permite sólo una tirada adicional.");
                return false;
            }
        }

        if (jugador.getMovEspecial() && avatar.getTipo().equals("pelota")) {
            if (avatar.siguienteMovPelota(false) != 0) {
                System.out.println("Utiliza el comando 'avanzar' para moverte.");
                return false;
            }
            return true; //la primera tirada de la pelota no implica moverse, no es necesario comprobar si se puede avanzar
        }
        return puedeAvanzar();
    }

    //similar a puedeLanzarDados pero comprueba si el avatar puede moverse actualmente, sea con una tirada normal de dados o con avanzar
    private boolean puedeAvanzar() {
        Jugador jugador = obtenerJugadorTurno();
        Avatar avatar = jugador.getAvatar();

        if (jugador.getFortuna() < 0) {
            System.out.println("Actualmente estás en deuda. Debes destruir edificios, hipotecar propiedades o declarar la bancarrota.");
            return false;
        }
        if (jugador.puedeCogerCarta() != 0) {
            System.out.println("Debes coger la carta primero.");
            return false;
        }

        if (jugador.getMovEspecial() && avatar.getTipo().equals("pelota") && avatar.siguienteMovPelota(false) == 0) {
            return false; //no puede avanzar más
        }
        return true;
    }

    // Método que ejecuta todas las acciones relacionadas con el comando 'lanzar'
    // elejiendo los valores de los dados
    private void lanzarDados() {
        int tirada1 = dado1.hacerTirada();
        int tirada2 = dado2.hacerTirada();
        lanzarDados(tirada1, tirada2);
    }

    // Método que ejecuta todas las acciones relacionadas con el comando 'lanzar
    // dados' para valores de tirada concretos
    private void lanzarDados(int tirada1, int tirada2) {

        Jugador jugador = obtenerJugadorTurno();

        Avatar avatar = jugador.getAvatar();

        System.out.println("Has sacado: " + tirada1 + " y " + tirada2 + ".");

        int valor_tiradas = tirada1 + tirada2;
        lanzamientos++;
        jugador.addTiradas(1);
        //manejo de dobles
        if (tirada1 == tirada2) {
            System.out.println("Dobles!");
            dobles_seguidos++;
            if (jugador.getEnCarcel()) {
                System.out.println("Sales de la carcel y vuelves a tirar.");
                jugador.salirCarcel();
            }
            if (dobles_seguidos == 3) {
                System.out.println("Has sacado dobles 3 veces seguidas! Vas a la carcel.");
                encarcelar(jugador);
                return;
            }
        } else {
            dobles_seguidos = 0;
            if (jugador.getEnCarcel()) {
                jugador.sumarTiradaCarcel();
                System.out.println("Continúas en la carcel. (Tiradas en cárcel: " + jugador.getTiradasCarcel() + ")");
                return;
            }
        }

        //manejo de movimientos especiales
        if (jugador.getMovEspecial()) {
            if (avatar.getTipo().equals("pelota")) {
                avatar.moverPelota(tablero.getPosiciones(), valor_tiradas);
                System.out.println("Utiliza el comando 'avanzar' para moverte.");
            }
            if (avatar.getTipo().equals("coche")) {
                avatar.moverCoche(tablero.getPosiciones(), valor_tiradas);
                if (valor_tiradas > 4 && lanzamientos < 4) {
                    System.out.println("Tu tirada continúa! Puedes volver a lanzar los dados.");
                }
                if (valor_tiradas == 4 && lanzamientos == 4) {
                    System.out.println("Has sacado dobles en la última tirada! Puedes volver a lanzar los dados.");
                }
            }
        } else {
            avatar.moverAvatar(tablero.getPosiciones(), valor_tiradas, true);
        }

        //llamada a evaluar casilla
        Casilla casillaFinal = avatar.getLugar();
        if (!(jugador.getMovEspecial() && avatar.getTipo().equals("pelota"))) //porque el (primer) movimiento especial de la pelota no implica moverse, no se evalua la casilla
            casillaFinal.evaluarCasilla(jugador, banca, valor_tiradas);
        if (jugador.getEnCarcel()) {
            encarcelar(jugador); //comprobación de si cae en 'ir a cárcel'

        }
        comprobacion4Vueltas();

    }

    private void comprobacion4Vueltas() {

        Jugador jugador = obtenerJugadorTurno();
        Avatar avatar = jugador.getAvatar();
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
        if (lanzamientos == 0) {
            return obtenerJugadorTurno().pagarMulta();
        } else {
            System.out.println("Sólo puedes pagar la multa al inicio del turno.");
        }
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
            if (avatares.get(i).getID().equals(id)) {
                return avatares.get(i);
            }
        }
        return null;
    }

    public Jugador getJugador(String nombre) {
        for (int i = 0; i < jugadores.size(); i++) {
            if (jugadores.get(i).getNombre().equals(nombre)) {
                return jugadores.get(i);
            }
        }
        return null;
    }

    // Método que realiza las acciones asociadas al comando 'acabar turno'.
    private void acabarTurno() {
        turno = (turno + 1) % obtenerNumeroDeJugadores();
        obtenerJugadorTurno().setPuedeComprar(true);
        lanzamientos = 0;
        dobles_seguidos = 0;
    }

    private void bancarrota(Jugador jugador) {
        Jugador jugadorTurno = obtenerJugadorTurno();

        if (jugadorTurno.getEnDeuda() == null) {
            jugadorTurno.setEnDeuda(banca);
        }
        ArrayList<Casilla> array_propiedades;
        Casilla casilla = obtenerJugadorTurno().getAvatar().getLugar();
        System.out.println(
                "El jugador " + jugadorTurno.getNombre() + " ha declarado la bancarrota y abandona la partida!");

        array_propiedades = jugadorTurno.getPropiedades();

        for (int i = 0; i < array_propiedades.size(); i++) {
            array_propiedades.get(i).getEdificios().clear();
            if (jugador != banca) {
                jugador.anhadirPropiedad(array_propiedades.get(i));

            }
        }
        if (!jugador.equals(banca)) {
            jugador.sumarFortuna(jugadorTurno.getFortunaPrevia());
            System.out.println("El jugador " + jugador.getNombre() + " recibe los " + jugadorTurno.getFortunaPrevia()
                    + " que tenía " + jugadorTurno.getNombre() + ".");
        }
        jugadores.remove(jugadorTurno);
        avatares.remove(jugadorTurno.getAvatar());
        casilla.getAvatares().remove(jugadorTurno.getAvatar());

        if (turno > jugadores.size() - 1) {
            turno = 0;
        }
        jugadorTurno = obtenerJugadorTurno();
        System.out.println("Turno de " + jugadorTurno.getNombre() + ".");

    }

    private String jugadoresMasVueltas() {
        int max = 0;
        List<String> jugadoresEmpatados = new ArrayList<>();

        for (Jugador jugador : jugadores) {
            int vueltas = jugador.getVueltas();
            if (vueltas > max) {
                max = vueltas;
                jugadoresEmpatados.clear();
                jugadoresEmpatados.add(jugador.getNombre());
            } else if (vueltas == max) {
                jugadoresEmpatados.add(jugador.getNombre());
            }
        }

        if (!jugadoresEmpatados.isEmpty() && max != 0) {
            return String.join(", ", jugadoresEmpatados);
        } else {
            return "Los jugadores no han dado ninguna vuelta aún.";
        }
    }

    private String casillasMasRentables() {
        float max = 0;
        List<String> casillasEmpatadas = new ArrayList<>();

        for (int i = 0; i < 40; i++) {
            float rentabilidad = tablero.getCasilla(i).GetRentabilidad();
            if (rentabilidad > max) {
                max = rentabilidad;
                casillasEmpatadas.clear();
                casillasEmpatadas.add(tablero.getCasilla(i).getNombre());
            } else if (rentabilidad == max) {
                casillasEmpatadas.add(tablero.getCasilla(i).getNombre());
            }
        }

        if (casillasEmpatadas.isEmpty() || max == 0) {
            return "No hay casillas rentables aún.";
        }
        return String.join(", ", casillasEmpatadas);

    }

    private String gruposMasRentables() {
        double max = 0;
        List<String> gruposEmpatados = new ArrayList<>();

        for (Grupo grupo : tablero.getGrupos()) {
            double rentabilidadGrupo = grupo.getRentabilidadGrupo();
            if (rentabilidadGrupo > max) {
                max = rentabilidadGrupo;
                gruposEmpatados.clear();
                gruposEmpatados.add(grupo.getNombre());
            } else if (rentabilidadGrupo == max) {
                gruposEmpatados.add(grupo.getNombre());
            }
        }

        if (!gruposEmpatados.isEmpty() && max != 0) {
            return String.join(", ", gruposEmpatados);
        } else {
            return "No hay grupos rentables aún.";
        }
    }

    public String casillasMasFrecuentadas() {
        int max = 0;
        List<String> casillasEmpatadas = new ArrayList<>();

        for (int i = 0; i < 40; i++) {
            int visitas = tablero.getCasilla(i).getVisitas();
            if (visitas > max) {
                max = visitas;
                casillasEmpatadas.clear();
                casillasEmpatadas.add(tablero.getCasilla(i).getNombre());
            } else if (visitas == max) {
                casillasEmpatadas.add(tablero.getCasilla(i).getNombre());
            }
        }

        if (!casillasEmpatadas.isEmpty() && max != 0) {

            return String.join(", ", casillasEmpatadas);
        } else {
            return "No hay casillas visitadas aún.";
        }
    }

    public String jugadoresMasVecesDados() {
        int max = 0;
        List<String> jugadoresEmpatados = new ArrayList<>();

        for (Jugador jugador : jugadores) {
            int tiradas = jugador.getTiradas();
            if (tiradas > max) {
                max = tiradas;
                jugadoresEmpatados.clear();
                jugadoresEmpatados.add(jugador.getNombre());
            } else if (tiradas == max) {
                jugadoresEmpatados.add(jugador.getNombre());
            }
        }

        if (!jugadoresEmpatados.isEmpty() && max != 0) {
            return String.join(", ", jugadoresEmpatados);
        } else {
            return "Nadie ha lanzado dados aún.";
        }
    }

    public String jugadoresEnCabeza() {
        double max = 0;
        List<String> jugadoresEmpatados = new ArrayList<>();

        for (Jugador jugador : jugadores) {
            float fortuna = jugador.getEnCabeza();
            if (fortuna > max) {
                max = fortuna;
                jugadoresEmpatados.clear();
                jugadoresEmpatados.add(jugador.getNombre());
            } else if (fortuna == max) {
                jugadoresEmpatados.add(jugador.getNombre());
            }
        }

        if (!jugadoresEmpatados.isEmpty()) {
            return String.join(", ", jugadoresEmpatados);
        } else {
            return "No hay jugadores en cabeza.";
        }
    }

    private void estadisticas() {
        System.out.println("casillaMasRentable: " + casillasMasRentables());
        System.out.println("grupoMasRentable: " + gruposMasRentables());
        System.out.println("casillaMasFrecuentada: " + casillasMasFrecuentadas());
        System.out.println("jugadorMasVueltas: " + jugadoresMasVueltas());
        System.out.println("jugadorMasVecesDados: " + jugadoresMasVecesDados());
        System.out.println("jugadorEnCabeza: " + jugadoresEnCabeza());
    }

    private void encarcelar(Jugador jugador) {
        jugador.setEnCarcel(true);
        jugador.setTiradasCarcel(0);
        jugador.getAvatar().setLugar(tablero.getPosiciones(), 10);
        if (jugador.getAvatar().siguienteMovPelota(false) != 0) {
            jugador.getAvatar().resetMovPelota();
            System.out.println("Tu tirada de la pelota ha sido interrumpida por haber caído en la cárcel.");
        }
    }
}
