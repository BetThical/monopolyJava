package monopoly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import partida.*;

public class Tablero {

    private final ArrayList<ArrayList<Casilla>> posiciones; // Posiciones del tablero: se define como un arraylist de
    // arraylists de casillas (uno por cada lado del tablero).
    private final HashMap<String, Grupo> grupos; // Grupos del tablero, almacenados como un HashMap con clave String
    // (será el color del grupo).

    private final ArrayList<Carta> barajaSuerte;
    private final ArrayList<Carta> barajaComunidad;
    private final Jugador banca; // Un jugador que será la banca.

    // Constructor: únicamente le pasamos el jugador banca (que se creará desde el
    // menú).
    public Tablero(Jugador b) {
        banca = b;
        posiciones = new ArrayList<>();
        grupos = new HashMap<>();
        barajaSuerte = new ArrayList<>();
        barajaComunidad = new ArrayList<>();

        generarCasillas();
        generarBarajas();

    }

    public int getNumCasillas() {
        // la cantidad de casillas es igual a la cantidad de filas por la cantidad de
        // columnas
        return posiciones.size() * posiciones.get(0).size();
    }

    // Método para crear todas las casillas del tablero. Formado a su vez por cuatro
    // métodos (1/lado).
    private void generarCasillas() {
        this.insertarLadoSur();
        this.insertarLadoOeste();
        this.insertarLadoNorte();
        this.insertarLadoEste();
    }

    private void generarBarajas() {
        barajaSuerte.add(new CartaSuerte(1,
                "Ve al Transportes1 y coge un avión. Si pasas por la casilla de Salida, cobra la cantidad habitual."));
        barajaSuerte.add(new CartaSuerte(2,
                "Decides hacer un viaje de placer. Avanza hasta Solar15 directamente, sin pasar por la casilla de Salida y sin cobrar la cantidad habitual."));
        barajaSuerte.add(
                new CartaSuerte(3,
                        "Vendes tu billete de avión para Solar17 en una subasta por Internet. Cobra 500000€."));
        barajaSuerte.add(
                new CartaSuerte(4, "Ve a Solar3. Si pasas por la casilla de Salida, cobra la cantidad habitual."));
        barajaSuerte.add(new CartaSuerte(5,
                "Los acreedores te persiguen por impago. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida y sin cobrar la cantidad habitual."));
        barajaSuerte.add(new CartaSuerte(6, "¡Has ganado el bote de la lotería! Recibe 1000000€."));

        barajaComunidad.add(
                new CartaComunidad(1, "Paga 500000€ por un fin de semana en un balneario de 5 estrellas."));
        barajaComunidad.add(new CartaComunidad(2,
                "Te investigan por fraude de identidad. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida y sin cobrar la cantidad habitual."));
        barajaComunidad.add(new CartaComunidad(3, "Colócate en la casilla de Salida. Cobra la cantidad habitual."));
        barajaComunidad.add(new CartaComunidad(4, "Tu compañía de Internet obtiene beneficios. Recibe 2000000€."));
        barajaComunidad.add(
                new CartaComunidad(5, "Paga 1000000€ por invitar a todos tus amigos a un viaje a Solar14."));
        barajaComunidad.add(new CartaComunidad(6,
                "Alquilas a tus compañeros una villa en Solar7 durante una semana. Paga 200000€ a cada jugador."));
    }

    // Función para imprimir el tablero
    public void imprimirTablero() {
        Juego.consola.imprimir("\n" + toString());
    }

    // Método para insertar las casillas del lado sur.
    private void insertarLadoSur() {

        ArrayList<Casilla> ladoSur = new ArrayList<>();
        ladoSur.add(new Especial("Salida", 1, banca)); // Especial: Salida
        ladoSur.add(new Solar("Solar1", 2, banca, 600000)); // Propiedad: Solar
        ladoSur.add(new AccionCajaComunidad("Caja", 3, banca)); // Acción: Caja de Comunidad
        ladoSur.add(new Solar("Solar2", 4, banca, 600000)); // Propiedad: Solar
        ladoSur.add(new Impuesto("Imp1", 5, banca, (float) (Valor.SUMA_VUELTA * 0.5))); // Impuesto
        ladoSur.add(new Transporte("Trans1", 6, banca, Valor.SUMA_VUELTA)); // Propiedad: Transporte
        ladoSur.add(new Solar("Solar3", 7, banca, 520000)); // Propiedad: Solar
        ladoSur.add(new AccionSuerte("Suerte", 8, banca)); // Acción: Suerte
        ladoSur.add(new Solar("Solar4", 9, banca, 520000)); // Propiedad: Solar
        ladoSur.add(new Solar("Solar5", 10, banca, 520000)); // Propiedad: Solar

        // Se crean los grupos correspondientes a esta fila.
        grupos.put(Valor.BLACK, new Grupo(ladoSur.get(1), ladoSur.get(3), Valor.BLACK + "Negro" + Valor.RESET));
        grupos.put(Valor.CYAN,
                new Grupo(ladoSur.get(6), ladoSur.get(8), ladoSur.get(9), Valor.CYAN + "Cián" + Valor.RESET));

        posiciones.add(ladoSur);
    }

    // Método que inserta casillas del lado oeste.
    private void insertarLadoOeste() {

        ArrayList<Casilla> ladoOeste = new ArrayList<>();
        ladoOeste.add(new Especial("Cárcel", 11, banca)); // Especial: Cárcel
        ladoOeste.add(new Solar("Solar6", 12, banca, 676000)); // Propiedad: Solar
        ladoOeste.add(new Servicio("Serv1", 13, banca, 0.75f * Valor.SUMA_VUELTA)); // Propiedad: Servicio
        ladoOeste.add(new Solar("Solar7", 14, banca, 676000)); // Propiedad: Solar
        ladoOeste.add(new Solar("Solar8", 15, banca, 676000)); // Propiedad: Solar
        ladoOeste.add(new Transporte("Trans2", 16, banca, Valor.SUMA_VUELTA)); // Propiedad: Transporte
        ladoOeste.add(new Solar("Solar9", 17, banca, 878000)); // Propiedad: Solar
        ladoOeste.add(new AccionCajaComunidad("Caja", 18, banca)); // Acción: Caja de Comunidad
        ladoOeste.add(new Solar("Solar10", 19, banca, 878000)); // Propiedad: Solar
        ladoOeste.add(new Solar("Solar11", 20, banca, 878000)); // Propiedad: Solar

        // Se crean los grupos correspondientes a esta fila.
        grupos.put(Valor.PURPLE,
                new Grupo(ladoOeste.get(1), ladoOeste.get(3), ladoOeste.get(4), Valor.PURPLE + "Morado" + Valor.RESET));
        grupos.put(Valor.YELLOW, new Grupo(ladoOeste.get(6), ladoOeste.get(8), ladoOeste.get(9),
                Valor.YELLOW + "Amarillo" + Valor.RESET));

        posiciones.add(ladoOeste);

    }

    // Método para insertar las casillas del lado norte.
    private void insertarLadoNorte() {

        ArrayList<Casilla> ladoNorte = new ArrayList<>();
        ladoNorte.add(new Especial("Parking", 21, banca)); // Especial: Parking
        ladoNorte.add(new Solar("Solar12", 22, banca, 1142440)); // Propiedad: Solar
        ladoNorte.add(new AccionSuerte("Suerte", 23, banca)); // Acción: Suerte
        ladoNorte.add(new Solar("Solar13", 24, banca, 1142440)); // Propiedad: Solar
        ladoNorte.add(new Solar("Solar14", 25, banca, 1142440)); // Propiedad: Solar
        ladoNorte.add(new Transporte("Trans3", 26, banca, Valor.SUMA_VUELTA)); // Propiedad: Transporte
        ladoNorte.add(new Solar("Solar15", 27, banca, 1485172)); // Propiedad: Solar
        ladoNorte.add(new Solar("Solar16", 28, banca, 1485172)); // Propiedad: Solar
        ladoNorte.add(new Servicio("Serv2", 29, banca, 0.75f * Valor.SUMA_VUELTA)); // Propiedad: Servicio
        ladoNorte.add(new Solar("Solar17", 30, banca, 1485172)); // Propiedad: Solar

        // Se crean los grupos correspondientes a esta fila.
        grupos.put(Valor.RED,
                new Grupo(ladoNorte.get(1), ladoNorte.get(3), ladoNorte.get(4), Valor.RED + "Rojo" + Valor.RESET));
        grupos.put(Valor.WHITE,
                new Grupo(ladoNorte.get(6), ladoNorte.get(7), ladoNorte.get(9), Valor.WHITE + "Marrón" + Valor.RESET));

        posiciones.add(ladoNorte);

    }

    // Método que inserta las casillas del lado este.
    private void insertarLadoEste() {

        ArrayList<Casilla> ladoEste = new ArrayList<>();

        ladoEste.add(new Especial("IrCarcel", 31, banca)); // Especial: Ir a la Cárcel
        ladoEste.add(new Solar("Solar18", 32, banca, 1930723.6f)); // Propiedad: Solar
        ladoEste.add(new Solar("Solar19", 33, banca, 1930723.6f)); // Propiedad: Solar
        ladoEste.add(new AccionCajaComunidad("Caja", 34, banca)); // Acción: Caja de Comunidad
        ladoEste.add(new Solar("Solar20", 35, banca, 1930723.6f)); // Propiedad: Solar
        ladoEste.add(new Transporte("Trans4", 36, banca, Valor.SUMA_VUELTA)); // Propiedad: Transporte
        ladoEste.add(new AccionSuerte("Suerte", 37, banca)); // Acción: Suerte
        ladoEste.add(new Solar("Solar21", 38, banca, 3764922.02f)); // Propiedad: Solar
        ladoEste.add(new Impuesto("Imp2", 39, banca, Valor.SUMA_VUELTA)); // Impuesto
        ladoEste.add(new Solar("Solar22", 40, banca, 3764922.02f)); // Propiedad: Solar

        // Se crean los grupos correspondientes a esta fila.
        grupos.put(Valor.GREEN,
                new Grupo(ladoEste.get(1), ladoEste.get(2), ladoEste.get(4), (Valor.GREEN + "Verde" + Valor.RESET)));
        grupos.put(Valor.BLUE, new Grupo(ladoEste.get(7), ladoEste.get(9), (Valor.BLUE + "Azul" + Valor.RESET)));

        posiciones.add(ladoEste);

    }

    // Para imprimir el tablero, modificamos el método toString().
    @Override
    public String toString() {
        String[][] tableroArr = new String[11][11]; // Se crea el tablero como un array bidimensional 11x11.

        for (int j = 0; j < 40; j++) {
            int fila, col;

            if (j < 11) { // lado sur
                fila = 10;
                col = 10 - j;
            } else if (j < 21) { // lado oeste
                fila = 20 - j;
                col = 0;
            } else if (j < 31) { // lado norte
                fila = 0;
                col = j - 20;
            } else {
                fila = j - 30; // lado este
                col = 10;
            }

            Casilla casilla = getCasilla(j);

            if (casilla instanceof Solar) { // Si es un solar, se imprime con el color correspondiente.

                tableroArr[fila][col] = String
                        .format(getCodigoColor(((Solar) casilla).getGrupo()) + casilla.getNombre() + Valor.RESET);
            } else {
                tableroArr[fila][col] = String.format(Valor.WHITE + casilla.getNombre() + Valor.RESET);
            }

            if (!casilla.getAvatares().isEmpty()) { // Si hay avatares en la casilla, se añaden al nombre
                // if (c.getAvatares().size() > 3)
                // tableroArr[fila][col] += String.format("[...]"); // Si hay más de 3, se
                // imprime un [...] por falta de espacio.
                {
                    tableroArr[fila][col] += "&";

                    for (int i = 0; i < casilla.getAvatares().size(); i++) {
                        Avatar avatar = casilla.getAvatares().get(i);
                        tableroArr[fila][col] += String.format(avatar.getID());
                    }
                }
            }

        }

        // Se crea un string que represente el tablero.
        StringBuilder tableroStr = new StringBuilder();
        for (int i = 0; i < 11; i++) {
            for (int k = 0; k < 11; k++) {
                if (tableroArr[i][k] == null) {
                    tableroStr.append(String.format("%-14s", ""));
                } else {
                    tableroStr.append(String.format("| %-20s|", tableroArr[i][k]));
                }
            }
            tableroStr.append("\n");

        }
        return tableroStr.toString();

    }

    // Método usado para buscar la casilla con el nombre pasado como argumento:
    // public Casilla encontrar_casilla(String nombre){
    // }
    // GETTERS
    public Jugador getBanca() {
        return banca;
    }

    public Grupo getGrupo(String color) { // devuelve el grupo asociado a un color
        return grupos.get(color);
    }

    public Casilla getCasilla(int pos) { // devuelve una casilla a partir de su posición
        return posiciones.get((int) pos / 10).get(pos % 10);
    }

    public Casilla getCasilla(String nombre) {// devuelve una casilla a partir de su nombre
        for (int i = 0; i < 40; i++) {
            if (getCasilla(i).getNombre().equals(nombre)) {
                return getCasilla(i);
            }
        }
        return null;
    }

    private String getCodigoColor(Grupo g) { // devuelve el código asociado a un grupo, que permite imprimir texto con
        // color
        for (String key : grupos.keySet()) {
            if (grupos.get(key).equals(g)) {
                return key;
            }
        }
        return "error";
    }

    public Collection<Grupo> getGrupos() {
        return grupos.values();
    }

    public Grupo getGrupoNombre(String nombre) {
        for (Grupo grupo : grupos.values()) {
            if (grupo.getNombre().contains(nombre)) {
                return grupo;
            }
        }
        return null;
    }

    public ArrayList<Carta> getSuerte() {
        return barajaSuerte;
    }

    public ArrayList<Carta> getComunidad() {
        return barajaComunidad;
    }

    public ArrayList<ArrayList<Casilla>> getPosiciones() {
        return posiciones;
    }

    public void SetCasilla(Casilla c, int pos) { // modifica una casilla del tablero
        ArrayList<Casilla> lado = posiciones.get((int) pos / 10);
        lado.set(pos % 10, c);
        posiciones.set((int) pos / 10, lado);
    }

    public void aumentarCoste(Jugador banca) { // Aumenta el coste de todas las casillas sin dueño en un 5%. Se usa
        // cuando los jugadores dan 4 vueltas.
        for (int i = 0; i < 40; i++) {
            if ((getCasilla(i) instanceof Propiedad)) {
                Propiedad propiedad = (Propiedad) getCasilla(i);
                if (propiedad.getDuenho().equals(banca)) {
                    float valor = (propiedad.getValor() * 0.05f);
                    propiedad.sumarValor(valor);
                }
            }
        }
    }

}
