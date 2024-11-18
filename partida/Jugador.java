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
    private float premiosInversionesOBote = 0;
    private int vecesEnLaCarcel = 0;
    public boolean movEspecial;

    public int tiradas = 0;

    public Jugador enDeuda = null;

    public float fortunaPrevia; // fortuna que ten o xogador cando antes de ter unha deuda que non pode pagar,
                                // é o que recibirá o xogador ao que lle debe cando declare a bancarrota se a
                                // declara
    private int cocheCalado;

    private boolean puedeComprar = true;

    // Constructor vacío. Se usará para crear la banca.
    public Jugador() {
        this.nombre = "la banca";
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

    public void setPuedeComprar(boolean v) {
        puedeComprar = v;
    }

    public boolean getPuedeComprar() {
        return puedeComprar;
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
        float fortunaInicial = this.getFortuna();
        this.fortuna += valor;
        if (fortunaInicial != this.getFortuna() && !"la banca".equals(this.nombre)) {
            float diferencia = this.getFortuna() - fortunaInicial;
            String signo = diferencia > 0 ? "+" : "";
            String color = diferencia > 0 ? Valor.GREEN : Valor.RED;
            System.out.println(color + "[variación de fortuna de " + this.getNombre() + ": " + fortunaInicial + " a " + this.getFortuna() + ". (diferencia: " + signo + diferencia + ")]" + Valor.RESET);
        }
    }

    public void calarCoche() {
        cocheCalado = 3;
    }

    public void reducirCocheCalado() {
        cocheCalado--;
    }

    public int getCocheCalado() {
        return cocheCalado;
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
        vecesEnLaCarcel += 1;
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

    public void sumarGastosProp(float cantidad) {
        this.dineroInvertido += cantidad;
    }

    public void sumarGastosImp(float cantidad) {
        this.pagoTasasEImpuestos += cantidad;
    }

    public void sumarGastosAlq(float cantidad) {
        this.pagoDeAlquileres += cantidad;
    }

    public void sumarCobreAlq(float cantidad) {
        this.cobroDeAlquileres += cantidad;
    }

    public void sumarCobreSal(float cantidad) {
        this.pasarPorCasillaDeSalida += cantidad;
    }

    public void sumarGastosBote(float cantidad) {
        this.premiosInversionesOBote += cantidad;
    }

    // Método al dar una vuelta completa al tablero, cobrando la cantidad
    // correspondiente.
    public void sumarVuelta() {
        sumarFortuna(Valor.SUMA_VUELTA);
        sumarCobreSal(Valor.SUMA_VUELTA);
        vueltas++;
    }

    public void pagarVuelta() {
        sumarGastos(Valor.SUMA_VUELTA);
        vueltas--;
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
        sumarGastosBote(bote);
        banca.restarDelBote(bote);
        System.out.println("El jugador " + getNombre() + " recibe " + bote + "€ del bote.");
    }

    // Pagar un alquiler a otro jugador (dueño). Devuelve True si se puede pagar la
    // deuda.
    public boolean pagar(float coste, Jugador duenho) {
        
        sumarGastos(coste);
                     if (getFortuna() < 0) {
            fortunaPrevia = (coste + getFortuna());
            System.out.println("No tienes suficiente dinero. Quedas en deuda con " + duenho.getNombre() + ".");
            enDeuda = duenho;
            return false;
        }
        duenho.sumarFortuna(coste);
        sumarGastosAlq(coste);
        duenho.sumarCobreAlq(coste);
        System.out.println(getNombre() + " ha pagado " + coste + "€ de alquiler a " + duenho.getNombre() + ".");
        return true;
    }

    // Pagar un gasto como un impuesto sin que lo reciba otro jugador.
    public boolean pagar(float coste) {

        sumarGastos(coste);
        if (getFortuna() < 0) {
            fortunaPrevia = (coste + getFortuna());
            System.out.println("No tienes suficiente dinero. Quedas en deuda con la banca.");
            enDeuda = null;
            return false;
        }
        sumarGastosImp(coste);
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
        sumarGastos(multa);
        if (fortuna > 0) {
            sumarGastosImp(multa);
            System.out.println("Pagas la multa y sales de la cárcel.");
            salirCarcel();
            return true;
        }
        System.out.println("No tienes los fondos necesarios para pagar la multa. Quedas en deuda con la banca");
        return false;

    }

    public int getVueltas() {
        return vueltas;
    }

    public void estadisticas() {
        System.out.println("Estadísticas del Jugador:");
        System.out.println("Dinero invertido: " + dineroInvertido);
        System.out.println("Pago de tasas e impuestos: " + pagoTasasEImpuestos);
        System.out.println("Pago de alquileres: " + pagoDeAlquileres);
        System.out.println("Cobro de alquileres: " + cobroDeAlquileres);
        System.out.println("Pasar por casilla de salida: " + pasarPorCasillaDeSalida);
        System.out.println("Premios por inversiones o bote: " + premiosInversionesOBote);
        System.out.println("Veces en la cárcel: " + vecesEnLaCarcel);
    }

    public int getTiradas(){
        return tiradas;
    }

    public void setTiradas(int p){
        this.tiradas += p;
    }

    public float getEnCabeza(){
        return (fortuna + dineroInvertido);
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
