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
    private boolean dobles_seguidos_check = false;
    private int tirada_anterior1; // Variable que guarda el valor de la tirada1 anterior (debug)
    private int tirada_anterior2; // Variable que guarda el valor de la tirada2 anterior (debug)
    private final Tablero tablero; // Tablero en el que se juega.
    private Dado dado1; // Dos dados para lanzar y avanzar casillas.
    private Dado dado2;
    private final Jugador banca; // El jugador banca.
    private boolean acabarPartida; // Booleano para comprobar si hai que acabar la partida.
    private final Scanner sc = new Scanner(System.in);
    private Edificio e;

    private int puedeCogerCarta; // 0 - no, 1 - suerte, 2 - comunidad

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
                    if (jugador.enDeuda == null) {
                        jugador.enDeuda = banca;
                    }
                    System.out.println(
                            Valor.RED + "[AVISO]:" + Valor.RESET
                                    + " actualmente estás en deuda (" + obtenerJugadorTurno().getFortuna()
                                    + "). Debes destruir edificios, hipotecar propiedades o declarar la bancarrota.");
                }
                if (jugador.limiteCarcel() && comando.equals("a")) { // a comprobación é solo ao inicio do
                    // turno
                    if (!jugador.pagarMulta()) {
                        jugador.enDeuda = banca;
                        break;
                    }
                }
                comando = sc.nextLine();
                analizarComando(comando);

                if (jugador.getAvatar().puedeCogerCarta == 2) {
                    System.out.println("Puedes coger una carta de suerte.");
                } else if (jugador.getAvatar().puedeCogerCarta == 1) {
                    System.out.println("Puedes coger una carta de comunidad.");
                }

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

                if (avatar.getLugar().getPosicion() <= 6) // trans1 es la casilla 6
                {
                    avatar.moverAvatar(tablero.getPosiciones(), 6 - avatar.getLugar().getPosicion(), true);
                } else {
                    avatar.moverAvatar(tablero.getPosiciones(), 40 - avatar.getLugar().getPosicion() + 6, true);
                }
                break;

            case 2:

                if (avatar.getLugar().getPosicion() <= 27) // Solar15 es la casilla 27
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

                if (avatar.getLugar().getPosicion() <= 7) // solar3 es la casilla 7
                {
                    avatar.moverAvatar(tablero.getPosiciones(), 7 - avatar.getLugar().getPosicion(), true);
                } else {
                    avatar.moverAvatar(tablero.getPosiciones(), 40 - avatar.getLugar().getPosicion() + 7, true);
                }
                break;

            case 5:

                avatar.setLugar(tablero.getPosiciones(), 10);
                jugador.setEnCarcel(true);
                break;

            case 6:

                jugador.sumarFortuna(1000000);
                break;

            case 7:

                jugador.sumarGastos(500000);
                if (jugador.getFortuna() < 0) {
                    jugador.fortunaPrevia = (500000 + jugador.getFortuna());
                    System.out.println("No tienes suficiente dinero. Quedas en deuda con el banco.");
                    jugador.enDeuda = banca;
                }
                break;

            case 8:
                System.out.println("Vas a la cárcel.");
                avatar.moverAvatar(tablero.getPosiciones(), Math.abs(10 - avatar.getLugar().getPosicion() + 1), false);
                jugador.setEnCarcel(true);
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
                    jugador.fortunaPrevia = (1000000 + jugador.getFortuna());
                    System.out.println("No tienes suficiente dinero. Quedas en deuda con el banco.");
                    jugador.enDeuda = banca;
                }
                break;

            case 12:

                for (int i = 0; i < jugadores.size(); i++) {
                    if (jugadores.get(i) != jugador) {
                        jugador.sumarGastos(200000);
                        jugadores.get(i).sumarFortuna(200000);

                        if (jugador.getFortuna() < 0) {
                            jugador.fortunaPrevia = (200000 + jugador.getFortuna());
                            System.out.println("No tienes suficiente dinero. Quedas en deuda con el banco.");
                            // do glosario de dubidas: Na carta de comunidade 6 (Alquilas a tus compañeros
                            // una villa en Solar7 durante una semana. Paga 200000€ a cada jugador),
                            // se o xogador non tén diñeiro para afrontar este pago e decide declararse en
                            // bancarrota, toda a súa fortuna e propiedades pasan á banca.
                            jugador.enDeuda = banca;
                            break;
                        }
                    }
                }

                break;
        }
        Casilla casillafinal = avatar.getLugar();

        if (!casillafinal.evaluarCasilla(jugador, banca, 0)) { // tirada non importa porque ningunha carta che manda a
                                                               // servicio
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
            if (jugador.movEspecial) {
                System.out.println("Cambio a modo estándar.");
                jugador.movEspecial = false;
            } else {
                System.out.println("Cambio a modo avanzado [" + jugador.getAvatar().getTipo() + "].");
                jugador.movEspecial = true;

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

        else if (comando.contains("lanzar dados") && jugador.getCocheCalado() > 0) {
            System.out.println("Tu coche está calado. No puedes lanzar los dados. (Turnos restantes: "
                    + (jugador.getCocheCalado() - 1) + ")");
        } else if (comando.equals("lanzar dados")
                && (lanzamientos == 0 || dado1.getValorPrevio() == dado2.getValorPrevio())) {

            if (!dobles_seguidos_check) {
                lanzarDados();
                lanzamientos++;
            }
            if (dado1.getValorPrevio() == dado2.getValorPrevio()) {
                dobles_seguidos++;
            }
            if (dobles_seguidos_check) {
                System.out.println("No puedes tirar los dados más veces. Estás en la carcel.");
            }
            if (dobles_seguidos == 3) {
                jugador.getAvatar().setLugar(tablero.getPosiciones(), 10);
                jugador.setEnCarcel(true);
                System.out.println("Has sacado dobles 3 veces te vas a la carcel.");
                lanzamientos = 1;
                dobles_seguidos = 0;
            }
        } else if (comando.contains("lanzar dados ")
                && (lanzamientos == 0 || tirada_anterior1 == tirada_anterior2)) {
            String numeros = comando.replace("lanzar dados ", "");
            String[] numero = numeros.split("\\+");
            tirada_anterior1 = Integer.parseInt(numero[0]);
            tirada_anterior2 = Integer.parseInt(numero[1]);
            if (tirada_anterior1 == tirada_anterior2) {
                dobles_seguidos++;
            }
            if (dobles_seguidos_check) {
                System.out.println("No puedes tirar los dados más veces. Estás en la carcel.");
            }
            if (dobles_seguidos == 3) {
                jugador.getAvatar().setLugar(tablero.getPosiciones(), 10);
                jugador.setEnCarcel(true);
                System.out.println("Has sacado dobles 3 veces te vas a la carcel.");
                dobles_seguidos_check = true;
                lanzamientos = 1;
                dobles_seguidos = 0;
                return;
            }
            if (!dobles_seguidos_check) {
                if (lanzamientos >= 4 && jugador.movEspecial && jugador.getAvatar().getTipo().equals("coche")) {
                    System.out.println("Has alcanzado el máximo de 4 tiradas con el coche.");
                } else if (lanzamientos >= 3) {
                    System.out.printf(
                            "Se ha alcanzado el máximo de 3 tiradas.");
                } else {
                    lanzarDados(tirada_anterior1, tirada_anterior2);
                    lanzamientos++;
                }
            }
        } else if (comando.contains("lanzar dados ")
                && (lanzamientos == 0 || dado1.getValorPrevio() == dado2.getValorPrevio())) {
            String numeros = comando.replace("lanzar dados ", "");
            String[] numero = numeros.split("\\+");
            lanzarDados(Integer.parseInt(numero[0]), Integer.parseInt(numero[1]));
            lanzamientos++;

        } else if (comando.equals("lanzar dados")) {
            if (jugador.movEspecial && jugador.getAvatar().getTipo().equals("coche")
                    && (dado2.getValorPrevio() + dado1.getValorPrevio()) > 4 && (lanzamientos < 4)) {
                lanzarDados();
                lanzamientos++;
            } else {
                if (lanzamientos >= 4 && jugador.movEspecial && jugador.getAvatar().getTipo().equals("coche")) {
                    System.out.println("Has alcanzado el máximo de 4 tiradas con el coche.");
                } else if (lanzamientos >= 3) {
                    System.out.printf(
                            "Se ha alcanzado el máximo de 3 tiradas.");
                } else {
                    System.out.printf(
                            "Sólo se pueden lanzar los dados una vez por turno, a no ser que saques dobles. (previas tiradas: %d %d)",
                            dado1.getValorPrevio(), dado2.getValorPrevio());
                }
            }
        } // comprar
        else if (comando.equals("comprar")) {
            if (!jugador.getPuedeComprar() && jugador.getAvatar().getTipo().equals("coche") && jugador.movEspecial) {
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
            if (jugador.enDeuda == null) {
                jugador.enDeuda = banca;
            }
            if (jugador.enDeuda == null)
                jugador.enDeuda = banca;
            System.out.println("Las propiedades y fortuna de " + jugador.getNombre() + " pasan a pertenecer a "
                    + jugador.enDeuda.getNombre() + ".");
            bancarrota(jugador.enDeuda);
        } /*
           * DEBUG
           */ else if (comando.contains("m ")) { // movimiento manual (debug)
            try {
                lanzarDados(Integer.parseInt(comando.replace("m ", "")));
                lanzamientos++;
            } catch (NumberFormatException ex) {
                System.out.println("Uso del comando: m [cantidad de casillas]");
            }
        } else if (comando.contains("f ")) { // fortuna manual (debug)

            try {
                jugador.sumarFortuna(Float.parseFloat(comando.replace("f ", "")));
                System.out.println("Nueva fortuna: " + jugador.getFortuna());
            } catch (NumberFormatException ex) {
                System.out.println("Uso del comando: f [fortuna]");
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

            if (jugador.getAvatar().puedeCogerCarta == 2) {

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

            } else if (jugador.getAvatar().puedeCogerCarta == 1) {
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

    // Método que ejecuta todas las acciones relacionadas con el comando 'lanzar
    // dados'.
    private void lanzarDados() {

        Jugador jugador = obtenerJugadorTurno();

        jugador.setTiradas(1);

        Avatar avatar = jugador.getAvatar();

        int tirada1 = dado1.hacerTirada();
        int tirada2 = dado2.hacerTirada();
        System.out.println("Has sacado: " + tirada1 + " y " + tirada2 + ".");

        int valor_tiradas = tirada1 + tirada2;
        if ((tirada1 == tirada2) && (dobles_seguidos != 3)) {
            System.out.println("Dobles!");
        } else if (jugador.getEnCarcel() && (dobles_seguidos != 3)) {

            System.out.println("Continúas en la carcel.");
            jugador.sumarTiradaCarcel();
            return;
        }

        if (jugador.getEnCarcel() && (tirada2 == tirada1) && (dobles_seguidos != 3)) {
            System.out.println("Sales de la carcel y vuelves a tirar.");
            jugador.salirCarcel();

        }

        Casilla casillainicio = avatar.getLugar();
        if (jugador.movEspecial) {
            if (avatar.getTipo().equals("pelota")) {
                avatar.moverPelota(tablero.getPosiciones(), valor_tiradas, banca);
            }
            if (avatar.getTipo().equals("coche")) {
                avatar.moverCoche(tablero.getPosiciones(), valor_tiradas);
                if (valor_tiradas > 4 && lanzamientos < 3) {
                    System.out.println("Tu tirada continúa! Puedes volver a lanzar los dados.");
                }
            }
        } else {
            avatar.moverAvatar(tablero.getPosiciones(), valor_tiradas, true);
        }

        Casilla casillafinal = avatar.getLugar();
        System.out.println("El avatar " + avatar.getID() + " avanza " + (valor_tiradas) + " posiciones, desde "
                + casillainicio.getNombre() + " hasta " + casillafinal.getNombre() + ".");

        if (!casillafinal.evaluarCasilla(jugador, banca, valor_tiradas)) {
            System.out.println("El jugador " + jugador.getNombre() + " no puede pagar sus deudas!");
        }

        if (casillafinal.getPosicion() == 31) {
            Casilla carcel = tablero.getCasilla("Carcel");
            jugador.getAvatar().moverAvatar(tablero.getPosiciones(), carcel, false);
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

    // Método que ejecuta todas las acciones relacionadas con el comando 'lanzar'
    // elejiendo los valores de los dados
    // Método que ejecuta todas las acciones relacionadas con el comando 'lanzar
    // dados' para valores de tirada concretos
    private void lanzarDados(int tirada1, int tirada2) {

        Jugador jugador = obtenerJugadorTurno();

        jugador.setTiradas(1);

        Avatar avatar = jugador.getAvatar();

        System.out.println("Has sacado: " + tirada1 + " y " + tirada2 + ".");

        int valor_tiradas = tirada1 + tirada2;
        if ((tirada1 == tirada2) && (dobles_seguidos != 3)) {
            System.out.println("Dobles!");
        } else if (jugador.getEnCarcel() && (dobles_seguidos != 3)) {

            System.out.println("Continúas en la carcel.");
            jugador.sumarTiradaCarcel();
            return;
        }

        if (jugador.getEnCarcel() && (tirada2 == tirada1) && (dobles_seguidos != 3)) {
            System.out.println("Sales de la carcel y vuelves a tirar.");
            jugador.salirCarcel();

        }

        Casilla casillainicio = avatar.getLugar();
        if (jugador.movEspecial) {
            if (avatar.getTipo().equals("pelota")) {
                avatar.moverPelota(tablero.getPosiciones(), valor_tiradas, banca);
            }
            if (avatar.getTipo().equals("coche")) {
                avatar.moverCoche(tablero.getPosiciones(), valor_tiradas);
                if (valor_tiradas > 4 && lanzamientos < 2) {
                    System.out.println("Tu tirada continúa! Puedes volver a lanzar los dados.");
                }
            }
        } else {
            avatar.moverAvatar(tablero.getPosiciones(), valor_tiradas, true);
        }

        Casilla casillafinal = avatar.getLugar();

        System.out.println("El avatar " + avatar.getID() + " avanza " + (valor_tiradas) + " posiciones, desde "
                + casillainicio.getNombre() + " hasta " + casillafinal.getNombre() + ".");

        if (!casillafinal.evaluarCasilla(jugador, banca, valor_tiradas)) {
            System.out.println("El jugador " + jugador.getNombre() + " no puede pagar sus deudas!");
        }

        if (casillafinal.getPosicion() == 31) {
            Casilla carcel = tablero.getCasilla("Carcel");
            jugador.getAvatar().moverAvatar(tablero.getPosiciones(), carcel, false);
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

        jugador.setTiradas(1);

        Avatar avatar = jugador.getAvatar();
        Casilla casillainicio = avatar.getLugar();

        avatar.moverAvatar(tablero.getPosiciones(), valor_tiradas, true);

        Casilla casillafinal = avatar.getLugar();
        System.out.println("El avatar " + avatar.getID() + " avanza " + (valor_tiradas) + " posiciones, desde "
                + casillainicio.getNombre() + " hasta " + casillafinal.getNombre() + ".");

        if (!casillafinal.evaluarCasilla(jugador, banca, valor_tiradas)) {
            System.out.println("El jugador " + jugador.getNombre() + " no puede pagar sus deudas!");
        }

        if (casillafinal.getPosicion() == 31) {
            Casilla carcel = tablero.getCasilla("Carcel");
            jugador.getAvatar().moverAvatar(tablero.getPosiciones(), carcel, false);
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
        dobles_seguidos_check = false;
    }

    private void bancarrota(Jugador jugador) {
        Jugador jugadorTurno = obtenerJugadorTurno();

        if (jugadorTurno.enDeuda == null) {
            jugadorTurno.enDeuda = banca;
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
            jugador.sumarFortuna(jugadorTurno.fortunaPrevia);
            System.out.println("El jugador " + jugador.getNombre() + " recibe los " + jugadorTurno.fortunaPrevia
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

    private String jugadorMasVueltas() {

        int max = jugadores.get(1).getVueltas();
        int n = 1;

        for (int i = 0; i < jugadores.size(); i++) {
            if (max < jugadores.get(i).getVueltas()) {
                max = jugadores.get(i).getVueltas();
                n = i;
            }
        }

        return (jugadores.get(n).getNombre());
    }

    private String casillaMasRentable() {

        float max = tablero.getCasilla(1).GetRentabilidad();
        int n = 1;

        for (int i = 0; i < 40; i++) {
            if (max < tablero.getCasilla(i).GetRentabilidad()) {
                max = tablero.getCasilla(i).GetRentabilidad();
                n = i;
            }
        }
        return tablero.getCasilla(n).getNombre();
    }

    private String grupoMasRentable() {

        float max = tablero.getCasilla(1).GetRentabilidad();
        int n = 1;

        for (int i = 0; i < 40; i++) {
            if (max < tablero.getCasilla(i).GetRentabilidad()) {
                max = tablero.getCasilla(i).GetRentabilidad();
                n = i;
            }
        }
        return tablero.getCasilla(n).getNombre();
    }

    private void estadisticas() {
        System.out.println("casillaMasRentable: " + casillaMasRentable());
        System.out.println("grupoMasRentable: " + grupoMasRentable());
        System.out.println("casillaMasFrecuentada: ");
        System.out.println("jugadorMasVueltas: " + jugadorMasVueltas());
        System.out.println("jugadorMasVecesDados: ");
        System.out.println("jugadorEnCabeza: ");
    }
}
