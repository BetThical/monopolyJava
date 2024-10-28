package monopoly;

import java.util.ArrayList;
import java.util.HashMap;
import partida.*;

public class Casilla {

    // Atributos:
    private String nombre; // Nombre de la casilla
    private String tipo; // Tipo de casilla (Solar, Especial, Transporte, Servicios, Comunidad, Sorte,
                         // Impostos).
    private float valor; // Valor de esa casilla (en la mayoría será valor de compra, en la casilla
                         // parking se usará como el bote).
    private int posicion; // Posición que ocupa la casilla en el tablero (entero entre 1 y 40).
    private Jugador duenho; // Dueño de la casilla (por defecto sería la banca).
    private Grupo grupo; // Grupo al que pertenece la casilla (si es solar).
    private float impuesto; // Cantidad a pagar por caer en la casilla: el alquiler en
                            // solares/servicios/transportes o impuestos.
    private float hipoteca; // Valor otorgado por hipotecar una casilla
    private ArrayList<Avatar> avatares; // Avatares que están situados en la casilla.
    private ArrayList<Edificio> edificios;
    private boolean hipotecada = false; // Indica si la casilla está actualmente hipotecada.

    // Constructores:
    public Casilla() {
    }// Parámetros vacíos

    /*
     * Constructor para casillas tipo Solar, Servicios o Transporte:
     * Parámetros: nombre casilla, tipo (debe ser solar, serv. o transporte),
     * posición en el tablero, valor y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, float valor, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.valor = valor;
        this.hipoteca = valor/2f;
        switch (tipo) {
            case "solar":
                this.impuesto = ((float) (valor * 0.1)); // solar: alquiler (base) = valor / 10
                break;
            case "transporte":
                this.impuesto = Valor.SUMA_VUELTA; // valor transporte base = cantidad al dar una vuelta
                break;
            default:
                // servicio
                this.impuesto = (Valor.SUMA_VUELTA / 200); // factor servicio = suma vuelta / 200
                break;
        }
        this.duenho = duenho;
        this.avatares = new ArrayList<>();
        this.edificios = new ArrayList<>();

    }

    /*
     * Constructor utilizado para inicializar las casillas de tipo IMPUESTOS.
     * Parámetros: nombre, posición en el tablero, impuesto establecido y dueño.
     */
    public Casilla(String nombre, int posicion, float impuesto, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = "imposto";
        this.posicion = posicion;
        this.impuesto = impuesto;
        this.duenho = duenho;
        this.avatares = new ArrayList<>();
        this.edificios = new ArrayList<>();

    }

    /*
     * Constructor utilizado para crear las otras casillas (Suerte, Caja de
     * comunidad y Especiales):
     * Parámetros: nombre, tipo de la casilla (será uno de los que queda), posición
     * en el tablero y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.duenho = duenho;
        this.valor = 0; // para el parking
        this.avatares = new ArrayList<>();
        this.edificios = new ArrayList<>();

    }

    // Método utilizado para añadir un avatar al array de avatares en casilla.
    public void anhadirAvatar(Avatar av) {
        if (!avatares.contains(av))
            avatares.add(av);
        else {
            System.out.println("error: añadido avatar que ya estaba en casilla");
        }
    }

    // Método utilizado para eliminar un avatar del array de avatares en casilla.
    public void eliminarAvatar(Avatar av) {
        if (avatares.contains(av))
            avatares.remove(av);
        else {
            System.out.println("error: eliminado avatar que no estaba en casilla");
        }
    }

    /*
     * Método para luar qué hacer en una casilla concreta. Parámetros:
     * - Jugador cuyo avatar está en esa casilla.
     * - La banca (para ciertas comprobaciones).
     * - El valor de la tirada: para determinar impuesto a pagar en casillas de
     * servicios.
     * Valor devuelto: true en caso de ser solvente (es decir, de cumplir las
     * deudas), y false
     * en caso de no cumplirlas.
     */
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (esComprable(actual, banca)) {
            System.out.println(
                    "El jugador " + actual.getNombre() + " puede comprar esta casilla, por " + getValor() + " euros.");
        }

        if (getduenhoJugador() != banca && getduenhoJugador() != actual) {// Para solares, servicio o transporte con
                                                                         // dueño
            if (hipotecada)
                {
                    System.out.println("El dueño es " + duenho.getNombre() + ", pero la propiedad está hipotecada.");
                    return true;
                }
                return actual.pagar(calcular_coste(tirada), duenho);}

        if (getNombre().equals("Parking")) { // Parking
            actual.cobrarBote(banca);
            return true;
        }

        if ("imposto".equals(getTipo())) { // Casillas impuesto
            banca.añadirAlBote(impuesto);
            return actual.pagar(impuesto);
        }

        if (getNombre().equals("IrCarcel")) // Casilla 30 (ir a carcel)
            System.out.println("El jugador " + actual.getNombre() + " va a la cárcel.");

        return true;

    }

    // Calcula la cantidad a pagarle a otro jugador si se cae en una casilla de su
    // propiedad.
    // Funciona para solares, transporte y servicios.
    // Parámetro: tirada, para las casillas de servicio
    private float calcular_coste(int tirada) {
        float coste;
        switch (tipo) {
            case "solar": {
                coste = getImpuesto();
                if (getGrupo().esDuenhoGrupo(duenho))
                    coste *= 2;
                coste += alquilerEdificios();
            }
            case "transporte":
                coste = (getImpuesto() * (0.25f * duenho.getNumTrans()));
            default: {
                // servicio
                if (duenho.getNumServ() == 1)
                    coste = (getImpuesto() * 4 * tirada);
                else
                    coste = (getImpuesto() * 10 * tirada);
            }
        }
        return coste;
    }

    // Devuelve True si el jugador comprador puede comprar la casilla.
    // Requisitos: el comprador tiene suficiente dinero y la casilla es un solar,
    // transporte o servicio sin dueño.
    public boolean esComprable(Jugador comprador, Jugador banca) {
        return (("solar".equals(tipo) || "transporte".equals(tipo) || "servicio".equals(tipo)) && (duenho == banca)
                && (valor <= comprador.getFortuna()));
    }

    /*
     * Método usado para comprar una casilla determinada. Parámetros:
     * - Jugador que solicita la compra de la casilla.
     * - Banca del monopoly (es el dueño de las casillas no compradas aún).
     */
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        banca.sumarFortuna(valor);
        solicitante.sumarGastos(valor);
        this.duenho = solicitante;
        solicitante.anhadirPropiedad(this);

        if (getTipo().equals("solar"))
            if (getGrupo().esDuenhoGrupo(solicitante)) {
                System.out
                        .println("El jugador " + solicitante.getNombre() + " es dueño de todas las casillas del grupo "
                                + getGrupo().getColor() + "!");
            }
    }

    /*
     * Método para añadir valor a una casilla. Utilidad:
     * - Sumar valor a la casilla de parking.
     * - Sumar valor a las casillas de solar al no comprarlas tras cuatro vueltas de
     * todos los jugadores.
     * Este método toma como argumento la cantidad a añadir del valor de la casilla.
     */
    public void sumarValor(float suma) {
        this.valor += suma;
    }

    /*
     * Método para mostrar información sobre una casilla.
     * Devuelve una cadena con información específica de cada tipo de casilla.
     * Banca para poder imprimir el bote.
     */
    public String infoCasilla(Jugador banca) {

        StringBuilder output = new StringBuilder();

        output.append("- Tipo: ").append(getTipo()).append("\n");

        if (getTipo().equals("solar")) {
            output.append("- Grupo: ").append(getGrupo().getColor()).append("\n");
            output.append("- Valor: ").append(getValor()).append("\n");
            output.append("- Alquiler: ").append(getImpuesto()).append("\n");
            if (hipotecada) output.append("[Hipotecada]\n");
        } 
        else if (getTipo().equals("transporte")){
            output.append("- Valor: ").append(getValor()).append("\n");
            output.append("- Alquiler: ").append(getImpuesto()).append("\n");
        }
        else if (getTipo().equals("imposto")) {
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
        if (tipo.equals("solar")) {
            return "Nombre: " + nombre + ", tipo: " + tipo + ", grupo: " + grupo.getColor() + ", valor: " + valor
                    + "€.\n";
        }
        return "Nombre: " + nombre + ", tipo: " + tipo + ", valor: " + valor + "€.\n";
    }

    public float valorEdificio(String tipo) {
        switch (tipo) {
            case "casa":
            case "hotel":
                return impuesto * 6f;
            case "piscina":
                return impuesto * 4f;
            default:
                return impuesto * 12.5f;
        }// nota: impuesto = valorinicial * 0.1
         // pista de deporte
         // 125% del valor inicial del solar
    }

    public boolean puedeConstruir(Edificio e, Jugador constructor) {
        // condiciones:
        // el jugador es dueño de la casilla
        // el jugador es dueño de todo el grupo O ha caído más de dos veces en la
        // casilla
        // si es un hotel, se deben haber construido 4 casas. el hotel elimina las
        // casas.
        // si es una piscina, se deben haber construido al menos un hotel y dos casas
        // si es una pista, se deben haber construido al menos dos hoteles

        // el máximo número de edificios que se pueden construir en un grupo es
        // 3 hoteles, 3 casas, 3 piscinas y 3 pistas, o 2 de cada si el grupo es de 2
        // casillas.

        if (!tipo.equals("solar")) {
            System.out.println("No se puede construir en esta casilla.");
            return false;
        }

        if (!constructor.equals(duenho)) {
            System.out.println("No eres dueño de esta casilla.");
            return false;
        }

        if (valorEdificio(e.getTipo()) > constructor.getFortuna()) {
            System.out.println("Careces de los fondos necesarios.");
            return false;
        }

        if (!grupo.esDuenhoGrupo(constructor) && constructor.getAvatar().getVecesCaidasEnCasilla(posicion - 1) <= 2) {
            System.out
                    .println("Debes ser dueño de todo el grupo o haber caído en esta casilla más de dos veces. Actual: "
                            + constructor.getAvatar().getVecesCaidasEnCasilla(posicion - 1));
            return false;
        }
        HashMap<String, Integer> edificiosGrupo = grupo.contarEdificiosPorTipo();
        HashMap<String, Integer> edificiosCasilla = contarEdificiosPorTipo();
        int maxEdificiosPorTipo = grupo.getNumCasillas();
        if (edificiosCasilla.getOrDefault("casa", 0) == 4 && e.getTipo().equals("casa")) {
            System.out.println("Se pueden construir un máximo de 4 casas en un solar.");
            return false;
        }

        if (edificiosCasilla.getOrDefault("casa", 0) < 4 && e.getTipo().equals("hotel")) {
            System.out.println("Para construir un hotel se deben construir antes 4 casas.");
            return false;
        }

        if (edificiosGrupo.getOrDefault("hotel", 0) == maxEdificiosPorTipo) {
            if (e.getTipo().equals("hotel")) {
                System.out.println(
                        "Se pueden construir un máximo de " + maxEdificiosPorTipo + " hoteles en este grupo.");
                return false;
            }
            if (e.getTipo().equals("casa") && edificiosGrupo.getOrDefault("casa", 0) == maxEdificiosPorTipo) {
                System.out.println("Se pueden construir un máximo de " + grupo.getNumCasillas()
                        + " casas y hoteles en este grupo.");
                return false;
            }
        }

        if (e.getTipo().equals("piscina")) {
            if (edificiosGrupo.getOrDefault("piscina", 0) == maxEdificiosPorTipo) {
                System.out.println(
                        "Se pueden construir un máximo de " + maxEdificiosPorTipo + " piscinas en este grupo.");
                return false;
            }
            if (edificiosCasilla.getOrDefault("casa", 0) < 2 || edificiosCasilla.getOrDefault("hotel", 0) < 1) {
                System.out.println(
                        "Para construir una piscina, se deben construir antes al menos 2 casas y 1 hotel.");
                return false;
            }

        }

        if (e.getTipo().equals("pista")) {
            if (edificiosGrupo.getOrDefault("pista", 0) == grupo.getNumCasillas()) {
                System.out.println(
                        "Se pueden construir un máximo de " + grupo.getNumCasillas()
                                + " pistas de deporte en este grupo.");
                return false;
            }
            if (edificiosCasilla.getOrDefault("hotel", 0) < 2) {
                System.out.println(
                        "Para construir una pista de deporte, se deben construir antes al menos 2 hoteles.");
                return false;
            }
        }
        // System.out.println("Se puede construir el edificio " + e.getTipo() + " en
        // esta casilla.");
        return true;
    }

    public void anhadirEdificio(Edificio e) {
        int numCasas = contarEdificiosPorTipo().getOrDefault("casa", 0);
        float alquilerEdificio = 0;
        switch (e.getTipo().toLowerCase()) {
            case "casa": {
                switch (numCasas) {
                    case 0:
                        alquilerEdificio = 5 * impuesto;
                    case 1:
                        alquilerEdificio = 10 * impuesto; // 2 casas: 15 veces o alquiler
                    case 2:
                        alquilerEdificio = 20 * impuesto; // 3 casas: 35 veces o alquiler
                    case 3:
                        alquilerEdificio = 15 * impuesto; // 4 casas: 50 veces o alquiler
                    default: {
                    }
                }
            }
            case "hotel":
                alquilerEdificio = 70 * impuesto;
            case "piscina":
            case "pista": {
                alquilerEdificio = 25 * impuesto;
                break;
            }
        }
        e.setAlquiler(alquilerEdificio);
        this.edificios.add(e);
        // duenho.anhadirEdificio(e);
        duenho.sumarGastos(valorEdificio(e.getTipo()));
        if (e.getTipo().equals("hotel")) {
            destruirEdificio("casa");
            destruirEdificio("casa");
            destruirEdificio("casa");
            destruirEdificio("casa");
            System.out.println("Las 4 casas han sido eliminadas.");

        }
    }

    public boolean destruirEdificio(String tipo) {
        for (int i = edificios.size() - 1; i >= 0; i--) {
            if (edificios.get(i).getTipo().equalsIgnoreCase(tipo)) {
                edificios.remove(i);
                return true;
            }
        }
        System.out.println("No se encontró un edificio de tipo " + tipo + " en la casilla.");
        return false;
    }

    public HashMap<String, Integer> contarEdificiosPorTipo() {
        HashMap<String, Integer> contador = new HashMap<>();

        for (Edificio edificio : edificios) {
            String tipo = edificio.getTipo();

            // Incrementar el contador para el tipo de edificio
            contador.put(tipo, contador.getOrDefault(tipo, 0) + 1);
        }

        return contador;
    }

    // GETTERS
    public String getNombre() {
        return nombre;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public float getValor() {
        return valor;
    }

    public float getImpuesto() {
        return impuesto;
    }

    public float alquilerEdificios() {
        float alquilerTotal = 0;
        for (Edificio e : edificios) {
            // System.out.println("Edificio: "+e.getTipo()+" Valor: "+e.getAlquiler());
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

    public boolean puedeHipotecar(Jugador j) {
        if (!j.equals(duenho)) {
            System.out.println("No eres dueño de " + nombre + ".");
            return false;
        }
        if (hipotecada) {
            System.out.println("Ya está hipotecada.");
            return false;
        }
        return true;

    }

    public boolean puedeDeshipotecar(Jugador j) {
        if (!j.equals(duenho)) {
            System.out.println("No eres dueño de " + nombre + ".");
            return false;
        }
        if (!hipotecada) {
            System.out.println("No está hipotecada.");
            return false;
        }

        if (hipoteca*1.1f > j.getFortuna()) {
            System.out.println("No tienes fondos para deshipotecarla.");
            return false;
        }
        return true;

    }

    public void hipotecar() {
        System.out.println("Se ha hipotecado " + nombre + ". " + duenho.getNombre() + " ha recibido " + hipoteca
                + "€ de la hipoteca.");
        setHipotecada(true);
        duenho.sumarFortuna(hipoteca);
    }

    public void deshipotecar() {
        System.out.println("Se ha deshipotecado " + nombre + ". " + duenho.getNombre() + " ha pagado " + (hipoteca*1.1f)
                + "€ de la hipoteca.");
        setHipotecada(false);
        duenho.sumarGastos(hipoteca);
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

    public void setValor(float v) {
        this.valor = v;
    }

    public void setDuenho(Jugador j) {
        this.duenho = j;
    }

    public void setHipotecada(boolean h) {
        this.hipotecada = h;
    }

}
