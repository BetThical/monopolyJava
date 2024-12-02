package monopoly;
import exception.comandoIncorrectoException.*;
import exception.comandoInvalidoException.*;
import exception.noEncontradoException.*;
import partida.Jugador;

public interface Comando {

    // * - - - Jugadores - - - * //
    void anhadirJugador();

    void jugadorActual(Jugador jugador);

    // * - - - Movimientos - - - * //
    void lanzarDados() throws DadosException, AvanzarException;

    void lanzarDados(int tirada1, int tirada2) throws DadosException, AvanzarException;

    void avanzar(Jugador jugador) throws AvanzarException;

    // * - - - Manejo propiedades - - - * //
    void comprar(Casilla casilla, Jugador jugador) throws CompraNoDisponibleException;

    void edificar(String args, Jugador jugador, Casilla casilla) throws EdificioNoValidoException, EdificioNoPermitidoException, FondosInsuficientesException;

    void destruir(String args, Jugador jugador, Casilla casilla) throws EdificioNoValidoException;

    void hipotecar(String args, Jugador jugador) throws CasillaNoEncontradaException;

    void deshipotecar(String args, Jugador jugador) throws CasillaNoEncontradaException;

    // * - - - Información partida - - - * //
    void listar(String args) throws ListarIncorrectoException, NoEncontradoException;

    void listarJugadores();

    void listarVenta();

    void listarEdificios();

    void listarEdificiosGrupo(String nombreGrupo) throws GrupoNoEncontradoException;

    void describir(String args) throws DescribirIncorrectoException, NoEncontradoException;

    void descJugador(String nombre) throws JugadorNoEncontradoException;

    void descAvatar(String ID) throws AvatarNoEncontradoException;

    void descCasilla(String nombre) throws CasillaNoEncontradaException;

    void estadisticas();

    void estadisticasJugador(String args) throws JugadorNoEncontradoException;

    // * - - - Comandos misceláneos - - - * //
    void cogerCarta(Jugador jugador) throws CartaNoDisponibleException, CartaNoEncontradaException, EntradaNoNumericaException;

    void salirCarcel(Jugador jugador) throws SalirCarcelException;

    void acabarTurno(Jugador jugador) throws AcabarTurnoException;

    void bancarrota(Jugador jugadorBancarrota);

    void fortunaManual(String args, Jugador jugador) throws FortunaManualException;
}
