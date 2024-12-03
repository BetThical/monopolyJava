package monopoly;

import exception.MonopolyException;
import exception.comandoIncorrectoException.DadosManualesException;
import exception.comandoIncorrectoException.PropiedadNoIndicadaException;
import partida.*;

public final class Menu {

    private Juego juego;

    public void titulo() {
        Juego.consola.imprimir(Valor.RED);

        Juego.consola.imprimir(
                "      ___          ___          ___          ___          ___          ___          ___   ___     ");
        Juego.consola.imprimir(
                "     /\\__\\        /\\  \\        /\\__\\        /\\  \\        /\\  \\        /\\  \\        /\\__\\ |\\__\\    ");
        Juego.consola.imprimir(
                "    /::|  |      /::\\  \\      /::|  |      /::\\  \\      /::\\  \\      /::\\  \\      /:/  / |:|  |   ");
        Juego.consola.imprimir(
                "   /:|:|  |     /:/\\:\\  \\    /:|:|  |     /:/\\:\\  \\    /:/\\:\\  \\    /:/\\:\\  \\    /:/  /  |:|  |   ");
        Juego.consola.imprimir(
                "  /:/|:|__|__  /:/  \\:\\  \\  /:/|:|  |__  /:/  \\:\\  \\  /::\\~\\:\\  \\  /:/  \\:\\  \\  /:/  /   |:|__|__ ");
        Juego.consola.imprimir(
                " /:/ |::::\\__/\\:/__/ \\:\\__/\\:/ |:| /\\__/\\:/__/ \\:\\__/\\:/\\:\\ \\:\\__/\\:/__/ \\:\\__/\\:/__/    /::::\\__\\");
        Juego.consola.imprimir(
                " \\/__/~~/:/  /\\:\\  \\ /:/  /\\/__|:|/:/  /\\:\\  \\ /:/  /\\/__\\:\\/:/  /\\:\\  \\ /:/  /\\:\\  \\   /:/~~/~   ");
        Juego.consola.imprimir(
                "       /:/  /  \\:\\  /:/  /     |:/:/  /  \\:\\  /:/  /      \\::/  /  \\:\\  /:/  /  \\:\\  \\ /:/  /     ");
        Juego.consola.imprimir(
                "      /:/  /    \\:\\/:/  /      |::/  /    \\:\\/:/  /        \\/__/    \\:\\/:/  /    \\:\\  \\/__/      ");
        Juego.consola.imprimir(
                "     /:/  /      \\::/  /       /:/  /      \\::/  /                   \\::/  /      \\:\\__\\          ");
        Juego.consola.imprimir(
                "     \\/__/        \\/__/        \\/__/        \\/__/                     \\/__/        \\/__/          ");
        Juego.consola.imprimir(Valor.BLUE
                + "                                ___       ___          ___          ___                           ");
        Juego.consola.imprimir(
                "                               /\\  \\     /\\  \\        /\\  \\        /\\  \\                          ");
        Juego.consola.imprimir(
                "                              /::\\  \\    \\:\\  \\      /::\\  \\      /::\\  \\                         ");
        Juego.consola.imprimir(
                "                            /::\\~\\:\\  \\   /::\\  \\  _\\:\\~\\ \\  \\  /::\\~\\:\\  \\                       ");
        Juego.consola.imprimir(
                "                           /:/\\:\\ \\:\\__/ /:/\\:\\__/\\/ \\:\\ \\ \\__/\\:/\\:\\ \\:\\__/                      ");
        Juego.consola.imprimir(
                "                           \\:\\~\\:\\ \\/__//:/  \\/__/\\:\\ \\:\\ \\/__/\\:\\~\\:\\ \\/__/                      ");
        Juego.consola.imprimir(
                "                            \\:\\ \\:\\__\\ /:/  /      \\:\\ \\:\\__\\   \\:\\ \\:\\__\\                        ");
        Juego.consola.imprimir(
                "                             \\:\\ \\/__/ \\/__/        \\:\\/:/  /    \\:\\ \\/__/                        ");
        Juego.consola.imprimir(
                "                              \\:\\__\\                 \\::/  /      \\:\\__\\                          ");
        Juego.consola.imprimir(
                "                               \\/__/                  \\/__/        \\/__/                          ");

        Juego.consola
                .leer(Valor.RESET + "\n\n\n                                Pulse ENTER para iniciar una partida.\n\n");

    }

    public void loopJugable() {
        titulo();
        juego = new Juego();
        String comando;

        while (!juego.getPartidaAcabada()) {
            Jugador jugador = juego.getJugadorTurno();
            juego.getTablero().imprimirTablero();

            if (jugador.getFortuna() < 0) {
                Juego.consola.imprimir(
                        Valor.RED + "[AVISO]:" + Valor.RESET
                        + " actualmente estás en deuda (" + juego.getJugadorTurno().getFortuna()
                        + "). Debes destruir edificios, hipotecar propiedades o declarar la bancarrota.");
            }
            comando = Juego.consola.leer(jugador.getColor() + "[" + jugador.getNombre() + "]: " + Valor.RESET);

            try {
                analizarComando(comando);
            } catch (MonopolyException ex) {
                Juego.consola.imprimir(ex.getMessage());
            }

        }
        Juego.consola.imprimir("La partida ha terminado! El ganador es " + juego.getJugadorTurno().getNombre() + ".");
    }

    /*
     * Método que interpreta el comando introducido y toma la accion
     * correspondiente.
     * Parámetro: cadena de caracteres (el comando).
     */
    private void analizarComando(String input) throws MonopolyException {
        Jugador jugador = juego.getJugadorTurno();
        Casilla casilla = juego.getJugadorTurno().getAvatar().getLugar();
        String[] partesComando = input.split(" ");

        switch (partesComando[0]) {
            // --- jugadores --- //
            case "añadir":
                if (input.equals("añadir jugador")) {
                    juego.anhadirJugador();
                }
                break;

            case "jugador":
                juego.jugadorActual(jugador);
                break;

            // - - - movimientos --- //
            case "lanzar":
                if (input.equals("lanzar dados")) { // lanzar dados aleatorio
                    juego.lanzarDados();
                } else if (input.contains("lanzar dados ")) { // DEBUG: lanzar dados manual (lanzar dados [x]+[y])
                    try {
                        String[] valores = partesComando[2].split("\\+");
                        juego.lanzarDados(Integer.parseInt(valores[0]), Integer.parseInt(valores[1]));
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                        throw new DadosManualesException();
                    }

                }
                break;

            case "avanzar": // 'avanzar' usado en mov pelota
                juego.avanzar(jugador);
                break;

            case "m": // DEBUG: moverse manualmente (m [x])
                try {
                    juego.lanzarDados(Integer.parseInt(partesComando[1]), 0);
                    juego.setLanzamientos(juego.getLanzamientos() + 1);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                    throw new DadosManualesException();
                }
                break;

            // - - - manejo propiedades --- //
            case "comprar":
                juego.comprar(casilla, jugador);
                break;

            case "edificar":
                try {
                    juego.edificar(partesComando[1], jugador, casilla);
                    break;
                } catch (ArrayIndexOutOfBoundsException ex) {
                    throw new exception.comandoIncorrectoException.EdificarIncorrectoException();
                }

            case "destruir":
                try {
                    juego.destruir(partesComando[1], jugador, casilla);
                    break;
                } catch (ArrayIndexOutOfBoundsException ex) {
                    throw new exception.comandoIncorrectoException.EdificarIncorrectoException();
                }

            case "hipotecar":
                try {
                    juego.hipotecar(partesComando[1], jugador);
                    break;
                } catch (ArrayIndexOutOfBoundsException ex) {
                    throw new PropiedadNoIndicadaException("hipotecar");
                }

            case "deshipotecar":
                try {
                    juego.deshipotecar(partesComando[1], jugador);
                    break;
                } catch (ArrayIndexOutOfBoundsException ex) {
                    throw new PropiedadNoIndicadaException("deshipotecar");
                }

            // - - - info partida --- //
            case "listar":
                if (input.equals("listar")) { // si solo se escribe 'listar'. 'listar' con un arg invalido se capta en
                    // juego.java
                    throw new exception.comandoIncorrectoException.ListarIncorrectoException();
                }
                juego.listar(partesComando[1]);
                break;

            case "describir": // incluye describir avatar, jugador o casilla
                juego.describir(input.replace("describir ", ""));
                break;

            case "estadisticas":
                if (input.equals("estadisticas")) {
                    juego.estadisticas(); // estadisticas partida
                } else if (input.contains("estadisticas ")) {
                    juego.estadisticasJugador(partesComando[1]); // estadisticas jugador
                }
                break;

            case "ver":
                if (input.equals("ver tablero")) {
                    // no hace nada. el tablero se imprime en todas las iteraciones.
                }
                break;

            // - - - acciones misceláneas --- //

            case "trato":
                juego.nuevoTrato(jugador, partesComando);
                break;

            case "cambiar":
                if (input.equals("cambiar modo")) {
                    juego.cambiarModo(jugador);
                }
                break;

            case "carta":
                juego.cogerCarta(jugador);
                break;
            case "salir":
                juego.salirCarcel(jugador);
                break;

            case "bancarrota":
                juego.bancarrota(jugador);
                break;

            case "acabar":
                if (input.equals("acabar turno")) {
                    juego.acabarTurno(jugador);
                }
                break;

            case "f": // fortuna manual (debug)
                try {
                    juego.fortunaManual(partesComando[1], jugador);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    throw new exception.comandoIncorrectoException.FortunaManualException();
                }
                break;

            default:
                throw new exception.comandoInvalidoException.ComandoInvalidoException(
                        "El comando introducido no es válido.");
        }
    }
}
