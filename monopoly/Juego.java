package monopoly;

import exception.comandoIncorrectoException.*;
import exception.comandoInvalidoException.*;
import exception.noEncontradoException.*;
import java.util.*;
import partida.*;

public final class Juego implements Comando {

    // Atributos
    private final ArrayList<Jugador> jugadores; // Jugadores de la partida.
    private final ArrayList<Avatar> avatares; // Avatares en la partida.
    private int turno = 0; // Índice correspondiente a la posición en el arrayList del jugador (y el
    // avatar) que tienen el turno
    private int lanzamientos = 0; // Variable para contar el número de lanzamientos de un jugador en un turno.
    private int dobles_seguidos = 0; // Variable para contar el número de dobles seguidos de un jugador en un turno.
    private final Tablero tablero; // Tablero en el que se juega.
    private final Dado dado1; // Dos dados para lanzar y avanzar casillas.
    private final Dado dado2;
    private final Jugador banca; // El jugador banca.
    private Edificio e; // Edificio para construir en una casilla.
    private boolean partidaAcabada = false; // Indica si la partida ha terminado.
    // Constantes
    private static final List<String> EDIFICIOS_VALIDOS = Arrays.asList("casa", "hotel", "piscina", "pista", "4casas");
    private static final int MAX_JUGADORES = 6; // Número máximo de jugadores en una partida.

    // Objetos
    public final static Consola consola = new ConsolaNormal(); // Consola para imprimir y leer mensajes.

    // Constructor
    public Juego() {
        this.banca = new Jugador();
        this.tablero = new Tablero(banca);
        dado1 = new Dado();
        dado2 = new Dado();
        jugadores = new ArrayList<>();
        avatares = new ArrayList<>();
        iniciarPartida(tablero);
    }

    // Comandos (en el orden que aparecen en menú)
    // * - - - Jugadores - - - * //
    // Método que añade un jugador a la partida.
    // Se invoca al inicializar la partida, y cada vez que se usa el comando 'añadir
    // jugador'.
    // Permite hasta MAX_JUGADORES (6) jugadores.
    @Override
    public void anhadirJugador() {
        if (jugadores.size() >= MAX_JUGADORES) {
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

    // Método que imprime, mediante la consola, el nombre y avatar de un jugador.
    // Corresponde al comando 'jugador'.
    @Override
    public void jugadorActual(Jugador jugador) {
        consola.imprimir(
                "Jugador actual: " + jugador.getNombre() + ", con avatar &" + jugador.getAvatar().getID() + ".");
    }

    // * - - - Movimientos - - - * //
    // Método que ejecuta todas las acciones relacionadas con el comando 'lanzar
    // dados',
    // con valores de tirada aleatorios.
    @Override
    public void lanzarDados() throws DadosException, AvanzarException {
        int tirada1 = dado1.hacerTirada();
        int tirada2 = dado2.hacerTirada();
        // Genera dos valores de tiradas aleatorios y luego se los pasa al método
        // lanzarDados con tirada manual.
        lanzarDados(tirada1, tirada2);
    }

    // Método que ejecuta todas las acciones relacionadas con el comando 'lanzar
    // dados' para valores de tirada concretos. Se usa desde lanzarDados() y también
    // con los comandos de prueba lanzar dados [x+y] y m[x].
    @Override
    public void lanzarDados(int tirada1, int tirada2) throws DadosException, AvanzarException {
        puedeLanzarDados(); // si no puede lanzar, devolverá una excepción, y no se lanzarán los dados

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
                jugador.encarcelar(tablero.getPosiciones());
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

        Casilla casillaFinal = jugador.getAvatar().getLugar();
        if (!(jugador.getMovEspecial() && (avatar instanceof Pelota))) // porque el (primer) movimiento especial de la
        { // pelota no implica moverse, no se evalua la casilla
            casillaFinal.evaluarCasilla(jugador, banca, valor_tiradas);
        } else {
            consola.imprimir("Utiliza el comando 'avanzar' para moverte.");
        }
        comprobacion4Vueltas();

    }

    // Método que ejecuta todas las acciones relacionadas con el comando 'avanzar',
    // que permite a un jugador avanzar con el avatar pelota en modo avanzado.
    @Override
    public void avanzar(Jugador jugador) throws AvanzarException {
        if (!(jugador.getAvatar() instanceof Pelota) || !jugador.getMovEspecial()) {
            throw new AvanzarException("Sólo se puede avanzar con el avatar pelota en modo avanzado");
        } else {
            Pelota pelota = (Pelota) jugador.getAvatar();
            puedeAvanzar(); // si no puede avanzar, devolverá una excepción, y no se moverá la pelota
            pelota.avanzar(tablero.getPosiciones(), banca);

        }
    }

    // * - - - Manejo propiedades - - - * //
    // Método que ejecuta todas las acciones relacionadas con el comando 'comprar'.
    // Desde menú recibe la casilla en la que se sitúa el jugador (sólo puedes
    // comprar
    // la casilla donde estás, excepto mediante un trato, que se maneja de otra
    // forma).
    @Override
    public void comprar(Casilla casilla, Jugador jugador) throws CompraNoDisponibleException {
        if (!jugador.getPuedeComprar() && jugador.getAvatar().getTipo().equals("coche") && jugador.getMovEspecial()) {
            throw new CompraNoDisponibleException(
                    "Al avanzar con el coche en modo avanzado, sólo puedes comprar una vez por turno.");
        }
        if (casilla instanceof Propiedad){
            Propiedad propiedad = (Propiedad) casilla;
            if (propiedad.esComprable(jugador, banca)) {
                consola.imprimir(
                        jugador.getNombre() + " compra la propiedad " + casilla.getNombre() + " por " + propiedad.getValor()
                        + ".");
                        propiedad.comprarCasilla(jugador, banca);
        }
        } else {
            throw new CompraNoDisponibleException("Esta casilla no es comprable");
        }
    }

    // Método que ejecuta todas las acciones relacionadas con el comando 'edificar'.
    // Desde menú recibe el tipo de edificio a construir como un string.
    @Override
    public void edificar(String args, Jugador jugador, Casilla casilla)
            throws EdificarIncorrectoException, EdificioNoPermitidoException, EdificioNoEncontradoException {
        if (!EDIFICIOS_VALIDOS.contains(args)) {
            throw new EdificarIncorrectoException();
        } else {
            if (casilla instanceof Solar){
                Solar solar = (Solar) casilla;
                e = new Edificio(args, casilla);
                solar.puedeConstruir(e, jugador); // Si no puede construir, lanzará una excepción
                consola.imprimir("Has comprado un(a) " + args + " en " + casilla.getNombre() + ", por "
                        + solar.valorEdificio(e.getTipo()) + ".");
                solar.anhadirEdificio(e, jugador);
            }
        }
    }

    // Método que ejecuta todas las acciones relacionadas con el comando 'destruir',
    // que
    // vende un edificio. Recibe el tipo de edificio a destruir como un string.
    @Override
    public void destruir(String args, Jugador jugador, Casilla casilla)
            throws EdificarIncorrectoException, EdificioNoEncontradoException {

        if (!EDIFICIOS_VALIDOS.contains(args)) {
            throw new EdificarIncorrectoException();
        } else {
            Solar solar = (Solar) casilla;
            solar.destruirEdificio(args, jugador);
            consola.imprimir("Has vendido un(a) " + args + " en " + casilla.getNombre() + ", por "
                    + solar.valorEdificio(args) / 2f + ".");

        }
    }

    // Método que realiza las acciones asociadas al comando 'hipotecar', que permite
    // a un
    // jugador hipotecar una propiedad. Recibe el nombre de la casilla a hipotecar.
    @Override
    public void hipotecar(String args, Jugador jugador) throws CasillaNoEncontradaException, HipotecaException {
        Casilla aHipotecar;
        aHipotecar = tablero.getCasilla(args);
        if (aHipotecar == null) {
            throw new CasillaNoEncontradaException(args);
        } else {
            aHipotecar.hipotecar(jugador);
            consola.imprimir("Se ha hipotecado " + aHipotecar.getNombre() + ". " + jugador.getNombre()
                    + " ha recibido " + aHipotecar.getHipoteca() * 1.1f
                    + "€ de la hipoteca.");

        }
    }

    // Método que realiza las acciones asociadas al comando 'deshipotecar', que
    // permite a un
    // jugador deshipotecar una propiedad. Recibe el nombre de la casilla a
    // deshipotecar.
    @Override
    public void deshipotecar(String args, Jugador jugador) throws CasillaNoEncontradaException, HipotecaException {
        Casilla aDeshipotecar;
        aDeshipotecar = tablero.getCasilla(args);
        if (aDeshipotecar == null) {
            throw new CasillaNoEncontradaException(args);
        } else {
            aDeshipotecar.deshipotecar(jugador);
            consola.imprimir("Se ha deshipotecado " + aDeshipotecar.getNombre() + ". " + jugador.getNombre()
                    + " ha pagado " + aDeshipotecar.getHipoteca()
                    + "€ de la hipoteca.");

        }
    }

    // * - - - Información partida - - - * //
    // Método invocado por el comando 'listar'. Permite listar jugadores, avatares,
    // propiedades en venta y edificios. Dependiendo del argumento, se invocan otros
    // métodos.
    @Override
    public void listar(String args) throws ListarIncorrectoException, NoEncontradoException {
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
            case "tratos":
                listarTratos();
                break;
            default:
                throw new ListarIncorrectoException();
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar jugadores'.
    @Override
    public void listarJugadores() {
        for (int i = 0; i < getNumeroDeJugadores(); i++) {
            descJugador(jugadores.get(i));
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar avatares'.
    public void listarAvatares() throws AvatarNoEncontradoException {
        for (int i = 0; i < getNumeroDeAvatares(); i++) {
            descAvatar(avatares.get(i));
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar enventa'.
    @Override
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

    // Método que realiza las acciones asociadas al comando 'listar tratos'.
    // Lista los tratos que han sido propuestos a un jugador.
    @Override
    public void listarTratos() {
        Jugador jugador = getJugadorTurno();
        for (Trato trato : jugador.getTratos()) {
            consola.imprimir(trato.toString());
        }

    }

    // Método que imprime los edificios existentes en la partida, casilla por
    // casilla.
    // Corresponde al comando 'listar edificios'.
    @Override
    public void listarEdificios() {
        for (int i = 0; i < tablero.getNumCasillas(); i++) {
            Casilla casilla = tablero.getCasilla(i);
            if (!casilla.getEdificios().isEmpty()) {
                Solar solar = (Solar) casilla;
                for (Edificio edificio : casilla.getEdificios()) {
                    consola.imprimir("\nID:" + edificio.getID());
                    consola.imprimir("Propietario: " + casilla.getduenhoJugador().getNombre());
                    consola.imprimir("Grupo: " + casilla.getGrupo().getNombre());
                    consola.imprimir("Coste: " + solar.valorEdificio(edificio.getTipo()));
                }
            }
        }

    }

    // Realiza las acciones asociadas al comando 'listar edificios [grupo]'. Esta
    // información
    // está en el objeto del grupo. Si el grupo no existe, lanza una excepción.
    @Override
    public void listarEdificiosGrupo(String nombreGrupo) throws GrupoNoEncontradoException {
        Grupo grupo = tablero.getGrupoNombre(nombreGrupo);
        if (grupo == null) {
            throw new GrupoNoEncontradoException(nombreGrupo);
        }
        grupo.descEdificios();

    }

    // Método que realiza las acciones asociadas al comando 'describir', que permite
    // describir jugadores, avatares y casillas. Dependiendo del argumento, se
    // invocan
    // otros métodos.
    @Override
    public void describir(String args) throws DescribirIncorrectoException, NoEncontradoException {
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
                    throw new DescribirIncorrectoException();
                } else {
                    descCasilla(args); // El uso de describir casilla es distinto (segun el enunciado: sólo 'describir
                    // Solar2' y no 'describir Casilla Solar2')
                }
                break;
        }
    }

    // Método que realiza las acciones asociadas al comando /describir jugador'.
    // Parámetro: nombre del jugador
    @Override
    public void descJugador(String nombre) throws JugadorNoEncontradoException {
        Jugador jugador = getJugador(nombre);
        if (!(jugador == null)) {
            descJugador(jugador);
        } else {
            throw new JugadorNoEncontradoException(nombre);
        }
    }

    // Método que imprime, mediante la consola, la información de un jugador,
    // pero recibe un objeto Jugador. Se invoca desde otros métodos, como listar.
    public void descJugador(Jugador jugador) {
        consola.imprimir("Nombre: " + jugador.getNombre());
        consola.imprimir("Avatar: " + jugador.getAvatar().getID());
        consola.imprimir("Fortuna: " + jugador.getFortuna());
        consola.imprimir("Propiedades:");
        for (int j = 0; j < jugador.getPropiedades().size(); j++) {
            consola.imprimir("  ||" + jugador.getPropiedades().get(j).getNombre() + "||");
            if (jugador.getPropiedades().get(j).getHipotecada()) {
                consola.imprimir("[H]");
            }
        }
        consola.imprimir("");
        consola.imprimir("Hipotecas: ");
        consola.imprimir("Edificios: ");
        consola.imprimir("");

    }

    // Método que realiza las acciones asociadas al comando 'describir avatar'.
    // Parámetro: id del avatar a describir.
    @Override
    public void descAvatar(String ID) throws AvatarNoEncontradoException {
        Avatar avatar = getAvatar(ID);
        if (avatar == null) {
            throw new AvatarNoEncontradoException(ID);
        } else {
            descAvatar(avatar);
        }
    }

    // Método que imprime, mediante la consola, la información de un avatar,
    // pero recibe un objeto Avatar. Se invoca desde otros métodos, como listar.
    public void descAvatar(Avatar avatar) {
        consola.imprimir("- ID: " + avatar.getID());
        consola.imprimir("- Tipo: " + avatar.getTipo());
        consola.imprimir("- Casilla: " + avatar.getLugar().getNombre());
        consola.imprimir("- Jugador: " + avatar.getJugador().getNombre());
        consola.imprimir("");
    }

    // Método que realiza las acciones asociadas al comando 'describir
    // nombre_casilla'.
    // Parámetros: nombre de la casilla a describir.
    @Override
    public void descCasilla(String nombre) throws CasillaNoEncontradaException {
        Casilla casilla = tablero.getCasilla(nombre);
        if (!(casilla == (null))) {
            consola.imprimir(casilla.infoCasilla(banca));
        } else {
            throw new CasillaNoEncontradaException(nombre);
        }
    }

    // Método que realiza las acciones asociadas al comando 'estadisticas'.
    // Imprime las estadísticas generales de la partida.
    @Override
    public void estadisticas() {
        consola.imprimir("casillaMasRentable: " + casillasMasRentables());
        consola.imprimir("grupoMasRentable: " + gruposMasRentables());
        consola.imprimir("casillaMasFrecuentada: " + casillasMasFrecuentadas());
        consola.imprimir("jugadorMasVueltas: " + jugadoresMasVueltas());
        consola.imprimir("jugadorMasVecesDados: " + jugadoresMasVecesDados());
        consola.imprimir("jugadorEnCabeza: " + jugadoresEnCabeza());
    }

    // Método que realiza las acciones asociadas al comando 'estadisticas jugador'.
    // Parámetro: nombre del jugador cuyas estadísticas imprimir. Lanza una
    // excepción
    // si el jugador no se encuentra en la partida.
    @Override
    public void estadisticasJugador(String args) throws JugadorNoEncontradoException {
        Jugador jugador = getJugador(args);
        if (jugador == null) {
            throw new JugadorNoEncontradoException(args);
        }
        jugador.estadisticas();
    }

    // * - - - Tratos - - - * //
    @Override
    public void aceptarTrato(Jugador jugador, String idTrato)
            throws TratoNoEncontradoException, EntradaNoNumericaException, TratoInvalidoException {
        Trato trato;
        try {
            trato = jugador.getTrato(Integer.parseInt(idTrato));
            if (trato == null) {
                throw new TratoNoEncontradoException(idTrato);
            }
            trato.aceptarTrato();
            consola.imprimir("Trato aceptado. (" + trato.toString() + ")");
            jugador.eliminarTrato(trato);
        } catch (NumberFormatException e) {
            throw new EntradaNoNumericaException();
        }
    }

    @Override
    public void rechazarTrato(Jugador jugador, String idTrato)
            throws TratoNoEncontradoException, EntradaNoNumericaException {
        Trato trato;
        try {
            trato = jugador.getTrato(Integer.parseInt(idTrato));
            if (trato == null) {
                throw new TratoNoEncontradoException(idTrato);
            }
            consola.imprimir("Trato rechazado. (" + trato.toString() + ")");
            jugador.eliminarTrato(trato);
        } catch (NumberFormatException e) {
            throw new EntradaNoNumericaException();
        }

    }

    // Método que realiza las acciones asociadas al comando 'trato'.
    // Lee las distintas partes del comando para crear el objeto trato, y si es válido, lo añade a la lista de tratos del jugador que lo recibe.
    // Lanza excepciones si el trato es incorrecto o si no se encuentra el jugador con el que se quiere hacer el trato.
    @Override
    public void nuevoTrato(Jugador jugador, String[] partesComando)
            throws TratoIncorrectoException, JugadorNoEncontradoException, TratoInvalidoException {
        // Uso correcto: trato [jugador]: cambiar (casilla y dinero, casilla y dinero)
        // Puede tener 5 (casilla por casilla o casilla por dinero) o 7 (casilla y
        // dinero por casilla, o viceversa) partes.
        Trato trato = null;
        Integer numElementos = partesComando.length;
        if (numElementos != 5 && numElementos != 7) {
            consola.imprimir("Número de elementos en el trato: " + numElementos);
            throw new TratoIncorrectoException();
        }

        for (int i = 0; i < partesComando.length; i++) {
            partesComando[i] = partesComando[i].replaceAll("[(),:]", "");
        }

        Jugador jugador2 = getJugador(partesComando[1]);
        if (jugador2 == null) {
            throw new JugadorNoEncontradoException(partesComando[1]);
        }

        // POSIBLES TRATOS
        String elemento1 = partesComando[3];
        String elemento2;
        float dinero = 0;
        Casilla casilla1 = tablero.getCasilla(elemento1);
        Casilla casilla2;

        try {
            dinero = Float.parseFloat(elemento1);
        } catch (NumberFormatException e) {
            // El elemento 1 no es un número --> debe ser una casilla
            casilla1 = tablero.getCasilla(elemento1);
            if (casilla1 == null) {
                // No es número ni casilla --> trato incorrecto
                consola.imprimir("No se ha encontrado la casilla " + elemento1);
                throw new TratoIncorrectoException();
            }
        }

        if (numElementos == 5) // trato 'simple', casilla por dinero, casilla por casilla o dinero por casilla
        {
            elemento2 = partesComando[4];
            try {
                dinero = Float.parseFloat(elemento2);
                if (casilla1 != null) {
                    trato = new Trato(jugador, jugador2, casilla1, dinero); // TIPO TRATO 1: casilla por dinero
                } else {
                    throw new TratoIncorrectoException(); // si se llega a este punto, se ha intentado cambiar dinero
                    // por dinero}
                }
            } catch (NumberFormatException e) {
                // El elemento 2 no es un número --> debe ser una casilla
                casilla2 = tablero.getCasilla(elemento2);
                if (casilla2 == null) {
                    // No es número ni casilla --> trato incorrecto
                    throw new TratoIncorrectoException();
                }
                if (dinero != 0) {
                    trato = new Trato(jugador, jugador2, dinero, casilla2); // TIPO TRATO 2: dinero por casilla

                }
                if (casilla1 != null) {
                    trato = new Trato(jugador, jugador2, casilla1, casilla2); // TIPO TRATO 3: casilla por casilla
                }
            }
        } else { // trato 'complejo', casilla y dinero por casilla o casilla por casilla y dinero
            String elemento3;
            if (partesComando[4].equals("y")) { // Jugador1 ofrece dos cosas.
                elemento2 = partesComando[5];
                elemento3 = partesComando[6];
                casilla2 = tablero.getCasilla(elemento3);
                if (casilla2 == null) { // Si J2 ofrece sólo una cosa, debe ser una casilla.
                    throw new TratoIncorrectoException();
                }
                try {
                    dinero = Float.parseFloat(elemento2); // dinero que ofrece J1
                    casilla2 = tablero.getCasilla(elemento3);
                    if (casilla2 != null) {
                        trato = new Trato(jugador, jugador2, casilla1, dinero, casilla2); // TIPO TRATO 4: casilla y
                        // dinero por casilla
                    } else {
                        throw new TratoIncorrectoException();
                    }
                } catch (NumberFormatException e) { // El elemento 2 ofrecido por J1 no es dinero --> debe ser una
                    // casilla, elemento1 debe ser un número.
                    casilla1 = tablero.getCasilla(elemento2);
                    try {
                        dinero = Float.parseFloat(elemento1);
                    } catch (NumberFormatException ex) {
                        throw new TratoIncorrectoException();
                    }
                    if (casilla1 == null) {
                        // No es número ni casilla --> trato incorrecto
                        throw new TratoIncorrectoException();
                    }
                    trato = new Trato(jugador, jugador2, casilla1, dinero, casilla2); // TIPO TRATO 4: casilla y dinero
                    // por casilla
                }
            } else { // Jugador2 ofrece dos cosas.
                if (casilla1 == null) { // si J2 ofrece dos cosas, lo que ofrece J1 debe ser una casilla.
                    throw new TratoIncorrectoException();
                }
                elemento2 = partesComando[4];
                elemento3 = partesComando[6];
                try {
                    dinero = Float.parseFloat(elemento3); // dinero que ofrece J2
                    casilla2 = tablero.getCasilla(elemento2);
                    if (casilla2 != null) {
                        trato = new Trato(jugador, jugador2, casilla1, casilla2, dinero); // TIPO TRATO 5: casilla por
                        // casilla y dinero
                    } else {
                        throw new TratoIncorrectoException();
                    }
                } catch (NumberFormatException e) { // El elemento 3 ofrecido por J2 no es dinero --> debe ser una
                    // casilla, elemento2 debe ser un número.
                    casilla2 = tablero.getCasilla(elemento3);
                    try {
                        dinero = Float.parseFloat(elemento2);
                    } catch (NumberFormatException ex) {
                        throw new TratoIncorrectoException();
                    }
                    if (casilla2 == null) {
                        // No es número ni casilla --> trato incorrecto
                        throw new TratoIncorrectoException();
                    }
                    trato = new Trato(jugador, jugador2, casilla1, casilla2, dinero); // TIPO TRATO 5: casilla por
                    // casilla y dinero
                }
            }
        }
        if (trato == null) {
            throw new TratoIncorrectoException();
        }
        trato.comprobarTratoValido(); // lanzará excepcion si el trato no es valido
        consola.imprimir(trato.toString());
        jugador2.anhadirTrato(trato);

    }

    // * - - - Comandos especiales - - - * //
    // Método que realiza las acciones asociadas al comando 'cambiar modo'.
    // Cambia el modo de movimiento de un jugador entre estándar y avanzado.
    public void cambiarModo(Jugador jugador) throws CambiarModoException {
        if (lanzamientos > 0) {
            throw new CambiarModoException();
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

    // Método que realiza las acciones asociadas al comando 'carta'.
    // Dependiendo de la carta que pueda coger el jugador (comunidad o suerte), coge
    // una baraja
    // y muestra las cartas disponibles. Luego, el jugador elige una carta y se
    // ejecuta su acción.
    @Override
    public void cogerCarta(Jugador jugador)
            throws CartaNoDisponibleException, CartaNoEncontradaException, EntradaNoNumericaException {
        int numero;
        if (jugador.puedeCogerCarta() == 0) {
            throw new CartaNoDisponibleException();
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
                consola.imprimir(
                        Valor.BLUE + entry.getKey() + "." + Valor.RESET + " " + entry.getValue().getTextoCarta());
            } else {
                consola.imprimir(
                        Valor.YELLOW + entry.getKey() + "." + Valor.RESET + " " + entry.getValue().getTextoCarta());
            }
        }
        try {
            numero = Integer.parseInt(consola.leer("Introduce un número entre 1 y 6:"));
        } catch (NumberFormatException ex) {
            throw new EntradaNoNumericaException();
        }
        if (numero < 1 || numero > 6) {
            throw new CartaNoEncontradaException(numero);

        } else {
            Carta carta = cartas.get(numero);
            consola.imprimir("Has seleccionado: " + carta.getTextoCarta());
            carta.accionCarta(jugadores, jugador, tablero);
            jugador.setCartaDisponible(0);
        }
    }

    // Método que ejecuta todas las acciones relacionadas con el comando 'salir
    // carcel', que implica pagar la multa. Lanza una excepción si el jugador no
    // puede
    // salir de la cárcel o pagar la multa.
    @Override
    public void salirCarcel(Jugador jugador) throws SalirCarcelException {
        if (!jugador.getEnCarcel()) {
            throw new SalirCarcelException("No estás en la cárcel");
        }
        if (lanzamientos != 0) {
            throw new SalirCarcelException("Sólo puedes pagar la multa al inicio del turno");

        }
        if (!getJugadorTurno().pagarMulta()) {
            throw new SalirCarcelException("No tienes suficiente dinero para pagar la multa");
        }
        // Si no ha saltado ninguna excepción, el jugador sale de la cárcel.
        Juego.consola.imprimir("Pagas la multa y sales de la cárcel.");
        jugador.salirCarcel();

    }

    // Método que realiza las acciones asociadas al comando 'acabar turno'.
    // Si el jugador está en deuda, en la cárcel o tiene que coger una carta, lanza
    // una excepción. Si no, pasa al siguiente turno.
    @Override
    public void acabarTurno(Jugador jugador) throws AcabarTurnoException {

        if (jugador.getFortuna() < 0) {
            throw new AcabarTurnoException(
                    "Estás en deuda. Debes hipotecar propiedades, vender edificios o declarar la bancarrota");
        }
        if (jugador.limiteCarcel()) {
            throw new AcabarTurnoException("Debes pagar la multa para salir de la cárcel");
        }
        if (jugador.puedeCogerCarta() != 0) {
            throw new AcabarTurnoException("Debes coger una carta antes de acabar el turno");
        }
        turno = (turno + 1) % getNumeroDeJugadores();
        nuevoTurno();

    }

    // Método que realiza las acciones asociadas al comando 'bancarrota'.
    // Si la deuda no es con ningún jugador (ej: un impuesto), se pasa a la banca.
    // Si el jugador en bancarrota es el último, se acaba la partida.
    // Si no, cede el turno al siguiente jugador.
    @Override
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

        for (int i = 0; i < array_propiedades.size(); i++) { // Los edificios se eliminan.
            array_propiedades.get(i).getEdificios().clear();
            if (jugadorRecibe != banca) {
                jugadorRecibe.anhadirPropiedad(array_propiedades.get(i));
            }
        }
        if (!jugadorRecibe.equals(banca)) { // Si el jugador con quien es la deuda no es la banca, este jugador recibe
            // la fortuna que le quedase al jugador.
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
        } else {
            nuevoTurno();
        }
    }

    // Comando para cambiar la fortuna de un jugador manualmente. Sólo para pruebas.
    @Override
    public void fortunaManual(String args, Jugador jugador) throws FortunaManualException {
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
            throw new FortunaManualException();
        }
    }

    // * - - - Métodos auxiliares - - - * //
    // Método para inciar una partida: crea los jugadores y avatares.
    // Este método se incluye en Juego porque es esta clase la que contiene las
    // listas
    // de jugadores y avatares.
    public void iniciarPartida(Tablero t) {

        int numJugadores = 0;

        while (numJugadores < 2 || numJugadores > 6) {
            try {
                numJugadores = Integer.parseInt(consola.leer("Introduce el número de jugadores (2-6): "));
            } catch (NumberFormatException ex) {
            }
        }
        for (int i = 0; i < numJugadores; i++) {
            anhadirJugador();
        }

        consola.imprimir("Partida iniciada con " + getNumJugadores() + " jugadores.");
        consola.imprimir("Empieza la partida: " + getJugadorTurno().getNombre() + ".");

    }

    // Inicia un nuevo turno, reiniciando las variables correspondientes.
    // Llamado por 'acabarTurno' y 'bancarrota'.
    private void nuevoTurno() {
        getJugadorTurno().setPuedeComprar(true);
        lanzamientos = 0;
        dobles_seguidos = 0;
        consola.imprimir("Turno de " + getJugadorTurno().getNombre() + ".");

    }

    // realiza las comprobaciones previas a poder lanzar los dados
    // lanza excepciones si no se cumplen las condiciones
    public void puedeLanzarDados() throws DadosException, AvanzarException {
        Jugador jugador = getJugadorTurno();
        Avatar avatar = jugador.getAvatar();

        if (jugador.getEnCarcel()) {
            if (jugador.getTiradasCarcel() > 2) {
                throw new DadosException("has pasado 3 turnos en la cárcel, debes pagar la multa.");
            } else if (lanzamientos > 0) {
                throw new DadosException("sólo puedes intentar sacar dobles para liberarte al inicio del turno.");
            }
        }
        if (lanzamientos > 0 && dobles_seguidos == 0 && (!(avatar instanceof Coche) || !jugador.getMovEspecial())) {
            throw new DadosException("ya has lanzado los dados en este turno.");

        }
        if (avatar instanceof Coche coche) {
            if (coche.getCocheCalado() > 0){
                throw new DadosException(
                        "por una previa tirada con el coche. Debes esperar " + (coche.getCocheCalado() - 1)
                        + " turnos para volver a lanzar los dados.");

            }
        }
        if ((avatar instanceof Coche) && jugador.getMovEspecial() && lanzamientos > 3) {
            if (dobles_seguidos == 0) {
                throw new DadosException("ya has lanzado los dados 4 veces en este turno.");
            }
            if (lanzamientos > 4) {
                throw new DadosException("sacar dobles te permite sólo una quinta tirada con el coche.");
            }
        }

        if (jugador.getMovEspecial() && (avatar instanceof Pelota)) {
            if (((Pelota) avatar).siguienteMovPelota(false) == 0) {
                return;
            }
            // la primera tirada de la pelota no implica moverse, no es necesario comprobar
            // si se puede avanzar
        }
        puedeAvanzar();
    }

    // similar a puedeLanzarDados pero comprueba si el avatar puede moverse
    // actualmente, sea con una tirada normal de dados o con avanzar
    private void puedeAvanzar() throws AvanzarException {
        Jugador jugador = getJugadorTurno();
        Avatar avatar = jugador.getAvatar();

        if (jugador.getFortuna() < 0) {
            throw new AvanzarException(
                    "Estás en deuda. Vende edificios, hipoteca propiedades o declara la bancarrota.");
        }
        if (jugador.puedeCogerCarta() != 0) {
            throw new AvanzarException("Debes coger una carta antes de avanzar.");

        }

        if (jugador.getMovEspecial() && (avatar instanceof Pelota)
                && ((Pelota) avatar).siguienteMovPelota(false) == 0) {
            throw new AvanzarException("No te quedan movimientos.");

        }
    }

    // Devuelve una string con una lista de todos los nombres de jugadores
    // que han dado el máximo de vueltas.
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

    // Devuelve una string con una lista de todos los nombres de casillas
    // que han dado el máximo beneficio.
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

    // Devuelve una string con una lista de todos los nombres de grupos
    // que han dado el máximo beneficio.
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

    // Devuelve una string con una lista de todos los nombres de casillas
    // en las que se ha caído más veces.
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

    // Devuelve una string con una lista de todos los nombres de jugadores
    // que han tirado más veces los dados.
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

    // Devuelve una string con una lista de todos los nombres de jugadores
    // que poseen más fortuna, contando con sus propiedades.
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

    // Comprueba si todos los jugadores han dado 4 vueltas al tablero, y si el
    // último avatar en moverse
    // ha dado, en su último movimiento, una vuelta que ha completado esta
    // condición. Si es el caso,
    // aumenta el coste de las propiedades.
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

    // SETTERS
    public void setLanzamientos(Integer newLanzamientos) {
        lanzamientos = newLanzamientos;
    }

    public void setPartidaAcabada(boolean p) {
        partidaAcabada = p;
    }

    // GETTERS
    private int getNumJugadores() {
        return jugadores.size();
    }

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
