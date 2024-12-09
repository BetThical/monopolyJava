package monopoly;

import exception.comandoInvalidoException.TratoInvalidoException;

import partida.*;

public class Trato {
    static int numTratos = 0;
    private int idTrato; // Identificador del trato
    private final Jugador jugador1; // Jugador que propone el trato
    private final Jugador jugador2; // Jugador que recibe el trato
    private final String descripcion;
    private final float dinero;
    private final int tipoTrato;
    private final Propiedad casilla1;
    private final Propiedad casilla2;

    public Trato(Jugador jugador1, Jugador jugador2, Propiedad casilla1, Propiedad casilla2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.dinero = 0;
        this.casilla1 = casilla1;
        this.casilla2 = casilla2;
        this.tipoTrato = 1;
        this.idTrato = ++numTratos;
        this.descripcion = jugador1.getNombre() + " le da " + Valor.GREEN + casilla1.getNombre() + Valor.RESET
                + " a cambio de " + Valor.YELLOW + casilla2.getNombre() + Valor.RESET + " a " + jugador2.getNombre();
    }

    public Trato(Jugador jugador1, Jugador jugador2, float dinero, Propiedad casilla2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.dinero = dinero;
        this.casilla1 = null;
        this.casilla2 = casilla2;
        this.tipoTrato = 2;
        this.idTrato = ++numTratos;
        this.descripcion = jugador1.getNombre() + " le da " + Valor.GREEN + dinero + "€" + Valor.RESET + " a cambio de "
                + Valor.YELLOW + casilla2.getNombre() + Valor.RESET + " a " + jugador2.getNombre();
    }

    public Trato(Jugador jugador1, Jugador jugador2, Propiedad casilla1, float dinero) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.dinero = dinero;
        this.casilla1 = casilla1;
        this.casilla2 = null;
        this.tipoTrato = 3;
        this.idTrato = ++numTratos;
        this.descripcion = jugador1.getNombre() + " le da " + Valor.GREEN + casilla1.getNombre() + Valor.RESET
                + " a cambio de " + Valor.YELLOW + dinero + "€" + Valor.RESET + " a " + jugador2.getNombre();
    }

    public Trato(Jugador jugador1, Jugador jugador2, Propiedad casilla1, Propiedad casilla2, float dinero) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.dinero = dinero;
        this.casilla1 = casilla1;
        this.casilla2 = casilla2;
        this.tipoTrato = 4;
        this.idTrato = ++numTratos;
        this.descripcion = jugador1.getNombre() + " le da " + Valor.GREEN + casilla1.getNombre() + Valor.RESET
                + " a cambio de " + Valor.YELLOW + casilla2.getNombre() + " y " + dinero + "€" + Valor.RESET + " a "
                + jugador2.getNombre();
    }

    public Trato(Jugador jugador1, Jugador jugador2, Propiedad casilla1, float dinero, Propiedad casilla2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.dinero = dinero;
        this.casilla1 = casilla1;
        this.casilla2 = casilla2;
        this.tipoTrato = 5;
        this.idTrato = ++numTratos;
        this.descripcion = jugador1.getNombre() + " le da " + Valor.GREEN + casilla1.getNombre() + " y " + dinero + "€"
                + Valor.RESET + " a cambio de " + Valor.YELLOW + casilla2.getNombre() + Valor.RESET + " a "
                + jugador2.getNombre();
    }

    @Override
    public String toString() {
        return Valor.BLUE + "Trato #" + idTrato + Valor.RESET + ": " + descripcion;
    }

    // Método que comprueba si un trato SE PUEDE PROPONER: si las casillas son propiedad de los jugadores 
    // involucrados en el trato.
    public void comprobarTratoValido() throws TratoInvalidoException {
        if (casilla1 != null && casilla1.getDuenho() != jugador1) {
            throw new TratoInvalidoException(
                    "El jugador " + jugador1.getNombre() + " no es dueño de la casilla " + casilla1.getNombre());
        }
        if (casilla2 != null && casilla2.getDuenho() != jugador2) {
            throw new TratoInvalidoException(
                    "El jugador " + jugador2.getNombre() + " no es dueño de la casilla " + casilla2.getNombre());
        }
    }

    // Método que comprueba si un trato SE PUEDE ACEPTAR: si los jugadores tienen suficiente dinero para
    // realizar el trato, controlan las casillas indicadas, etc.
    public void aceptarTrato() throws TratoInvalidoException {
        comprobarTratoValido();
        if ((tipoTrato == 2 || tipoTrato == 5) && jugador1.getFortuna() < dinero) {
            throw new TratoInvalidoException("El jugador " + jugador1.getNombre() + " no tiene suficiente dinero.");
        }
        if ((tipoTrato == 3 || tipoTrato == 4) && jugador2.getFortuna() < dinero) {
            throw new TratoInvalidoException("El jugador " + jugador2.getNombre() + " no tiene suficiente dinero.");
        }

        switch (tipoTrato) {
            case 1:
                jugador1.darCasilla(casilla1, jugador2);
                jugador2.darCasilla(casilla2, jugador1);
                break;

            case 2:
                jugador1.pagar(dinero, jugador2, false);
                jugador2.darCasilla(casilla2, jugador1);
                break;

            case 3:
                jugador1.darCasilla(casilla1, jugador2);
                jugador2.pagar(dinero, jugador1, false);
                break;

            case 4:
                jugador1.darCasilla(casilla1, jugador2);
                jugador2.darCasilla(casilla2, jugador1);
                jugador2.pagar(dinero, jugador2, false);
                break;
            case 5:
                jugador1.darCasilla(casilla1, jugador2);
                jugador2.darCasilla(casilla2, jugador1);
                jugador1.pagar(dinero, jugador2, false);

                break;

        }
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public int getIdTrato() {
        return idTrato;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    public float getDinero() {
        return dinero;
    }

    public Casilla getCasilla1() {
        return casilla1;
    }

    public Casilla getCasilla2() {
        return casilla2;
    }
}
