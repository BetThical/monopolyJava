package monopoly;

import java.util.ArrayList;

import exception.comandoInvalidoException.EdificioNoPermitidoException;
import exception.noEncontradoException.EdificioNoEncontradoException;
import partida.*;

public class Solar extends Propiedad {

    // Atributos:
    private Grupo grupo;
    private ArrayList<Edificio> edificios;

    // Constructores:
    public Solar(String nombre, int posicion, Jugador duenho, float valor) {
        super(nombre, posicion, duenho, valor, valor / 2f, valor / 10);
        this.edificios = new ArrayList<>();
    }

    // Métodos:

    public int numCasas() {
        int num = 0;
        for (Edificio e : edificios) {
            if (e instanceof Casa) {
                num++;
            }
        }
        return num;
    }

    public int numHoteles() {
        int num = 0;
        for (Edificio e : edificios) {
            if (e instanceof Hotel) {
                num++;
            }
        }
        return num;
    }

    public int numPiscinas() {
        int num = 0;
        for (Edificio e : edificios) {
            if (e instanceof Piscina) {
                num++;
            }
        }
        return num;
    }

    public int numPistas() {
        int num = 0;
        for (Edificio e : edificios) {
            if (e instanceof Pista) {
                num++;
            }
        }
        return num;
    }
    
    @Override
    public String casEnVenta(){
        return "Nombre: " + getNombre() + ", tipo: solar" + ", grupo: " + grupo.getColor() + ", valor: " + getValor()
        + "€.\n";
    }

    // lanza excepciones si no se puede construir el edificio dado en la casilla
    public void puedeConstruir(Edificio e, Jugador constructor)
            throws EdificioNoPermitidoException {
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

        if (!constructor.equals(getDuenho())) {
            throw new EdificioNoPermitidoException("No eres dueño de esta casilla");
        }

        if (e.getValor() > constructor.getFortuna()) {
            throw new EdificioNoPermitidoException("No tienes fondos suficientes para construir este edificio");
        }

        if (!grupo.esDuenhoGrupo(constructor) && constructor.getAvatar().getVecesCaidasEnCasilla(getPosicion() - 1) <= 2) {
            throw new EdificioNoPermitidoException(
                    "Debes ser dueño de todo el grupo o haber caído en esta casilla más de dos veces. Actual: "
                            + constructor.getAvatar().getVecesCaidasEnCasilla(getPosicion() - 1));
        }
        int maxEdificiosPorTipo = grupo.getNumCasillas();
        if (numCasas() >= 4 && e instanceof Casa) {
            throw new EdificioNoPermitidoException("Se pueden construir un máximo de 4 casas en un solar");
        }

        if (numCasas() < 4 && e instanceof Hotel) {
            throw new EdificioNoPermitidoException("Para construir un hotel se deben construir antes 4 casas (actual: "
                    + numCasas() + ")");
        }

        if (grupo.numHoteles() >= maxEdificiosPorTipo) {
            if (e instanceof Hotel) {
                throw new EdificioNoPermitidoException(
                        "Se pueden construir un máximo de " + maxEdificiosPorTipo + " hoteles en este grupo");
            }
        }

        if (grupo.numHoteles() == (maxEdificiosPorTipo - 1) && (e instanceof Hotel)
                && grupo.numCasas() - 4 > maxEdificiosPorTipo) {
            throw new EdificioNoPermitidoException(
                    "Construir este hotel haría que se superase el número máximo de casas en este gripo");
        }

        if ((e instanceof Casa) && grupo.numCasas() >= maxEdificiosPorTipo
                && grupo.numHoteles() >= maxEdificiosPorTipo) {
            throw new EdificioNoPermitidoException("Se pueden construir un máximo de " + grupo.getNumCasillas()
                    + " casas en este grupo.");
        }

        if (e instanceof Piscina) {
            if (grupo.numPiscinas() >= maxEdificiosPorTipo) {
                throw new EdificioNoPermitidoException(
                        "Se pueden construir un máximo de " + maxEdificiosPorTipo + " piscinas en este grupo");
            }
            if (numCasas() < 2 || numHoteles() < 1) {
                throw new EdificioNoPermitidoException(
                        "Para construir una piscina, se deben construir antes al menos 2 casas y 1 hotel");
            }
        }

        if (e instanceof Pista) {
            if (grupo.numPistas() >= grupo.getNumCasillas()) {
                throw new EdificioNoPermitidoException(
                        "Se pueden construir un máximo de " + grupo.getNumCasillas()
                                + " pistas de deporte en este grupo.");
            }
            if (numHoteles() < 2) {
                throw new EdificioNoPermitidoException(
                        "Para construir una pista de deporte, se deben construir antes al menos 2 hoteles");
            }
        }
    }

    public void anhadirEdificio(Edificio e, Jugador jugador) throws EdificioNoEncontradoException {
        int numCasas = numCasas();
        float alquilerEdificio = 0;
        if (e instanceof Hotel) {
            for (int i = 0; i < 4; i++) {
                destruirEdificio("casa", jugador, false);
            }
        }
        switch (e.getTipoNombre().toLowerCase()) {
            case "casa": {
                switch (numCasas) {
                    case 0:
                        alquilerEdificio = 5 * getAlquiler();
                        break;
                    case 1:
                        alquilerEdificio = 10 * getAlquiler(); // 2 casas: 15 veces o alquiler
                        break;
                    case 2:
                        alquilerEdificio = 20 * getAlquiler(); // 3 casas: 35 veces o alquiler
                        break;
                    case 3:
                        alquilerEdificio = 15 * getAlquiler(); // 4 casas: 50 veces o alquiler
                        break;
                    default: {
                        alquilerEdificio = 0;
                        break;

                    }
                }
                break;
            }
            case "hotel":
                alquilerEdificio = 70 * getAlquiler();
                break;
            case "piscina":
            case "pista": {
                alquilerEdificio = 25 * getAlquiler();
                break;
            }
        }
        e.setAlquiler(alquilerEdificio);
        this.edificios.add(e);
        // duenho.anhadirEdificio(e);
        getDuenho().sumarGastos(e.getValor());
        getDuenho().sumarGastosProp(e.getValor());

    }

    public void destruirEdificio(String tipo, Jugador jugador, boolean cobrar) throws EdificioNoEncontradoException {
        for (int i = edificios.size() - 1; i >= 0; i--) {
            if (edificios.get(i).getTipoNombre().equalsIgnoreCase(tipo)) {
                Edificio e = edificios.get(i);
                edificios.remove(i);
                if (cobrar)
                    jugador.sumarFortuna(e.getValor() / 2f);

                return;
            }
        }
        throw new EdificioNoEncontradoException(tipo);

    }

    @Override
    public String infoCasilla(Jugador banca) {
        StringBuilder info = new StringBuilder();
        info.append("Tipo: ").append(getAlquiler()).append("\n");
        info.append("Grupo: ").append(grupo.getNombre()).append("\n");
        info.append("Dueño: ").append(getDuenho().getNombre()).append("\n");
        info.append("Alquiler: ").append(calcularCoste(0)).append("\n");

        info.append("Lista de edificios: ");
        for (Edificio e : edificios) {
            info.append(e.getID()).append(" ");
        }
        info.append("\nHipotecada: ").append((getHipotecada() ? "si" : "no"));
        info.append("\n");
        return info.toString();
    }

    @Override
    public float calcularCoste(int tirada) {
        float coste;
        coste = getAlquiler();
        if (getGrupo().esDuenhoGrupo(getDuenho())) {
            coste *= 2;
        }
        coste += alquilerEdificios();
        return coste;
    }

    public float alquilerEdificios() {
        float alquilerTotal = 0;
        for (Edificio e : edificios) {
            alquilerTotal += e.getAlquiler(); // Sumar el alquiler de cada edificio
        }
        return alquilerTotal;
    }

    public void setGrupo(Grupo g) {
        this.grupo = g;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public ArrayList<Edificio> getEdificios() {
        return edificios;
    }
}