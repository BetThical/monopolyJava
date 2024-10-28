package partida;

import java.util.ArrayList;
import monopoly.*;

public class Jugador {

    // Atributos:
    private String nombre; // Nombre del jugador
    private Avatar avatar; // Avatar que tiene en la partida.
    private float fortuna; // Dinero que posee.
    private float gastos; // Gastos realizados a lo largo del juego.
    private boolean enCarcel; // Será true si el jugador está en la carcel
    private int tiradasCarcel; // Cuando está en la carcel, contará las tiradas sin éxito que ha hecho allí
                               // para intentar salir (se usa para limitar el numero de intentos).
    private int vueltas; // Cuenta las vueltas dadas al tablero.
    private ArrayList<Casilla> propiedades; // Propiedades que posee el jugador.
    private float bote; // Usado por la banca para almacenar el bote

    private float dineroInvertido = 0;
    private float pagoTasasEImpuestos = 0;
    private float pagoDeAlquileres = 0;
    private float cobroDeAlquileres = 0;
    private float pasarPorCasillaDeSalida = 0;
    private float vecesEnLaCarcel = 0;

    // Constructor vacío. Se usará para crear la banca.
    public Jugador() {

    }

    /*
     * Constructor principal. Requiere parámetros:
     * Nombre del jugador, tipo del avatar que tendrá, casilla en la que empezará y
     * ArrayList de
     * avatares creados (usado para dos propósitos: evitar que dos jugadores tengan
     * el mismo nombre y
     * que dos avatares tengan mismo ID). Desde este constructor también se crea el
     * avatar.
     */
    public Jugador(String nombre, String tipoAvatar, Casilla inicio, ArrayList<Avatar> avCreados) {
        this.nombre = nombre;
        this.avatar = new Avatar(tipoAvatar, this, inicio, avCreados);
        this.propiedades = new ArrayList<>();

    }

    // Otros métodos:
    // Método para añadir una propiedad al jugador. Como parámetro, la casilla a
    // añadir.
    public void anhadirPropiedad(Casilla casilla) {
        if (!propiedades.contains(casilla))
            this.propiedades.add(casilla);
    }

    // Método para eliminar una propiedad del arraylist de propiedades de jugador.
    public void eliminarPropiedad(Casilla casilla) {
        if (propiedades.contains(casilla))
            this.propiedades.remove(casilla);
    }

    // Método para añadir fortuna a un jugador
    // Como parámetro se pide el valor a añadir. Si hay que restar fortuna, se
    // pasaría un valor negativo.
    public void sumarFortuna(float valor) {
        this.fortuna += valor;
    }

    public float getFortuna() {
        return fortuna;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public String getNombre() {
        return nombre;
    }

    public float getGastos() {
        return gastos;
    }

    public ArrayList<Casilla> getPropiedades() {
        return propiedades;
    }

    // Método para sumar gastos a un jugador.
    // Parámetro: valor a añadir a los gastos del jugador (será el precio de un
    // solar, impuestos pagados...).
    public void sumarGastos(float valor) {
        this.gastos += valor;
        this.sumarFortuna(-valor);
    }

    public void setEnCarcel(boolean enCarcel) {
        this.enCarcel = enCarcel;
    }

    public boolean getEnCarcel() {
        return enCarcel;
    }

    public float getBote() {
        return bote;
    }

    public void añadirAlBote(float cantidad) {
        this.bote += cantidad;
    }

    public void restarDelBote(float cantidad) {
        this.bote -= cantidad;
    }

    public void sumarGastosAlq(float cantidad) {
        this.dineroInvertido += cantidad;
    }

    // Método al dar una vuelta completa al tablero, cobrando la cantidad
    // correspondiente.
    public void sumarVuelta() {
        fortuna += Valor.SUMA_VUELTA;
        vueltas++;
    }

    // Se sale de la cárcel y se resetea el número de tiradas en la cárcel
    public void salirCarcel() {
        tiradasCarcel = 0;
        this.enCarcel = false;
    }

    public int getTiradasCarcel() {
        return tiradasCarcel;
    }

    public void sumarTiradaCarcel() {
        tiradasCarcel++;
    }

    public int getNumTrans() { // número de casillas de transporte que posee
        int j = 0;
        for (int i = 0; i < propiedades.size(); i++) {
            if (propiedades.get(i).getTipo().equals("transporte"))
                j++;
        }
        return j;
    }

    public int getNumServ() { // número de casillas de servicio que posee
        int j = 0;
        for (int i = 0; i < propiedades.size(); i++) {
            if (propiedades.get(i).getTipo().equals("servicio"))
                j++;
        }
        return j;
    }

    // Se cobra el bote, el cual se vacía. La banca gestiona el bote.
    public void cobrarBote(Jugador banca) {
        float bote = banca.getBote();
        sumarFortuna(bote);
        banca.restarDelBote(bote);
        System.out.println("El jugador " + getNombre() + " recibe " + bote + "€ del bote.");
    }

    // Pagar un alquiler a otro jugador (dueño). Devuelve True si se puede pagar la
    // deuda.
    public boolean pagar(float coste, Jugador duenho) {
        if (coste > getFortuna()) {
            System.out.println("No tienes suficiente dinero. (" + coste + "$)");
            return false;
        }
        duenho.sumarFortuna(coste);
        sumarGastos(coste);
        System.out.println(getNombre() + " ha pagado " + coste + "€ de alquiler a " + duenho.getNombre() + ".");
        return true;
    }

    // Pagar un gasto como un impuesto sin que lo reciba otro jugador.
    public boolean pagar(float coste) {
        if (coste > getFortuna()) {
            System.out.println("No tienes suficiente dinero.");
            return false;
        }
        sumarGastos(coste);
        System.out.println(getNombre() + " ha pagado " + coste + "€ en impuestos.");
        return true;
    }

    // Devuelve true si el jugador ha pasado ya 3 turnos en la cárcel y debe pagar
    // la multa
    public boolean limiteCarcel() {
        return (enCarcel && (tiradasCarcel > 2));
    }

    // Método para salir manualmente de la cárcel pagando la multa. Devuelve True si
    // puede pagarla, y libera al jugador.
    public boolean pagarMulta() {
        float multa = 0.25f * Valor.SUMA_VUELTA;
        if (fortuna > multa) {
            sumarGastos(multa);
            System.out.println("Pagas la multa y sales de la cárcel.");
            salirCarcel();
            return true;
        }
        System.out.println("No tienes los fondos necesarios.");
        return false;

    }

    public int getVueltas() {
        return vueltas;
    }
    /*
     * public void anhadirEdificio(Edificio e){
     * this.edificios.add(e);
     * }
     * 
     * public ArrayList<Edificio> getEdificios() {
     * return edificios;
     * }
     */
}