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
    private float rentable = 0; // Rentabilidad de las casillas
    private int visitas = 0;

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
        this.hipoteca = valor / 2f;
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

    // Pepito

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
    @SuppressWarnings("ConvertToStringSwitch")
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (esComprable(actual, banca)
                && !(!actual.getPuedeComprar() && actual.getAvatar().getTipo().equals("coche")
                        && actual.getMovEspecial())) {
            String respuesta = Juego.consola
                    .leer("El jugador " + actual.getNombre() + " puede comprar esta casilla, por " + getValor()
                            + " euros. Comprar? (Y/N)")
                    .toUpperCase(); // Captura la respuesta del jugador (en mayúsculas)

            if (respuesta.equals("Y")) {
                comprarCasilla(actual, banca); // Llama al método para realizar la compra
                Juego.consola.imprimir(
                        "El jugador " + actual.getNombre() + " ha comprado la casilla " + getNombre() + ".");
            } else if (respuesta.equals("N")) {
                Juego.consola.imprimir("El jugador " + actual.getNombre() + " ha decidido no comprar la casilla.");
            } else {
                Juego.consola.imprimir("Respuesta inválida. Por favor, introduce 'Y' o 'N'."); //todo
            }
        }

        if (getduenhoJugador() != banca && getduenhoJugador() != actual) {// Para solares, servicio o transporte con
            // dueño
            if (hipotecada) {
                Juego.consola.imprimir("El dueño es " + duenho.getNombre() + ", pero la propiedad está hipotecada.");
                return true;
            }
            sumarRentable(calcular_coste(tirada));
            return actual.pagar(calcular_coste(tirada), duenho);
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
        return true;

    }

    // Calcula la cantidad a pagarle a otro jugador si se cae en una casilla de su
    // propiedad.
    // Funciona para solares, transporte y servicios.
    // Parámetro: tirada, para las casillas de servicio
    public float calcular_coste(int tirada) {
        float coste;
        switch (tipo) {
            case "solar": {
                coste = getImpuesto();
                if (getGrupo().esDuenhoGrupo(duenho)) {
                    coste *= 2;
                }
                coste += alquilerEdificios();
                return coste;
            }
            case "transporte": {
                coste = (getImpuesto() * (0.25f * duenho.getNumTrans()));
                // System.out.printf("impuesto:%f, numtrans:%d, total:%f", getImpuesto(),
                // duenho.getNumTrans(), coste);
                return coste;
            }

            default: {
                // servicio
                if (duenho.getNumServ() == 1) {
                    coste = (getImpuesto() * 4 * tirada);
                } else {
                    coste = (getImpuesto() * 10 * tirada);
                }
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
        solicitante.sumarGastosProp(valor);
        solicitante.sumarGastos(valor);

        this.duenho = solicitante;
        solicitante.anhadirPropiedad(this);

        if (getTipo().equals("solar")) {
            if (getGrupo().esDuenhoGrupo(solicitante)) {
                System.out
                        .println("El jugador " + solicitante.getNombre() + " es dueño de todas las casillas del grupo "
                                + getGrupo().getColor() + "!");
            }
        }
        if (solicitante.getPuedeComprar()) {
            solicitante.setPuedeComprar(false);
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
            output.append("- Dueño: ").append(getduenhoJugador().getNombre()).append("\n");
            output.append("- Grupo: ").append(getGrupo().getColor()).append("\n");
            output.append("- Valor: ").append(getValor()).append("\n");
            output.append("- Alquiler: ").append(calcular_coste(0)).append("\n");
            if (hipotecada) {
                output.append("[Hipotecada]\n");
            }
        } else if (getTipo().equals("transporte")) {
            output.append("- Dueño: ").append(getduenhoJugador().getNombre()).append("\n");
            output.append("- Valor: ").append(getValor()).append("\n");
            output.append("- Alquiler: ").append(calcular_coste(0)).append("\n");
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

    public boolean puedeConstruir(Edificio e, Jugador constructor) { //todo: cambiar a excepciones??
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
            Juego.consola.imprimir("No se puede construir en esta casilla.");
            return false;
        }

        if (!constructor.equals(duenho)) {
            Juego.consola.imprimir("No eres dueño de esta casilla.");
            return false;
        }

        if (valorEdificio(e.getTipo()) > constructor.getFortuna()) {
            Juego.consola.imprimir("Careces de los fondos necesarios.");
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
        if (edificiosCasilla.getOrDefault("casa", 0) >= 4 && e.getTipo().equals("casa")) {
            Juego.consola.imprimir("Se pueden construir un máximo de 4 casas en un solar.");
            return false;
        }

        if (edificiosCasilla.getOrDefault("casa", 0) < 4 && e.getTipo().equals("hotel")) {
            Juego.consola.imprimir("Para construir un hotel se deben construir antes 4 casas.");
            return false;
        }

        if (edificiosGrupo.getOrDefault("hotel", 0) >= maxEdificiosPorTipo) {
            if (e.getTipo().equals("hotel")) {
                Juego.consola.imprimir(
                        "Se pueden construir un máximo de " + maxEdificiosPorTipo + " hoteles en este grupo.");
                return false;
            }
        }

        if (edificiosGrupo.getOrDefault("hotel", 0) == (maxEdificiosPorTipo - 1) && e.getTipo().equals("hotel")
                && edificiosGrupo.getOrDefault("casa", 0) > maxEdificiosPorTipo) {
            Juego.consola
                    .imprimir("Construir este hotel haría que se superase el número máximo de casas en esta casilla.");
            return false;
        }

        if (e.getTipo().equals("casa") && edificiosGrupo.getOrDefault("casa", 0) >= maxEdificiosPorTipo
                && edificiosGrupo.getOrDefault("hotel", 0) >= maxEdificiosPorTipo) {
            Juego.consola.imprimir("Se pueden construir un máximo de " + grupo.getNumCasillas()
                    + " casas en este grupo.");
            return false;
        }

        if (e.getTipo().equals("hotel") && edificiosGrupo.getOrDefault("casa", 0) - 4 > maxEdificiosPorTipo
                && edificiosGrupo.getOrDefault("hotel", 0) + 1 >= maxEdificiosPorTipo) {
            Juego.consola.imprimir(
                    "No se puede construir un hotel porque se superaría el número máximo de casas permitido. (se permite superar este límite hasta construir los hoteles)");
            return false;
        }

        if (e.getTipo().equals("piscina")) {
            if (edificiosGrupo.getOrDefault("piscina", 0) >= maxEdificiosPorTipo) {
                Juego.consola.imprimir(
                        "Se pueden construir un máximo de " + maxEdificiosPorTipo + " piscinas en este grupo.");
                return false;
            }
            if (edificiosCasilla.getOrDefault("casa", 0) < 2 || edificiosCasilla.getOrDefault("hotel", 0) < 1) {
                Juego.consola.imprimir(
                        "Para construir una piscina, se deben construir antes al menos 2 casas y 1 hotel.");
                return false;
            }
        }

        if (e.getTipo().equals("pista")) {
            if (edificiosGrupo.getOrDefault("pista", 0) == grupo.getNumCasillas()) {
                Juego.consola.imprimir(
                        "Se pueden construir un máximo de " + grupo.getNumCasillas()
                                + " pistas de deporte en este grupo.");
                return false;
            }
            if (edificiosCasilla.getOrDefault("hotel", 0) < 2) {
                Juego.consola.imprimir(
                        "Para construir una pista de deporte, se deben construir antes al menos 2 hoteles.");
                return false;
            }
        }
        // Juego.consola.imprimir("Se puede construir el edificio " + e.getTipo() + " en
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
                        break;
                    case 1:
                        alquilerEdificio = 10 * impuesto; // 2 casas: 15 veces o alquiler
                        break;
                    case 2:
                        alquilerEdificio = 20 * impuesto; // 3 casas: 35 veces o alquiler
                        break;
                    case 3:
                        alquilerEdificio = 15 * impuesto; // 4 casas: 50 veces o alquiler
                        break;
                    default: {
                        alquilerEdificio = 0;
                        break;

                    }
                }
                break;
            }
            case "hotel":
                alquilerEdificio = 70 * impuesto;
                break;
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
        duenho.sumarGastosProp(valorEdificio(e.getTipo()));
        if (e.getTipo().equals("hotel")) {
            destruirEdificio("casa");
            destruirEdificio("casa");
            destruirEdificio("casa");
            destruirEdificio("casa");
            Juego.consola.imprimir("Las 4 casas han sido eliminadas.");

        }
    }

    public boolean destruirEdificio(String tipo) { //todo: mover a juego
        for (int i = edificios.size() - 1; i >= 0; i--) {
            if (edificios.get(i).getTipo().equalsIgnoreCase(tipo)) {
                edificios.remove(i);
                return true;
            }
        }
        Juego.consola.imprimir("No se encontró un edificio de tipo " + tipo + " en la casilla.");
        return false;
    }

    public HashMap<String, Integer> contarEdificiosPorTipo() {
        HashMap<String, Integer> contador = new HashMap<>();

        for (Edificio edificio : edificios) {
            String tipoCasilla = edificio.getTipo();

            // Incrementar el contador para el tipo de edificio
            contador.put(tipoCasilla, contador.getOrDefault(tipoCasilla, 0) + 1);
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
            // Juego.consola.imprimir("Edificio: "+e.getTipo()+" Valor: "+e.getAlquiler());
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

    //todo mover a juego?
    public boolean puedeHipotecar(Jugador j) {
        if (!j.equals(duenho)) {
            Juego.consola.imprimir("No eres dueño de " + nombre + ".");
            return false;
        }
        if (hipotecada) {
            Juego.consola.imprimir("Ya está hipotecada.");
            return false;
        }
        return true;

    }

    public boolean puedeDeshipotecar(Jugador j) {
        if (!j.equals(duenho)) {
            Juego.consola.imprimir("No eres dueño de " + nombre + ".");
            return false;
        }
        if (!hipotecada) {
            Juego.consola.imprimir("No está hipotecada.");
            return false;
        }

        if (hipoteca * 1.1f > j.getFortuna()) {
            Juego.consola.imprimir("No tienes fondos para deshipotecarla.");
            return false;
        }
        return true;

    }

    public void hipotecar() {
        Juego.consola.imprimir("Se ha hipotecado " + nombre + ". " + duenho.getNombre() + " ha recibido " + hipoteca
                + "€ de la hipoteca.");
        setHipotecada(true);
        duenho.sumarFortuna(hipoteca);
    }

    public void deshipotecar() {
        System.out
                .println("Se ha deshipotecado " + nombre + ". " + duenho.getNombre() + " ha pagado " + (hipoteca * 1.1f)
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
