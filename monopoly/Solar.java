package monopoly;

import java.util.ArrayList;
import java.util.HashMap;

import exception.comandoInvalidoException.EdificioNoPermitidoException;
import exception.noEncontradoException.EdificioNoEncontradoException;
import partida.*;

public class Solar extends Propiedad{

    // Atributos:
    private float hipoteca;
    private float impuesto;
    private Jugador duenho;
    private Grupo grupo;
    private int posicion;
    private ArrayList<Edificio> edificios;

    // Constructores:
    public Solar(String nombre, int posicion, Jugador duenho, float valor) {
        super(nombre, posicion, duenho, "solar", valor);
        this.impuesto = (Valor.SUMA_VUELTA / 200);
        this.hipoteca = valor / 2f;
        this.edificios = new ArrayList<>();
    }


    // Métodos:

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
        // casillas.

        if (!constructor.equals(duenho)) {
            throw new EdificioNoPermitidoException("No eres dueño de esta casilla");
        }

        if (valorEdificio(e.getTipo()) > constructor.getFortuna()) {
            throw new EdificioNoPermitidoException("No tienes fondos suficientes para construir este edificio");
        }

        if (!grupo.esDuenhoGrupo(constructor) && constructor.getAvatar().getVecesCaidasEnCasilla(posicion - 1) <= 2) {
            throw new EdificioNoPermitidoException(
                    "Debes ser dueño de todo el grupo o haber caído en esta casilla más de dos veces. Actual: "
                    + constructor.getAvatar().getVecesCaidasEnCasilla(posicion - 1));
        }
        HashMap<String, Integer> edificiosGrupo = grupo.contarEdificiosPorTipo();
        HashMap<String, Integer> edificiosCasilla = contarEdificiosPorTipo();
        int maxEdificiosPorTipo = grupo.getNumCasillas();
        if (edificiosCasilla.getOrDefault("casa", 0) >= 4 && e.getTipo().equals("casa")) {
            throw new EdificioNoPermitidoException("Se pueden construir un máximo de 4 casas en un solar");
        }

        if (edificiosCasilla.getOrDefault("casa", 0) < 4 && e.getTipo().equals("hotel")) {
            throw new EdificioNoPermitidoException("Para construir un hotel se deben construir antes 4 casas");
        }

        if (edificiosGrupo.getOrDefault("hotel", 0) >= maxEdificiosPorTipo) {
            if (e.getTipo().equals("hotel")) {
                throw new EdificioNoPermitidoException(
                        "Se pueden construir un máximo de " + maxEdificiosPorTipo + " hoteles en este grupo");
            }
        }

        if (edificiosGrupo.getOrDefault("hotel", 0) == (maxEdificiosPorTipo - 1) && e.getTipo().equals("hotel")
                && edificiosGrupo.getOrDefault("casa", 0) > maxEdificiosPorTipo) {
            throw new EdificioNoPermitidoException(
                    "Construir este hotel haría que se superase el número máximo de casas en esta casilla");
        }

        if (e.getTipo().equals("casa") && edificiosGrupo.getOrDefault("casa", 0) >= maxEdificiosPorTipo
                && edificiosGrupo.getOrDefault("hotel", 0) >= maxEdificiosPorTipo) {
            throw new EdificioNoPermitidoException("Se pueden construir un máximo de " + grupo.getNumCasillas()
                    + " casas en este grupo.");
        }

        if (e.getTipo().equals("hotel") && edificiosGrupo.getOrDefault("casa", 0) - 4 > maxEdificiosPorTipo
                && edificiosGrupo.getOrDefault("hotel", 0) + 1 >= maxEdificiosPorTipo) {
            throw new EdificioNoPermitidoException(
                    "No se puede construir un hotel porque se superaría el número máximo de casas permitido. (se permite superar este límite hasta construir los hoteles)");
        }

        if (e.getTipo().equals("piscina")) {
            if (edificiosGrupo.getOrDefault("piscina", 0) >= maxEdificiosPorTipo) {
                throw new EdificioNoPermitidoException(
                        "Se pueden construir un máximo de " + maxEdificiosPorTipo + " piscinas en este grupo");
            }
            if (edificiosCasilla.getOrDefault("casa", 0) < 2 || edificiosCasilla.getOrDefault("hotel", 0) < 1) {
                throw new EdificioNoPermitidoException(
                        "Para construir una piscina, se deben construir antes al menos 2 casas y 1 hotel");
            }
        }

        if (e.getTipo().equals("pista")) {
            if (edificiosGrupo.getOrDefault("pista", 0) == grupo.getNumCasillas()) {
                throw new EdificioNoPermitidoException(
                        "Se pueden construir un máximo de " + grupo.getNumCasillas()
                        + " pistas de deporte en este grupo.");
            }
            if (edificiosCasilla.getOrDefault("hotel", 0) < 2) {
                throw new EdificioNoPermitidoException(
                        "Para construir una pista de deporte, se deben construir antes al menos 2 hoteles");
            }
        }
    }


    public void anhadirEdificio(Edificio e, Jugador jugador) throws EdificioNoEncontradoException {
        int numCasas = contarEdificiosPorTipo().getOrDefault("casa", 0);
        float alquilerEdificio = 0;
        if (e.getTipo().equals("hotel")) {
            for (int i = 0; i < 4; i++) {
                destruirEdificio("casa", jugador);
            }
        }
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

    }


    public void destruirEdificio(String tipo, Jugador jugador) throws EdificioNoEncontradoException {
        for (int i = edificios.size() - 1; i >= 0; i--) {
            if (edificios.get(i).getTipo().equalsIgnoreCase(tipo)) {
                edificios.remove(i);
                jugador.sumarFortuna(valorEdificio(tipo) / 2f);

                return;
            }
        }
        throw new EdificioNoEncontradoException(tipo);

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


}