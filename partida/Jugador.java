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
    private String color; // Usado para imprimir el color del jugador en la consola.
    private ArrayList<Trato> tratos; // Lista de tratos propuestos al jugador.
    private boolean movEspecial; // True si el jugador está en un movimiento especial.
    private Jugador enDeuda = null; // Jugador al que se le debe dinero.
    private int cartaDisponible = 0; // 1 - carta de suerte, 2 - carta de caja de comunidad, 0 - ninguna
    private float fortunaPrevia = 0; // Guarda la fortuna previa a una deuda para poder dársela al jugador cuando declare la bancarrota

    //Atributos de estadísticas:
    private float dineroInvertido = 0;
    private float pagoTasasEImpuestos = 0;
    private float pagoDeAlquileres = 0;
    private float cobroDeAlquileres = 0;
    private float pasarPorCasillaDeSalida = 0;
    private float premiosInversionesOBote = 0;
    private int vecesEnLaCarcel = 0;
    private int tiradas = 0;

    public void anhadirTrato(Trato t) {
        tratos.add(t);
    }

    public void eliminarTrato(Trato t) {
        tratos.remove(t);
    }

    public void setFortunaPrevia(float fortunaPrevia) {
        this.fortunaPrevia = fortunaPrevia;
    }

    public float getFortunaPrevia() {
        return fortunaPrevia;
    }

    public Trato getTrato(int id) {
        for (Trato t : tratos) {
            if (t.getIdTrato() == id) {
                return t;
            }
        }
        return null;
    }

    public ArrayList<Trato> getTratos() {
        return tratos;
    }

    public float getPremiosInversionesOBote() {
        return premiosInversionesOBote;
    }

    public void setPremiosInversionesOBote(float premiosInversionesOBote) {
        this.premiosInversionesOBote = premiosInversionesOBote;
    }

    public int getVecesEnLaCarcel() {
        return vecesEnLaCarcel;
    }

    public int puedeCogerCarta() {
        return cartaDisponible;
    }

    public String getColor() {
        return color;
    }

    public void setCartaDisponible(int cartaDisponible) {
        this.cartaDisponible = cartaDisponible;
    }

    public void setVecesEnLaCarcel(int vecesEnLaCarcel) {
        this.vecesEnLaCarcel = vecesEnLaCarcel;
    }

    public boolean getMovEspecial() {
        return movEspecial;
    }

    public void setFortuna(float fortuna) {
        this.fortuna = fortuna;
    }

    public void setMovEspecial(boolean movEspecial) {
        this.movEspecial = movEspecial;
    }

    public Jugador getEnDeuda() {
        return enDeuda;
    }

    public void setEnDeuda(Jugador enDeuda) {
        this.enDeuda = enDeuda;
    }

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
        switch (tipoAvatar) {
            case "pelota":
                this.avatar = new Pelota(this, inicio, avCreados);
                break;
            case "coche":
                this.avatar = new Coche(this, inicio, avCreados);
                break;
            default:
                this.avatar = null;
                break;
        }
        this.propiedades = new ArrayList<>();
        this.tratos = new ArrayList<>();
        switch (avCreados.size() + 1) {
            case 1:
                this.color = Valor.RED;
                break;
            case 2:
                this.color = Valor.BLUE;
                break;
            case 3:
                this.color = Valor.YELLOW;
                break;
            case 4:
                this.color = Valor.GREEN;
                break;
            case 5:
                this.color = Valor.PURPLE;
                break;
            case 6:
                this.color = Valor.CYAN;
                break;
            default:
                this.color = Valor.WHITE;
                break;
        }

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
        if (!propiedades.contains(casilla)) {
            this.propiedades.add(casilla);
        }
    }

    // Método para eliminar una propiedad del arraylist de propiedades de jugador.
    public void eliminarPropiedad(Casilla casilla) {
        if (propiedades.contains(casilla)) {
            this.propiedades.remove(casilla);
        }
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
            String colorFortuna = diferencia > 0 ? Valor.GREEN : Valor.RED;
            Juego.consola
                    .imprimir(colorFortuna + "[variación de fortuna de " + this.getNombre() + ": " + fortunaInicial
                            + " a "
                            + this.getFortuna() + ". (diferencia: " + signo + diferencia + ")]" + Valor.RESET);
        }
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
    public void sumarVuelta(boolean cobrar) {
        if (cobrar) {
            sumarFortuna(Valor.SUMA_VUELTA);
            sumarCobreSal(Valor.SUMA_VUELTA);
        }
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

    public void setTiradasCarcel(int tiradasCarcel) {
        this.tiradasCarcel = tiradasCarcel;
    }

    public void sumarTiradaCarcel() {
        tiradasCarcel++;
    }

    public int getNumTrans() { // número de casillas de transporte que posee
        try {
            int j = 0;
            for (int i = 0; i < propiedades.size(); i++) {
                if (propiedades.get(i).getTipo().equals("transporte")) {
                    j++;
                }
            }
            return j;
        } catch (NullPointerException e) { // si el dueño es la banca (no tiene propiedades, se calculará el precio
            // teniendo 1 sola casilla)
            return 1;
        }
    }

    public int getNumServ() { // número de casillas de servicio que posee
        int j = 0;
        for (int i = 0; i < propiedades.size(); i++) {
            if (propiedades.get(i).getTipo().equals("servicio")) {
                j++;
            }
        }
        return j;
    }

    // Se cobra el bote, el cual se vacía. La banca gestiona el bote.
    public void cobrarBote(Jugador banca) {
        float valorBote = banca.getBote();
        sumarFortuna(valorBote);
        sumarGastosBote(valorBote);
        banca.restarDelBote(valorBote);
        Juego.consola.imprimir("El jugador " + getNombre() + " recibe " + valorBote + "€ del bote.");
    }

    // Pagar un alquiler a otro jugador (dueño). Devuelve True si se puede pagar la
    // deuda.
    public boolean pagar(float coste, Jugador duenho, boolean alquiler) {

        sumarGastos(coste);
        if (getFortuna() < 0) {
            setFortunaPrevia(coste + getFortuna());
            Juego.consola.imprimir("No tienes suficiente dinero. Quedas en deuda con " + duenho.getNombre() + ".");
            enDeuda = duenho;
            return false;
        }
        duenho.sumarFortuna(coste);
        if (alquiler) {
            sumarGastosAlq(coste);
            duenho.sumarCobreAlq(coste);
            Juego.consola.imprimir(getNombre() + " ha pagado " + coste + "€ de alquiler a " + duenho.getNombre() + ".");
        } else {
            Juego.consola.imprimir(getNombre() + " ha pagado " + coste + "€ a " + duenho.getNombre() + ".");

        }
        return true;
    }

    public void darCasilla(Casilla casilla, Jugador recibidor) {
        recibidor.anhadirPropiedad(casilla);
        casilla.setDuenho(recibidor);
        eliminarPropiedad(casilla);
    }

    // Pagar un gasto como un impuesto sin que lo reciba otro jugador.
    public boolean pagar(float coste) {

        sumarGastos(coste);
        if (getFortuna() < 0) {
            fortunaPrevia = (coste + getFortuna());
            Juego.consola.imprimir("No tienes suficiente dinero. Quedas en deuda con la banca.");
            enDeuda = null;
            return false;
        }
        sumarGastosImp(coste);
        Juego.consola.imprimir(getNombre() + " ha pagado " + coste + "€ en impuestos.");
        return true;
    }

    // Devuelve true si el jugador ha pasado ya 3 turnos en la cárcel y debe pagar
    // la multa
    public boolean limiteCarcel() {
        return (enCarcel && (tiradasCarcel > 2));
    }

    // Devuelve true si el jugador tiene los fondos necesarios para pagar la multa,
    // y si los tiene, la paga.
    public boolean pagarMulta() {
        float multa = 0.25f * Valor.SUMA_VUELTA;
        if (fortuna > multa) {
            sumarGastos(multa);
            sumarGastosImp(multa);
            return true;
        }

        return false;

    }

    public int getVueltas() {
        return vueltas;
    }

    /*
     * Método que realiza el proceso completo de encarcelar a un jugador:
     * - Lo fija como encarcelado.
     * - Aumenta el contador de veces en la cárcel, y resetea su numero de tiradas
     * en la carcel.
     * - Lo mueve a la casilla de la cárcel.
     * - Si el avatar es una pelota, se finaliza prematuramente su movimiento.
     * Requiere el ArrayList de casillas para mover al jugador a la cárcel.
     */
    public void encarcelar(ArrayList<ArrayList<Casilla>> casillas) {
        setEnCarcel(true);
        setTiradasCarcel(0);
        avatar.setLugar(casillas, 10);
        vecesEnLaCarcel += 1;
        if ((avatar instanceof Pelota) && ((Pelota) avatar).siguienteMovPelota(false) != 0) {
            ((Pelota) avatar).resetMovPelota();
        }
    }

    public void estadisticas() {
        Juego.consola.imprimir("Estadísticas del Jugador:");
        Juego.consola.imprimir("Dinero invertido: " + dineroInvertido);
        Juego.consola.imprimir("Pago de tasas e impuestos: " + pagoTasasEImpuestos);
        Juego.consola.imprimir("Pago de alquileres: " + pagoDeAlquileres);
        Juego.consola.imprimir("Cobro de alquileres: " + cobroDeAlquileres);
        Juego.consola.imprimir("Pasar por casilla de salida: " + pasarPorCasillaDeSalida);
        Juego.consola.imprimir("Premios por inversiones o bote: " + premiosInversionesOBote);
        Juego.consola.imprimir("Veces en la cárcel: " + vecesEnLaCarcel);
    }

    public int getTiradas() {
        return tiradas;
    }

    public void addTiradas(int p) {
        this.tiradas += p;
    }

    public float getEnCabeza() {
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
