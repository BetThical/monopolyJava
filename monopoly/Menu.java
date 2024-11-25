package monopoly;

import partida.*;



public final class Menu{
    
    public final Juego juego = new Juego();
    
    public void titulo() {

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
        Juego.consola.imprimir(
                "                                ___       ___          ___          ___                           ");
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

        Juego.consola.leer("\n\n\n                                Pulse ENTER para iniciar una partida.\n\n");

    }

    public void loopJugable() {
        String comando = "";
        while (!juego.getPartidaAcabada()) {
            Jugador jugador = juego.getJugadorTurno();
            juego.getTablero().imprimirTablero();

            if (jugador.getFortuna() < 0) {
                if (jugador.getEnDeuda() == null) {
                    jugador.setEnDeuda(juego.getBanca());
                }
                Juego.consola.imprimir(
                        Valor.RED + "[AVISO]:" + Valor.RESET
                        + " actualmente estás en deuda (" + juego.getJugadorTurno().getFortuna()
                        + "). Debes destruir edificios, hipotecar propiedades o declarar la bancarrota.");
            }
            if (jugador.limiteCarcel() && comando.equals("a")) { // a comprobación é solo ao inicio do
                // turno
                if (!jugador.pagarMulta()) {
                    jugador.setEnDeuda(juego.getBanca());
                    break;
                }
            }
            comando = Juego.consola.leer(jugador.getColor() + "[" + jugador.getNombre() + "]: " + Valor.RESET);
            analizarComando(comando);


        }
        Juego.consola.imprimir("La partida ha terminado! El ganador es " + juego.getJugadorTurno().getNombre() + ".");
    }

/*
         * Método que interpreta el comando introducido y toma la accion
         * correspondiente.
         * Parámetro: cadena de caracteres (el comando).
     */
    private void analizarComando(String input) {
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
                    if (juego.puedeLanzarDados()) {
                        juego.lanzarDados();
                    }
                } else if (input.contains("lanzar dados ")) { // DEBUG: lanzar dados manual (lanzar dados [x]+[y])
                    if (juego.puedeLanzarDados()) {
                        try {
                            String[] valores = partesComando[2].split("\\+");
                            juego.lanzarDados(Integer.parseInt(valores[0]), Integer.parseInt(valores[1]));
                        } catch (NumberFormatException ex) {
                            Juego.consola.imprimir("Uso del comando: lanzar dados [tirada1]+[tirada2]");
                        }
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
                } catch (NumberFormatException ex) {
                    Juego.consola.imprimir("Uso del comando: m [cantidad de casillas]");
                }
                break;

            // - - - manejo propiedades --- //
            case "comprar":
                juego.comprar(casilla);
                break;

            case "edificar":
                juego.edificar(partesComando[1], jugador, casilla);
                break;

            case "destruir":
                juego.destruir(partesComando[1], jugador, casilla);
                break;

            case "hipotecar":
                juego.hipotecar(partesComando[1], jugador);
                break;

            case "deshipotecar":
                juego.deshipotecar(partesComando[1], jugador);
                break;

            // - - - acciones misceláneas --- //
            case "cambiar":
                if (input.equals("cambiar modo")) {
                    juego.cambiarModo(jugador);
                }
                break;

            case "carta":
                juego.cogerCarta(jugador);
                break;
            case "salir":
                if (input.equals("salir carcel")) {
                    juego.salirCarcel(jugador);
                }
                break;

            case "bancarrota":
                juego.bancarrota(jugador);
                break;

            case "acabar":
                if (input.equals("acabar turno")) {
                    juego.acabarTurno(jugador);
                }
                break;

            // - - - info partida --- //
            case "listar":
                juego.listar(partesComando[1]);
                break;

            case "describir ": // incluye describir avatar, jugador o casilla
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
                    // no hace nada
                }
                break;

            case "f": // fortuna manual (debug)
                juego.fortunaManual(partesComando[1], jugador);
                break;

            default:
                Juego.consola.imprimir("Comando inválido.");
                break;
        }
    }
}
