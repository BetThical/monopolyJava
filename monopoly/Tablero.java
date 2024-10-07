package monopoly;



import java.sql.ResultSet;
import partida.*;
import monopoly.Valor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Tablero {
    //Atributos.
    private ArrayList<ArrayList<Casilla>> posiciones; //Posiciones del tablero: se define como un arraylist de arraylists de casillas (uno por cada lado del tablero).
    private HashMap<String, Grupo> grupos; //Grupos del tablero, almacenados como un HashMap con clave String (será el color del grupo).
    private Jugador banca; //Un jugador que será la banca.

    //Constructor: únicamente le pasamos el jugador banca (que se creará desde el menú).
    public Tablero(Jugador b) {
        banca = b;
        posiciones = new ArrayList<>();
        grupos = new HashMap<>();

        generarCasillas();

    }


    //Método para crear todas las casillas del tablero. Formado a su vez por cuatro métodos (1/lado).
    private void generarCasillas() {
        this.insertarLadoSur();
        this.insertarLadoOeste();
        this.insertarLadoNorte();
        this.insertarLadoEste();
    }

    //Función para imprimir el tablero
    public void imprimirTablero() {
        System.out.print(toString());
    }

    //Método para insertar las casillas del lado sur.
    private void insertarLadoSur() {

        ArrayList<Casilla> ladoSur = new ArrayList<>();
        ladoSur.add(new Casilla("Salida", "especial", 1, banca));
        ladoSur.add(new Casilla("Solar1", "solar", 2, 600000, banca));
        ladoSur.add(new Casilla("Caja", "comunidad", 3, banca));
        ladoSur.add(new Casilla("Solar2", "solar", 4, 600000, banca));
        ladoSur.add(new Casilla("Imp1", "imposto", 5,((float) (Valor.SUMA_VUELTA * 0.5)),  banca));
        ladoSur.add(new Casilla("Trans1", "transporte", 6, 0, banca));
        ladoSur.add(new Casilla("Solar3", "solar", 7, 520000, banca));
        ladoSur.add(new Casilla("Suerte", "suerte", 8, banca));
        ladoSur.add(new Casilla("Solar4", "solar", 9, 520000, banca));
        ladoSur.add(new Casilla("Solar5", "solar", 10, 520000, banca));

        grupos.put("negro", new Grupo(ladoSur.get(1), ladoSur.get(3), "\u001B[30m"));
        grupos.put("cian", new Grupo(ladoSur.get(6), ladoSur.get(8), ladoSur.get(9), "\u001B[36m"));

        posiciones.add(ladoSur);
    }

    //Método que inserta casillas del lado oeste.
    private void insertarLadoOeste() {

        ArrayList<Casilla> ladoOeste = new ArrayList<>();
        ladoOeste.add(new Casilla("Carcel", "especial", 11, banca));
        ladoOeste.add(new Casilla("Solar6", "solar", 12, 676000, banca));
        ladoOeste.add(new Casilla("Serv1", "servicio", 13, banca));
        ladoOeste.add(new Casilla("Solar7", "solar", 14, 676000, banca));
        ladoOeste.add(new Casilla("Solar8", "solar", 15, 676000, banca));
        ladoOeste.add(new Casilla("Trans2", "transporte", 16, 0, banca));
        ladoOeste.add(new Casilla("Solar9", "solar", 17, 878000, banca));
        ladoOeste.add(new Casilla("Caja", "comunidad", 18, banca));
        ladoOeste.add(new Casilla("Solar10", "solar", 19, 878000, banca));
        ladoOeste.add(new Casilla("Solar11", "solar", 20, 878000, banca));

        grupos.put("morado", new Grupo(ladoOeste.get(1), ladoOeste.get(3), ladoOeste.get(4), "\u001B[35m"));
        grupos.put("amarillo", new Grupo(ladoOeste.get(6), ladoOeste.get(8), ladoOeste.get(9), "\u001B[33m"));


        posiciones.add(ladoOeste);

    }


    //Método para insertar las casillas del lado norte.
    private void insertarLadoNorte() {

        ArrayList<Casilla> ladoNorte = new ArrayList<>();
        ladoNorte.add(new Casilla("Parking", "especial", 21, banca));
        ladoNorte.add(new Casilla("Solar12", "solar", 22, 1142440, banca));
        ladoNorte.add(new Casilla("Suerte", "suerte", 23, banca));
        ladoNorte.add(new Casilla("Solar13", "solar", 24, 1142440, banca));
        ladoNorte.add(new Casilla("Solar14", "solar", 25, 1142440, banca));
        ladoNorte.add(new Casilla("Trans3", "transporte", 26, 0, banca));
        ladoNorte.add(new Casilla("Solar15", "solar", 27, 1485172, banca));
        ladoNorte.add(new Casilla("Solar16", "solar", 28, 1485172, banca));
        ladoNorte.add(new Casilla("Serv2", "servicio", 29, banca));
        ladoNorte.add(new Casilla("Solar17", "solar", 30, 1485172, banca));

        grupos.put("rojo", new Grupo(ladoNorte.get(1), ladoNorte.get(3), ladoNorte.get(4), "\u001B[31m"));
        grupos.put("marron", new Grupo(ladoNorte.get(6), ladoNorte.get(7), ladoNorte.get(9), "\u001B[37m"));

        posiciones.add(ladoNorte);

    }


    //Método que inserta las casillas del lado este.
    private void insertarLadoEste() {

        ArrayList<Casilla> ladoEste = new ArrayList<>();

        ladoEste.add(new Casilla("IrCarcel", "especial", 31, banca));
        ladoEste.add(new Casilla("Solar18", "solar", 32, (float) 1930723.6, banca));
        ladoEste.add(new Casilla("Solar19", "solar", 33, (float) 1930723.6, banca));
        ladoEste.add(new Casilla("Caja", "comunidad", 34, banca));
        ladoEste.add(new Casilla("Solar20", "solar", 35, (float) 1930723.6, banca));
        ladoEste.add(new Casilla("Trans4", "transporte", 36, 0, banca));
        ladoEste.add(new Casilla("Suerte", "suerte", 37, banca));
        ladoEste.add(new Casilla("Solar21", "solar", 38, (float) 3764922.02, banca));
        ladoEste.add(new Casilla("Imp2", "imposto", 39, ((float) Valor.SUMA_VUELTA), banca));
        ladoEste.add(new Casilla("Solar22", "solar", 40, (float) 3764922.02, banca));

        grupos.put("verde", new Grupo(ladoEste.get(1), ladoEste.get(2), ladoEste.get(4), "\u001B[32m"));
        grupos.put("azul", new Grupo(ladoEste.get(7), ladoEste.get(9), "\u001B[34m"));

        posiciones.add(ladoEste);


    }

    //Para imprimir el tablero, modificamos el método toString().
    @Override
    public String toString() {
        String[][] tableroArr = new String[11][11];

        for (int j = 0; j < 40; j++) {
            int fila, col;

            if (j < 11) { //lado sur
                fila = 10;
                col = 10 - j;
            } else if (j < 21) { //lado oeste
                fila = 20 - j;
                col = 0;
            } else if (j < 31) { //lado norte
                fila = 0;
                col = j - 20;
            } else {
                fila = j - 30; //lado este
                col = 10;
            }

            Casilla c = getCasilla(j);


            if (c.getTipo().equals("solar")) {
                tableroArr[fila][col] = String.format(c.getGrupo().getColor() + c.getNombre() + Valor.RESET);
            } else
                tableroArr[fila][col] = String.format(Valor.WHITE + c.getNombre() + Valor.RESET);
            
            if (!c.getAvatares().isEmpty()){
                if (c.getAvatares().size()>3)
                    tableroArr[fila][col] += String.format("[...]");
                else{
                    tableroArr[fila][col]+="&";
                    for (int i = 0; i < c.getAvatares().size(); i++) {
                        tableroArr[fila][col] += String.format(c.getAvatares().get(i).getID());
                    }}
            }

        }

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


    //Método usado para buscar la casilla con el nombre pasado como argumento:
    // public Casilla encontrar_casilla(String nombre){
    // }

    // GETTERS

    public Jugador getBanca() {
        return banca;
    }

    public Grupo getGrupo(String color) { //devuelve el grupo asociado a un color
        return grupos.get(color);
    }

    public Casilla getCasilla(int pos) { //devuelve una casilla a partir de su posición
        return posiciones.get((int) pos / 10).get(pos % 10);
    }

    
    //toma sobrecarga de métodos
    public Casilla getCasilla(String nombre) { // devuelve una casilla a partir de su nombre
        for (int i=0; i<getPosiciones().size(); i++){
            if (getCasilla(i).getNombre().equals(nombre))
                return getCasilla(i);
        }
        return null;
    }

 
    public ArrayList<ArrayList<Casilla>> getPosiciones() {
        return posiciones;
    }

    public void SetCasilla(Casilla c, int pos){ //modifica una casilla del tablero
        ArrayList<Casilla> lado = posiciones.get((int) pos/10);
        lado.set(pos%10, c);
        posiciones.set((int) pos/10, lado);
    }

    public void aumentarCoste(Jugador banca){
        for (int i=0; i<getPosiciones().size(); i++){
            if (getCasilla(i).getduenhoJugador() == banca){
                float valor = (getCasilla(i).getValor()*0.05f);
                getCasilla(i).sumarValor(valor);
            }
        }
    
    }
}
