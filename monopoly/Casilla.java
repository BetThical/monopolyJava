package monopoly;

import exception.comandoInvalidoException.EdificioNoPermitidoException;
import exception.comandoInvalidoException.HipotecaException;
import exception.noEncontradoException.EdificioNoEncontradoException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import partida.*;

public abstract class Casilla{

    // Atributos:
    private String nombre; // Nombre de la casilla
    private String tipo; // Tipo de casilla (Solar, Especial, Transporte, Servicios, Comunidad, Sorte,
    // Impostos).
    private int posicion; // Posición que ocupa la casilla en el tablero (entero entre 1 y 40).
    private Jugador duenho; // Dueño de la casilla (por defecto sería la banca).
    private Grupo grupo; // Grupo al que pertenece la casilla (si es solar).
    private float impuesto; // Cantidad a pagar por caer en la casilla: el alquiler en
    // solares/servicios/transportes o impuestos.
    private float hipoteca; // Valor otorgado por hipotecar una casilla
    private ArrayList<Avatar> avatares; // Avatares que están situados en la casilla.
    private ArrayList<Edificio> edificios;
    private float rentable = 0; // Rentabilidad de las casillas
    private int visitas = 0;

    private boolean hipotecada = false; // Indica si la casilla está actualmente hipotecada.


    // Constructores:
    public Casilla() {
    }

    protected Casilla(String nombre, int posicion, Jugador duenho, String tipo){

        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.duenho = duenho;
        this.avatares = new ArrayList<>();
    }

    // Métodos:

    // Método utilizado para añadir un avatar al array de avatares en casilla.
    public void anhadirAvatar(Avatar av) {
        if (!avatares.contains(av)) {
            avatares.add(av);
        }
    }

    // Método utilizado para eliminar un avatar del array de avatares en casilla.
    public void eliminarAvatar(Avatar av) {
        if (avatares.contains(av)) {
            avatares.remove(av);
        }
    }

    /*
     * Método para evaluar qué hacer en una casilla concreta. Parámetros:
     * - Jugador cuyo avatar está en esa casilla.
     * - La banca (para ciertas comprobaciones).
     * - El valor de la tirada: para determinar impuesto a pagar en casillas de
     * servicios.
     * Valor devuelto: true en caso de ser solvente (es decir, de cumplir las
     * deudas), y false
     * en caso de no cumplirlas.
     */
    @SuppressWarnings("ConvertToStringSwitch")
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (this instanceof Propiedad){
            Propiedad propiedad = (Propiedad) this;
            if (propiedad.esComprable(actual, banca)
            && !(!actual.getPuedeComprar() && actual.getAvatar().getTipo().equals("coche")
            && actual.getMovEspecial())) {
                String respuesta = Juego.consola
                .leer("El jugador " + actual.getNombre() + " puede comprar esta casilla, por " + propiedad.getValor()
                        + " euros. Comprar? (Y/N)")
                .toUpperCase(); // Captura la respuesta del jugador (en mayúsculas)

                if (respuesta.equals("Y")) {
                    propiedad.comprarCasilla(actual, banca); // Llama al método para realizar la compra
                    Juego.consola.imprimir(
                            "El jugador " + actual.getNombre() + " ha comprado la casilla " + getNombre() + ".");
                } else if (respuesta.equals("N")) {
                    Juego.consola.imprimir("El jugador " + actual.getNombre() + " ha decidido no comprar la casilla.");
                } else {
                    Juego.consola.imprimir("Respuesta inválida. Por favor, introduce 'Y' o 'N'.");
                }
            }
        }

        if (getduenhoJugador() != banca && getduenhoJugador() != actual && (this instanceof Propiedad)) {// Para solares, servicio o transporte con
            // dueño
            Propiedad propiedad = (Propiedad) this;
            if (hipotecada) {
                Juego.consola.imprimir("El dueño es " + duenho.getNombre() + ", pero la propiedad está hipotecada.");
                return true;
            }
            sumarRentable(propiedad.calcular_coste(tirada));
            return actual.pagar(propiedad.calcular_coste(tirada), duenho, true);
        }

        if (getNombre().equals("Parking")) { // Parking
            actual.cobrarBote(banca);
            return true;
        }

        if ("imposto".equals(getTipo())) { // Casillas impuesto
            banca.añadirAlBote(impuesto);
            return actual.pagar(impuesto);
        }

        if (getNombre().equals("IrCarcel")) { // Casilla 30 (ir a carcel)
            Juego.consola.imprimir("El jugador " + actual.getNombre() + " va a la cárcel.");
            actual.setEnCarcel(true); // esto envía un aviso al menú, que encarcelará al jugador (sólo menú puede
            // encarcelar)
        }
        if (getTipo().equals("comunidad") || getTipo().equals("suerte")) {
            if (getTipo().equals("comunidad") && actual.puedeCogerCarta() == 0) {
                Juego.consola.imprimir("El jugador " + actual.getNombre()
                        + " ha caído en una casilla de comunidad. Puede sacar una carta.");
                actual.setCartaDisponible(1);
            }

            if (getTipo().equals("suerte") && actual.puedeCogerCarta() == 0) {
                {
                    Juego.consola.imprimir("El jugador " + actual.getNombre()
                            + " ha caído en una casilla de suerte. Puede sacar una carta.");
                    actual.setCartaDisponible(2);
                }
            }

            return true;
        }
        if (getNombre().equals("IrCarcel")) {

        }
        return true;

    }

    /*
     * Método para mostrar información sobre una casilla.
     * Devuelve una cadena con información específica de cada tipo de casilla.
     * Banca para poder imprimir el bote.
     */
    public String infoCasilla(Jugador banca) {

        StringBuilder output = new StringBuilder();

        output.append("- Tipo: ").append(getTipo()).append("\n");

        if (this instanceof Solar) {
            Propiedad propiedad = (Propiedad) this;
            output.append("- Dueño: ").append(getduenhoJugador().getNombre()).append("\n");
            output.append("- Grupo: ").append(getGrupo().getColor()).append("\n");
            output.append("- Valor: ").append(propiedad.getValor()).append("\n");
            output.append("- Alquiler: ").append(propiedad.calcular_coste(0)).append("\n");
            if (hipotecada) {
                output.append("[Hipotecada]\n");
            }
        } else if (this instanceof Transporte) {
            Propiedad propiedad = (Propiedad) this;
            output.append("- Dueño: ").append(getduenhoJugador().getNombre()).append("\n");
            output.append("- Valor: ").append(propiedad.getValor()).append("\n");
            output.append("- Alquiler: ").append(propiedad.calcular_coste(0)).append("\n");
        } else if (getTipo().equals("imposto")) {
            output.append("- Imposto: ").append(getImpuesto()).append("\n");
        } else if (getNombre().equals("parking")) {
            output.append("- Bote: ").append(banca.getBote()).append("\n");
        } else if (getNombre().equals("carcel")) {
            output.append("- Fianza: ").append(Valor.FORTUNA_INICIAL * 0.25).append("\n");
        }

        if (!getAvatares().isEmpty()) {
            output.append("- Jugadores:\n");
            for (int i = 0; i < getAvatares().size(); i++) {
                output.append("   · ").append(getAvatares().get(i).getJugador().getNombre()).append("\n");
            }
        }

        if (!edificios.isEmpty()) {
            output.append("- Edificios:\n");
            for (int i = 0; i < edificios.size(); i++) {
                output.append("   · ").append(edificios.get(i).getTipo());
            }
        }
        output.append("\n");

        return output.toString();
    }


    /*
     * Método para mostrar información de una casilla en venta.
     * Valor devuelto: texto con esa información.
     */
    public String casEnVenta() {
        if (this instanceof Propiedad){
            Propiedad propiedad = (Propiedad) this;
            if (this instanceof Solar) {
                return "Nombre: " + nombre + ", tipo: " + tipo + ", grupo: " + grupo.getColor() + ", valor: " + propiedad.getValor()
                        + "€.\n";
            }
            return "Nombre: " + nombre + ", tipo: " + tipo + ", valor: " + propiedad.getValor() + "€.\n";
        }
        return "Esta casilla no está en venta.\n";
    }


    // GETTERS
    public String getNombre() {
        return nombre;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public float getImpuesto() {
        return impuesto;
    }

    public float alquilerEdificios() {
        float alquilerTotal = 0;
        for (Edificio e : edificios) {
            alquilerTotal += e.getAlquiler(); // Sumar el alquiler de cada edificio
        }
        return alquilerTotal;
    }

    public Jugador getduenhoJugador() {
        return duenho;
    }

    public int getPosicion() {
        return posicion;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean puedeDeshipotecar(Jugador j) {
        return true;
    }

    public void hipotecar(Jugador jugador) throws HipotecaException {
        if (!jugador.equals(duenho)) {
            throw new HipotecaException("hipotecar", "no eres dueño de " + nombre + ".");
        }
        if (hipotecada) {
            throw new HipotecaException("hipotecar", "ya está hipotecada.");
        }
        setHipotecada(true);
        duenho.sumarFortuna(hipoteca);
    }

    public void deshipotecar(Jugador jugador) throws HipotecaException {
        if (!jugador.equals(duenho)) {
            throw new HipotecaException("deshipotecar", "no eres dueño de " + nombre + ".");

        }
        if (!hipotecada) {
            throw new HipotecaException("deshipotecar", nombre + " no está hipotecada.");

        }

        if (hipoteca * 1.1f > jugador.getFortuna()) {
            throw new HipotecaException("deshipotecar", "no tienes los fondos necesarios (" + hipoteca * 1.1f + "€).");

        }
        setHipotecada(false);
        duenho.sumarGastos(hipoteca * 1.1f);
    }

    public float getHipoteca() {
        return hipoteca;
    }

    public ArrayList<Avatar> getAvatares() {
        return avatares;
    }

    public ArrayList<Edificio> getEdificios() {
        return edificios;
    }

    public boolean getHipotecada() {
        return hipotecada;
    }
    // SETTERS

    public void setPosicion(int p) {
        this.posicion = p;
    }

    public void setGrupo(Grupo g) {
        this.grupo = g;
    }

    public void setDuenho(Jugador j) {
        this.duenho = j;
    }

    public void setHipotecada(boolean h) {
        this.hipotecada = h;
    }

    public float GetRentabilidad() {
        return rentable;
    }

    public void sumarRentable(float pago) {
        this.rentable += pago;
    }

    public int getVisitas() {
        return visitas;
    }

    public void addVisitas(int p) {
        this.visitas += p;
    }


}