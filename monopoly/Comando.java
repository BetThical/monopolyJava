package monopoly;

import partida.Jugador;

public interface Comando {

    // --- jugadores --- //
    void anhadirJugador();

    void jugadorActual(Jugador jugador);

    // - - - movimientos --- //
    void lanzarDados();
    void lanzarDados(int tirada1, int tirada2);
    void avanzar(Jugador jugador);

    // - - - manejo propiedades --- //
    void comprar(Casilla casilla);
    void edificar(String args, Jugador jugador, Casilla casilla);
    void destruir(String args, Jugador jugador, Casilla casilla);
    void hipotecar(String args, Jugador jugador);
    void deshipotecar(String args, Jugador jugador);

    // - - - acciones misceláneas --- //
    void cambiarModo(Jugador jugador);
    void cogerCarta(Jugador jugador);
    boolean salirCarcel(Jugador jugador);
    void bancarrota(Jugador jugadorBancarrota, Jugador jugadorRecibe);
    void acabarTurno(Jugador jugador); 

    // - - - info partida --- //
    void listar(String args);
    void listarJugadores();
    void listarAvatares();
    void listarEdificios();
    void listarEdificiosGrupo(String nombreGrupo);
    void describir(String args);
    void estadisticas();
    void estadisticasJugador(String nombreJugador);
    



}
