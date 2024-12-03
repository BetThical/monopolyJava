package monopoly;

import partida.*;

public class Trato {

    private final Jugador jugador1; // Jugador que propone el trato
    private final Jugador jugador2; // Jugador que recibe el trato
    private final String descripcion;
    private final float dinero;
    private final Casilla casilla1;
    private final Casilla casilla2;
    public Trato(Jugador jugador1, Jugador jugador2, Casilla casilla1, Casilla casilla2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.dinero = 0;
        this.casilla1 = casilla1;
        this.casilla2 = casilla2;

        this.descripcion = jugador1.getNombre() + " le da " + Valor.GREEN + casilla1.getNombre() + Valor.RESET + " a cambio de " + Valor.YELLOW + casilla2.getNombre() + Valor.RESET + " a " + jugador2.getNombre();
    }

    public Trato(Jugador jugador1, Jugador jugador2, float dinero, Casilla casilla2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.dinero = dinero;
        this.casilla1 = null;
        this.casilla2 = casilla2;
        this.descripcion = jugador1.getNombre() + " le da " + Valor.GREEN + dinero + "€" + Valor.RESET + " a cambio de " + Valor.YELLOW + casilla2.getNombre() + Valor.RESET + " a " + jugador2.getNombre();
    }

    public Trato(Jugador jugador1, Jugador jugador2, Casilla casilla1, float dinero) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.dinero = dinero;
        this.casilla1 = casilla1;
        this.casilla2 = null;
        this.descripcion = jugador1.getNombre() + " le da " + Valor.GREEN + casilla1.getNombre() + Valor.RESET + " a canbio de " + Valor.YELLOW + dinero + "€" + Valor.RESET + " a " + jugador2.getNombre();
    }

    public Trato(Jugador jugador1, Jugador jugador2, Casilla casilla1, Casilla casilla2, float dinero) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.dinero = dinero;
        this.casilla1 = casilla1;
        this.casilla2 = casilla2;
        this.descripcion = jugador1.getNombre() + " le da " + Valor.GREEN + casilla1.getNombre() + Valor.RESET + " a cambio de " + Valor.YELLOW + casilla2.getNombre() + " y " + dinero + "€" + Valor.RESET + " a " + jugador2.getNombre();
    }

    public Trato(Jugador jugador1, Jugador jugador2, Casilla casilla1, float dinero, Casilla casilla2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.dinero = dinero;
        this.casilla1 = casilla1;
        this.casilla2 = casilla2;
        this.descripcion = jugador1.getNombre() + " le da " + Valor.GREEN + casilla1.getNombre() + " y " + dinero + "€" + Valor.RESET + " a cambio de " + Valor.YELLOW + casilla2.getNombre() + Valor.RESET + " a " + jugador2.getNombre();
    }

    @Override
    public String toString() {
        return "Trato propuesto: " + descripcion;
    }

    

    public Jugador getJugador1() {
        return jugador1;
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
