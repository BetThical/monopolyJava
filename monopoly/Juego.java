package monopoly;

import java.util.*;
import partida.*;

public final class Juego {

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
    private Edificio e; // Edificio para construir en una casilla.
    private static final List<String> EDIFICIOS_VALIDOS = Arrays.asList("casa", "hotel", "piscina", "pista", "4casas");
    // Constantes
    private static final int MAX_JUGADORES = 6; // Número máximo de jugadores en una partida.
    private boolean partidaAcabada = false; // Indica si la partida ha terminado.
    // objetos
    public final static Consola consola = new ConsolaNormal(); // Consola para imprimir y leer mensajes.

    public void listar(String args) {
        switch (args) {
            case "jugadores":
                listarJugadores();
                break;
            case "avatares":
                listarAvatares();
                break;
            case "venta":
                listarVenta();
                break;
            case "edificios":
                listarEdificios();
                break;
            default:
                consola.imprimir("Comando inválido.");
                break;
        }
    }

    public void listarEdificios() {
        for (int i = 0; i < tablero.getNumCasillas(); i++) {
            Casilla casilla = tablero.getCasilla(i);
            if (casilla.getEdificios().size() > 0) {
                for (Edificio edificio : casilla.getEdificios()) {
                    consola.imprimir("\nID:" + edificio.getID());
                    consola.imprimir("Propietario: " + casilla.getduenhoJugador().getNombre());
                    consola.imprimir("Grupo: " + casilla.getGrupo().getNombre());
                    consola.imprimir("Coste: " + casilla.valorEdificio(edificio.getTipo()));
                }
            }
        }

    }

    private int numjugadores() {
        return jugadores.size();
    }

    public void describir(String args) {
        String[] argsArray = args.split(" ");
        switch (argsArray[0]) {
            case "jugador":
                descJugador(argsArray[1]);
                break;
            case "avatar":
                descAvatar(argsArray[1]);
                break;
            default:
                if (args.equals("describir")) { // si el comando introducido es solo 'describir'
                    consola.imprimir(
                            "Uso: describir [casilla], describir jugador [jugador], describir avatar [avatar]");
                } else {
                    descCasilla(args);
                }
                break;
        }
    }

    public Juego() {
        this.banca = new Jugador();
        this.tablero = new Tablero(banca);
        dado1 = new Dado();
        dado2 = new Dado();
        jugadores = new ArrayList<>();
        avatares = new ArrayList<>();
    }

    // Método para inciar una partida: crea los jugadores y avatares.
    public void iniciarPartida(Tablero t) {

        int jugadores = 0;

        while (jugadores < 2 || jugadores > 6) {
            try {
                jugadores = Integer.parseInt(consola.leer("Introduce el número de jugadores (2-6): "));
            } catch (Exception ex) {
                jugadores = 0;

            }
        }
        for (int i = 1; (i <= jugadores); i++) {
            anhadirJugador();
        }

        consola.imprimir("Partida iniciada con " + numjugadores() + " jugadores.");
        consola.imprimir("Empieza la partida: " + getJugadorTurno().getNombre() + ".");

    }

    public void anhadirJugador() {
        if (jugadores.size() >= MAX_JUGADORES) {
            return;
        }
        Casilla casillaInicio = tablero.getCasilla(0);
        String nombre = consola.leer("\nIntroduce el nombre del jugador " + (getNumeroDeJugadores() + 1) + ": ");
        String tipoAvatar = consola.leer("Elige el tipo de avatar para " + nombre + " (por ejemplo: coche, pelota):");
        /// DEBUG (para ir mas rapido en test) ///
        if (tipoAvatar.equals("a")) {
            consola.imprimir(Valor.YELLOW + "DEBUG: Se ha creado un avatar coche." + Valor.RESET);
            tipoAvatar = "coche";
        }
        if (tipoAvatar.equals("b")) {
            consola.imprimir(Valor.YELLOW + "DEBUG: Se ha creado un avatar pelota." + Valor.RESET);
            tipoAvatar = "pelota";
        }
        while (!tipoAvatar.equals("coche") && !tipoAvatar.equals("pelota")) {
            tipoAvatar = consola.leer("Elige un tipo de avatar válido para " + nombre + " (coche o pelota):");
        }
        Jugador jugador = new Jugador(nombre, tipoAvatar, casillaInicio, avatares);

        jugador.sumarFortuna((float) Valor.FORTUNA_INICIAL);
        jugadores.add(jugador);
        avatares.add(jugador.getAvatar());
        casillaInicio.anhadirAvatar(jugador.getAvatar());
        consola.imprimir("Jugador " + nombre + " con avatar " + tipoAvatar + " creado.");
    }

    public void funcionesCartas(Avatar avatar, Tablero tablero, int id) {
        Jugador jugador = avatar.getJugador();
        Casilla casillaInicial = avatar.getLugar();
        switch (id) {
            case 1:

                if (avatar.getLugar().getPosicion() <= 6) // trans1 es la casilla 6
                {
                    avatar.moverAvatar(tablero.getPosiciones(), 6 - avatar.getLugar().getPosicion(), true);
                } else {
                    avatar.moverAvatar(tablero.getPosiciones(),
                            tablero.getNumCasillas() - avatar.getLugar().getPosicion() + 6, true);
                }
                break;

            case 2:

                if (avatar.getLugar().getPosicion() <= 27) // Solar15 es la casilla 27
                {
                    avatar.moverAvatar(tablero.getPosiciones(), 27 - avatar.getLugar().getPosicion(), false);
                } else {
                    avatar.moverAvatar(tablero.getPosiciones(),
                            tablero.getNumCasillas() - avatar.getLugar().getPosicion() + 27, false);
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
                    avatar.moverAvatar(tablero.getPosiciones(),
                            tablero.getNumCasillas() - avatar.getLugar().getPosicion() + 7, true);
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
                    consola.imprimir("No tienes suficiente dinero. Quedas en deuda con el banco.");
                    jugador.setEnDeuda(banca);
                }
                break;

            case 8:
                consola.imprimir("Vas a la cárcel.");
                encarcelar(jugador);

                break;

            case 9:

                avatar.moverAvatar(tablero.getPosiciones(),
                        tablero.getNumCasillas() - avatar.getLugar().getPosicion() + 1, true);
                break;

            case 10:

                jugador.sumarFortuna(2000000);
                break;

            case 11:

                jugador.sumarGastos(1000000);
                if (jugador.getFortuna() < 0) {
                    jugador.setFortunaPrevia((1000000 + jugador.getFortuna()));
                    consola.imprimir("No tienes suficiente dinero. Quedas en deuda con el banco.");
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
                            consola.imprimir("No tienes suficiente dinero. Quedas en deuda con el banco.");
                            // do glosario de dubidas: Na carta de comunidade 6 (Alquilas a tus compañeros
                            // una villa en Solar7 durante una semana. Paga 200000€ a cada jugador),
                            // se o xogador non tén diñeiro para afrontar este pago e decide declararse en
                            // bancarrota, toda a súa fortuna e propiedades pasan á banca.
                            jugador.setEnDeuda(banca);
                            break;
                        }
                    }
                }

                break;
        }

        if (avatar.getLugar() != casillaInicial) { // si se ha movido
            avatar.getLugar().evaluarCasilla(jugador, banca, 0);
        }
        if (avatar.get4Voltas() == true) {
            boolean condicion = true;
            for (int i = 0; i < jugadores.size(); i++) {
                if (jugadores.get(i).getVueltas() < jugador.getVueltas()) {
                    condicion = false;
                }
            }
            if (condicion == true) {
                consola.imprimir(("Todos los jugadores han dado 4 vueltas! El precio de las propiedades aumenta."));
                tablero.aumentarCoste(banca);
            }
        }

    }

    public void cambiarModo(Jugador jugador) {
        if (lanzamientos > 0) {
            consola.imprimir("No puedes cambiar de modo después de lanzar los dados.");
        } else {
            if (jugador.getMovEspecial()) {
                consola.imprimir("Cambio a modo estándar.");
                jugador.setMovEspecial(false);
            } else {
                consola.imprimir("Cambio a modo avanzado [" + jugador.getAvatar().getTipo() + "].");
                jugador.setMovEspecial(true);

            }
        }
    }

    public void hipotecar(String args, Jugador jugador) {
        Casilla aHipotecar;
        aHipotecar = tablero.getCasilla(args);
        if (aHipotecar == null) {
            consola.imprimir("Casilla inválida.");
        } else {
            if (aHipotecar.puedeHipotecar(jugador)) {
                aHipotecar.hipotecar();
            }
        }
    }

    public void deshipotecar(String args, Jugador jugador) {
        Casilla aDeshipotecar;
        aDeshipotecar = tablero.getCasilla(args);
        if (aDeshipotecar == null) {
            consola.imprimir("Casilla inválida.");
        } else {
            if (aDeshipotecar.puedeDeshipotecar(jugador)) {
                aDeshipotecar.deshipotecar();
            }
        }
    }

    public void listarEdificiosGrupo(String nombreGrupo) {
        try {
            Grupo grupo = tablero.getGrupoNombre(nombreGrupo);
            grupo.descEdificios();
        } catch (Exception ex) {
            consola.imprimir("grupo invalido (" + nombreGrupo + ")");
        }
    }

    public void jugadorActual(Jugador jugador) {
        consola.imprimir(
                "Jugador actual: " + jugador.getNombre() + ", con avatar &" + jugador.getAvatar().getID() + ".");
    }

    public void avanzar(Jugador jugador) {
        if (!(jugador.getAvatar() instanceof Pelota) || !jugador.getMovEspecial()) {
            consola.imprimir("El comando avanzar solo está disponible para el avatar pelota en modo avanzado.");
        } else {
            Pelota pelota = (Pelota) jugador.getAvatar();
            if (puedeAvanzar()) {
                pelota.avanzar(tablero.getPosiciones(), banca);
            } else {
                consola.imprimir("No puedes avanzar.");
            }
        }
    }

    public void edificar(String args, Jugador jugador, Casilla casilla) {
        if (!EDIFICIOS_VALIDOS.contains(args)) {
            consola.imprimir("Edificios válidos: casa, hotel, piscina, pista.");
        } else {
            if (args.equals("4casas")) {
                args = "casa";
                for (int i = 0; i < 4; i++) {
                    e = new Edificio(args, casilla);
                    if (casilla.puedeConstruir(e, jugador)) {
                        consola.imprimir("Has comprado un(a) " + args + " en " + casilla.getNombre() + ", por "
                                + casilla.valorEdificio(e.getTipo()) + ".");
                        casilla.anhadirEdificio(e);
                    }
                }
            } else {
                e = new Edificio(args, casilla);
                if (casilla.puedeConstruir(e, jugador)) {
                    consola.imprimir("Has comprado un(a) " + args + " en " + casilla.getNombre() + ", por "
                            + casilla.valorEdificio(e.getTipo()) + ".");
                    casilla.anhadirEdificio(e);
                }
            }
        }
    }

    public void destruir(String args, Jugador jugador, Casilla casilla) {

        if (!EDIFICIOS_VALIDOS.contains(args)) {
            consola.imprimir("Edificios válidos: casa, hotel, piscina, pista.");
        } else {
            consola.imprimir("Has vendido un(a) " + args + " en " + casilla.getNombre() + ", por "
                    + casilla.valorEdificio(args) / 2f + ".");
            casilla.destruirEdificio(args);
            jugador.sumarFortuna(casilla.valorEdificio(args) / 2f);
        }
    }

    public void fortunaManual(String args, Jugador jugador) {
        try {
            float amount = Float.parseFloat(args);
            if (args.startsWith("+")) {
                jugador.sumarFortuna(amount);
            } else if (args.startsWith("-")) {
                jugador.sumarFortuna(amount);
            } else {
                jugador.setFortuna(amount);
            }
            consola.imprimir("Nueva fortuna: " + jugador.getFortuna());
        } catch (NumberFormatException ex) {
            consola.imprimir("Uso del comando: f [+/-][fortuna]");
        }
    }

    public void cogerCarta(Jugador jugador) {

        if (jugador.puedeCogerCarta() == 0) {
            consola.imprimir("No puedes coger cartas.");
            return;
        }
        int cartaDisponible = jugador.puedeCogerCarta();
        HashMap<Integer, Carta> cartas; // 1: comunidad, 2: suerte
        if (cartaDisponible == 1) {
            cartas = tablero.getComunidad();
            consola.imprimir("Has cogido una carta de la comunidad.");
        } else {
            consola.imprimir("Has cogido una carta de la suerte.");
            cartas = tablero.getSuerte();

        }
        consola.imprimir("Cartas disponibles:");
        for (Map.Entry<Integer, Carta> entry : cartas.entrySet()) {
            if (cartaDisponible == 1) { // suerte en amarillo, comunidad en azul
                consola.imprimir(Valor.BLUE + entry.getKey() + "." + Valor.RESET + " " + entry.getValue().getCarta());
            } else {
                consola.imprimir(Valor.YELLOW + entry.getKey() + "." + Valor.RESET + " " + entry.getValue().getCarta());
            }
        }
        int numero = Integer.parseInt(consola.leer("Introduce un número entre 1 y 6:"));
        if (numero < 1 || numero > 6) {
            consola.imprimir("Número inválido.");
        } else {
            Carta carta = cartas.get(numero);
            consola.imprimir("Has seleccionado: " + carta.getCarta());
            if (cartaDisponible == 1) {
                numero += 6; // eliminar esto cando se separen as cartas

            }
            funcionesCartas(jugador.getAvatar(), tablero, numero);
            jugador.setCartaDisponible(0);
        }
    }

    /*
     * Método que realiza las acciones asociadas al comando /describir jugador'.
     * Parámetro: nombre del jugador
     */
    public void descJugador(String nombre) {
        Jugador jugador = getJugador(nombre);
        if (!(jugador == null)) {
            consola.imprimir("Nombre: " + jugador.getNombre());
            consola.imprimir("Avatar: " + jugador.getAvatar().getID());
            consola.imprimir("Fortuna: " + jugador.getFortuna());
            consola.imprimir("Propiedades: ");
            for (int j = 0; j < jugador.getPropiedades().size(); j++) {
                System.out.print("  ||" + jugador.getPropiedades().get(j).getNombre());
                if (jugador.getPropiedades().get(j).getHipotecada()) {
                    System.out.print("[H]");
                }
                System.out.print("||");
            }
            consola.imprimir("");
            /*
             * ArrayList<Edificio> edificios = jugador.getEdificios();
             * if (!edificios.isEmpty()) {
             * consola.imprimir("- Edificios:\n");
             * for (int i = 0; i < edificios.size(); i++) {
             * consola.imprimir("   · " + edificios.get(i).getTipo());
             * }
             * }
             */
        } else {
            consola.imprimir("No existe un jugador con ese nombre.");
        }
    }

    public void descJugador(Jugador jugador) {
        if (!(jugador == null)) {
            consola.imprimir("Nombre: " + jugador.getNombre());
            consola.imprimir("Avatar: " + jugador.getAvatar().getID());
            consola.imprimir("Fortuna: " + jugador.getFortuna());
            consola.imprimir("Propiedades:");
            for (int j = 0; j < jugador.getPropiedades().size(); j++) {
                System.out.print("  ||" + jugador.getPropiedades().get(j).getNombre());
                if (jugador.getPropiedades().get(j).getHipotecada()) {
                    System.out.print("[H]");
                }
                System.out.print("||");
            }
            consola.imprimir("");
            consola.imprimir("Hipotecas: ");
            consola.imprimir("Edificios: ");
            consola.imprimir("");
        } else {
            consola.imprimir("No existe el jugador.");
        }
    }

    /*
     * Método que realiza las acciones asociadas al comando 'describir avatar'.
     * Parámetro: id del avatar a describir.
     */
    public void descAvatar(String ID) {
        Avatar avatar = getAvatar(ID);
        if (!(avatar == null)) {
            consola.imprimir("- ID: " + avatar.getID());
            consola.imprimir("- Tipo: " + avatar.getTipo());
            consola.imprimir("- Casilla: " + avatar.getLugar().getNombre());
            consola.imprimir("- Jugador: " + avatar.getJugador().getNombre());
            consola.imprimir("");

        } else {
            consola.imprimir("No existe un avatar con ese ID.");
        }
    }

    /*
     * Método que realiza las acciones asociadas al comando 'describir
     * nombre_casilla'.
     * Parámetros: nombre de la casilla a describir.
     */
    public void descCasilla(String nombre) {
        Casilla casilla = tablero.getCasilla(nombre);
        if (!(casilla == (null))) {
            consola.imprimir(casilla.infoCasilla(banca));
        } else {
            consola.imprimir("No existe la casilla \'" + nombre + "\'.");
        }
    }

    public boolean puedeLanzarDados() {
        Jugador jugador = getJugadorTurno();
        Avatar avatar = jugador.getAvatar();

        if (jugador.getEnCarcel()) {
            if (jugador.getTiradasCarcel() > 2) {
                consola.imprimir("Has pasado 3 turnos en la cárcel. Debes pagar la multa.");
                return false;
            } else if (lanzamientos > 0) {
                consola.imprimir("Sólo puedes intentar salir de la cárcel al inicio de tu turno, una vez por turno");
                return false;
            }
        }
        if (lanzamientos > 0 && dobles_seguidos == 0 && (!(avatar instanceof Coche) || !jugador.getMovEspecial())) {
            consola.imprimir("Ya has lanzado los dados en este turno.");
            return false;
        }
        if (jugador.getCocheCalado() > 0) {
            consola.imprimir("Por una previa tirada con el coche, no puedes tirar durante " + jugador.getCocheCalado()
                    + " turnos.");
            return false;
        }
        if ((avatar instanceof Coche) && jugador.getMovEspecial() && lanzamientos > 3) {
            if (dobles_seguidos == 0) {
                consola.imprimir("Ya has lanzado los dados 4 veces en este turno.");
                return false;
            }
            if (lanzamientos > 4) {
                consola.imprimir("Sacar dobles en la última tirada del coche te permite sólo una tirada adicional.");
                return false;
            }
        }

        if (jugador.getMovEspecial() && (avatar instanceof Pelota)) {
            if (((Pelota) avatar).siguienteMovPelota(false) != 0) {
                consola.imprimir("Utiliza el comando 'avanzar' para moverte.");
                return false;
            }
            return true; // la primera tirada de la pelota no implica moverse, no es necesario comprobar
                         // si se puede avanzar
        }
        return puedeAvanzar();
    }

    // similar a puedeLanzarDados pero comprueba si el avatar puede moverse
    // actualmente, sea con una tirada normal de dados o con avanzar
    private boolean puedeAvanzar() {
        Jugador jugador = getJugadorTurno();
        Avatar avatar = jugador.getAvatar();

        if (jugador.getFortuna() < 0) {
            consola.imprimir(
                    "Actualmente estás en deuda. Debes destruir edificios, hipotecar propiedades o declarar la bancarrota.");
            return false;
        }
        if (jugador.puedeCogerCarta() != 0) {
            consola.imprimir("Debes coger la carta primero.");
            return false;
        }

        if (jugador.getMovEspecial() && (avatar instanceof Pelota)
                && ((Pelota) avatar).siguienteMovPelota(false) == 0) {
            return false; // no puede avanzar más
        }
        return true;
    }

    // Método que ejecuta todas las acciones relacionadas con el comando 'lanzar'
    // elejiendo los valores de los dados
    public void lanzarDados() {
        int tirada1 = dado1.hacerTirada();
        int tirada2 = dado2.hacerTirada();
        lanzarDados(tirada1, tirada2);
    }

    // Método que ejecuta todas las acciones relacionadas con el comando 'lanzar
    // dados' para valores de tirada concretos
    public void lanzarDados(int tirada1, int tirada2) {

        Jugador jugador = getJugadorTurno();

        Avatar avatar = jugador.getAvatar();

        consola.imprimir("Has sacado: " + tirada1 + " y " + tirada2 + ".");

        int valor_tiradas = tirada1 + tirada2;
        lanzamientos++;
        jugador.addTiradas(1);
        // manejo de dobles
        if (tirada1 == tirada2) {
            consola.imprimir("Dobles!");
            dobles_seguidos++;
            if (jugador.getEnCarcel()) {
                consola.imprimir("Sales de la carcel y vuelves a tirar.");
                jugador.salirCarcel();
            }
            if (dobles_seguidos == 3) {
                consola.imprimir("Has sacado dobles 3 veces seguidas! Vas a la carcel.");
                encarcelar(jugador);
                return;
            }
        } else {
            dobles_seguidos = 0;
            if (jugador.getEnCarcel()) {
                jugador.sumarTiradaCarcel();
                consola.imprimir("Continúas en la carcel. (Tiradas en cárcel: " + jugador.getTiradasCarcel() + ")");
                return;
            }
        }

        // manejo de movimientos especiales
        if (jugador.getMovEspecial()) {
            if ((avatar instanceof Pelota)) {
                ((Pelota) avatar).moverEnAvanzado(tablero.getPosiciones(), valor_tiradas);
            }
            if ((avatar instanceof Coche)) {
                ((Coche) avatar).moverEnAvanzado(tablero.getPosiciones(), valor_tiradas);
                if (valor_tiradas > 4 && lanzamientos < 4) {
                    consola.imprimir("Tu tirada continúa! Puedes volver a lanzar los dados.");
                }
                if (dobles_seguidos != 0 && lanzamientos == 4) {
                    consola.imprimir("Has sacado dobles en la última tirada! Puedes volver a lanzar los dados.");
                }
            }
        } else {
            avatar.moverAvatar(tablero.getPosiciones(), valor_tiradas, true);
        }

        // llamada a evaluar casilla
        Casilla casillaFinal = avatar.getLugar();
        if (!(jugador.getMovEspecial() && (avatar instanceof Pelota))) // porque el (primer) movimiento especial de la
                                                                       // pelota no implica moverse, no se evalua la
                                                                       // casilla
        {
            casillaFinal.evaluarCasilla(jugador, banca, valor_tiradas);
        } else {
            consola.imprimir("Utiliza el comando 'avanzar' para moverte.");
        }
        if (jugador.getEnCarcel()) {
            encarcelar(jugador); // comprobación de si cae en 'ir a cárcel'

        }
        comprobacion4Vueltas();

    }

    private void comprobacion4Vueltas() {

        Jugador jugador = getJugadorTurno();
        Avatar avatar = jugador.getAvatar();
        if (avatar.get4Voltas() == true) {
            boolean condicion = true;
            for (int i = 0; i < jugadores.size(); i++) {
                if (jugadores.get(i).getVueltas() < jugador.getVueltas()) {
                    condicion = false;
                }
            }
            if (condicion == true) {
                consola.imprimir(("Todos los jugadores han dado 4 vueltas! El precio de las propiedades aumenta."));
                tablero.aumentarCoste(banca);
            }
        }
    }

    /*
     * Método que ejecuta todas las acciones realizadas con el comando 'comprar
     * nombre_casilla'.
     * Parámetro: cadena de caracteres con el nombre de la casilla.
     */
    public void comprar(Casilla casilla) {
        Jugador jugador = getJugadorTurno();
        if (!jugador.getPuedeComprar() && jugador.getAvatar().getTipo().equals("coche") && jugador.getMovEspecial()) {
            consola.imprimir(
                    "Al realizar el movimiento especial del coche, sólo puedes comprar una vez por turno.");
            return;
        }
        if (casilla.esComprable(jugador, banca)) {
            consola.imprimir(
                    jugador.getNombre() + " compra la propiedad " + casilla.getNombre() + " por " + casilla.getValor()
                            + ".");
            casilla.comprarCasilla(jugador, banca);
        } else {
            consola.imprimir("No puedes comprar esta casilla.");
        }
    }

    // Método que ejecuta todas las acciones relacionadas con el comando 'salir
    // carcel'.
    public boolean salirCarcel(Jugador jugador) {
        if (!jugador.getEnCarcel()) {
            consola.imprimir("No estás en la cárcel.");
            return false;
        }
        if (lanzamientos != 0) {
            consola.imprimir("Sólo puedes pagar la multa al inicio del turno.");
            return false;
        }

        return getJugadorTurno().pagarMulta();

    }

    // Método que realiza las acciones asociadas al comando 'listar enventa'.
    public void listarVenta() {
        banca.getPropiedades();
        Casilla casilla_aux;
        for (int i = 0; i < tablero.getNumCasillas(); i++) {
            casilla_aux = tablero.getCasilla(i);
            if ((casilla_aux.getTipo().equals("solar") || casilla_aux.getTipo().equals("transporte")
                    || casilla_aux.getTipo().equals("servicio")) && casilla_aux.getduenhoJugador() == banca) {
                consola.imprimir(casilla_aux.casEnVenta());
            }
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar jugadores'.
    public void listarJugadores() {
        for (int i = 0; i < getNumeroDeJugadores(); i++) {
            descJugador(jugadores.get(i));
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar avatares'.
    public void listarAvatares() {
        for (int i = 0; i < getNumeroDeAvatares(); i++) {
            descAvatar(avatares.get(i).getID());

        }
    }

    // Método que realiza las acciones asociadas al comando 'acabar turno'.
    public void acabarTurno(Jugador jugador) {

        if (jugador.getFortuna() < 0) {
            consola.imprimir(
                    "Actualmente estás en deuda. Debes destruir edificios, hipotecar propiedades o declarar la bancarrota.");
            return;
        }
        turno = (turno + 1) % getNumeroDeJugadores();
        nuevoTurno();

    }

    // esta función se separa del comando 'acabar turno' porque la bancarrota
    // inicia un nuevo turno sin sumarle 1 a turno, y sin la comprobación de deuda
    private void nuevoTurno() {

        getJugadorTurno().setPuedeComprar(true);
        lanzamientos = 0;
        dobles_seguidos = 0;
        consola.imprimir("Turno de " + getJugadorTurno().getNombre() + ".");

    }

    public void bancarrota(Jugador jugadorBancarrota) {
        Jugador jugadorRecibe;
        if (jugadorBancarrota.getEnDeuda() == null) {
            jugadorRecibe = banca;
        } else {
            jugadorRecibe = jugadorBancarrota.getEnDeuda();
        }
        ArrayList<Casilla> array_propiedades;
        Casilla casilla = getJugadorTurno().getAvatar().getLugar();
        consola.imprimir(
                "El jugador " + jugadorBancarrota.getNombre() + " ha declarado la bancarrota y abandona la partida!");
        consola.imprimir("Las propiedades y fortuna de " + jugadorBancarrota.getNombre() + " pasan a pertenecer a "
                + jugadorRecibe.getNombre() + ".");
        array_propiedades = jugadorBancarrota.getPropiedades();

        for (int i = 0; i < array_propiedades.size(); i++) {
            array_propiedades.get(i).getEdificios().clear();
            if (jugadorRecibe != banca) {
                jugadorRecibe.anhadirPropiedad(array_propiedades.get(i));
            }
        }
        if (!jugadorRecibe.equals(banca)) {
            jugadorRecibe.sumarFortuna(jugadorRecibe.getFortunaPrevia());
            consola.imprimir(
                    "El jugador " + jugadorRecibe.getNombre() + " recibe los " + jugadorBancarrota.getFortunaPrevia()
                            + " que tenía " + jugadorBancarrota.getNombre() + ".");
        }

        jugadores.remove(jugadorBancarrota);
        avatares.remove(jugadorBancarrota.getAvatar());
        casilla.getAvatares().remove(jugadorBancarrota.getAvatar());
        if (jugadores.size() < 2) {
            Juego.consola.imprimir("El único jugador que queda es " + jugadores.get(0).getNombre() + "!");
            setPartidaAcabada(true);
        } else
            nuevoTurno();
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

        for (int i = 0; i < tablero.getNumCasillas(); i++) {
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

    public String gruposMasRentables() {
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

        for (int i = 0; i < tablero.getNumCasillas(); i++) {
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

    public void estadisticas() {
        consola.imprimir("casillaMasRentable: " + casillasMasRentables());
        consola.imprimir("grupoMasRentable: " + gruposMasRentables());
        consola.imprimir("casillaMasFrecuentada: " + casillasMasFrecuentadas());
        consola.imprimir("jugadorMasVueltas: " + jugadoresMasVueltas());
        consola.imprimir("jugadorMasVecesDados: " + jugadoresMasVecesDados());
        consola.imprimir("jugadorEnCabeza: " + jugadoresEnCabeza());
    }

    public void estadisticasJugador(String args) {
        Jugador jugador = getJugador(args);
        if (jugador == null) {
            consola.imprimir("Jugador no encontrado.");
            return;
        }
        jugador.estadisticas();

    }

    private void encarcelar(Jugador jugador) {
        jugador.setEnCarcel(true);
        jugador.setTiradasCarcel(0);
        jugador.getAvatar().setLugar(tablero.getPosiciones(), 10);
        if ((jugador.getAvatar() instanceof Pelota) && ((Pelota) jugador.getAvatar()).siguienteMovPelota(false) != 0) {
            ((Pelota) jugador.getAvatar()).resetMovPelota();
            consola.imprimir("Tu tirada de la pelota ha sido interrumpida por haber caído en la cárcel.");
        }
    }
    // SETTERS

    public void setLanzamientos(Integer newLanzamientos) {
        lanzamientos = newLanzamientos;
    }

    public void setPartidaAcabada(boolean p) {
        partidaAcabada = p;
    }

    // GETTERS
    public Tablero getTablero() {
        return tablero;
    }

    public Jugador getBanca() {
        return banca;
    }

    public Integer getLanzamientos() {
        return lanzamientos;
    }

    public Jugador getJugadorTurno() {
        return jugadores.get(turno);
    }

    public int getNumeroDeJugadores() {
        return jugadores.size();
    }

    public int getNumeroDeAvatares() {
        return avatares.size();
    }

    public boolean getPartidaAcabada() {
        return partidaAcabada;
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

}
