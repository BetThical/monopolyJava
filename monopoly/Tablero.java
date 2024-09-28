package monopoly;

import partida.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Tablero {
    //Atributos.
    private ArrayList<ArrayList<Casilla>> posiciones; //Posiciones del tablero: se define como un arraylist de arraylists de casillas (uno por cada lado del tablero).
    private HashMap<String, Grupo> grupos; //Grupos del tablero, almacenados como un HashMap con clave String (será el color del grupo).
    private Jugador banca; //Un jugador que será la banca.

    //Constructor: únicamente le pasamos el jugador banca (que se creará desde el menú).
    public Tablero(Jugador banca) {
    }

    
    //Método para crear todas las casillas del tablero. Formado a su vez por cuatro métodos (1/lado).
    private void generarCasillas() {
        this.insertarLadoSur();
        this.insertarLadoOeste();
        this.insertarLadoNorte();
        this.insertarLadoEste();
    }
    
    //Método para insertar las casillas del lado sur.
    private void insertarLadoSur() {
        ArrayList<Casilla> ladoSur = this.posiciones.get(0);
            
        ladoNorte.add(new Casilla("Salida", "especial", 1, banca));
        ladoNorte.add(new Casilla("Solar1", "solar", 2, 600000, banca));
        ladoNorte.add(new Casilla("Caja", "comunidad", 3, banca));
        ladoNorte.add(new Casilla("Solar2", "solar", 4, , banca));
        ladoNorte.add(new Casilla("Imp1", "solar", 5, banca));
        ladoNorte.add(new Casilla("Trans1", "transporte", 6, valor, banca));
        ladoNorte.add(new Casilla("Solar3", "solar", 7, valor, banca));
        ladoNorte.add(new Casilla("Suerte", "solar", 8, banca));
        ladoNorte.add(new Casilla("Solar4", "servicio", 9, valor, banca));
        ladoNorte.add(new Casilla("Solar5", "solar", 10, valor, banca));
    }

     //Método que inserta casillas del lado oeste.
    private void insertarLadoOeste() {
        
        ArrayList<Casilla> ladoOeste = this.posiciones.get(1);
            
        ladoNorte.add(new Casilla("Carcel", "especial", 11, banca));
        ladoNorte.add(new Casilla("Solar6", "solar", 12, valor, banca));
        ladoNorte.add(new Casilla("Serv1", "servicio", 13, banca));
        ladoNorte.add(new Casilla("Solar7", "solar", 14, valor, banca));
        ladoNorte.add(new Casilla("Solar8", "solar", 15, valor, banca));
        ladoNorte.add(new Casilla("Trans2", "transporte", 16, valor, banca));
        ladoNorte.add(new Casilla("Solar9", "solar", 17, valor, banca));
        ladoNorte.add(new Casilla("Caja", "comunidad", 18, banca));
        ladoNorte.add(new Casilla("Solar10", "solar", 19, valor, banca));
        ladoNorte.add(new Casilla("Solar11", "solar", 20, valor, banca));
    }


    //Método para insertar las casillas del lado norte.
    private void insertarLadoNorte() {

        ArrayList<Casilla> ladoNorte = this.posiciones.get(2);
        
        ladoNorte.add(new Casilla("Parking", "especial", 21, banca));
        ladoNorte.add(new Casilla("Solar12", "solar", 22, valor , banca));
        ladoNorte.add(new Casilla("Suerte", "suerte", 23, banca));
        ladoNorte.add(new Casilla("Solar13", "solar", 24, valor, banca));
        ladoNorte.add(new Casilla("Solar14", "solar", 25, valor, banca));
        ladoNorte.add(new Casilla("Trans3", "transporte", 26, valor, banca));
        ladoNorte.add(new Casilla("Solar15", "solar", 27, valor,  banca));
        ladoNorte.add(new Casilla("Solar16", "solar", 28, valor, banca));
        ladoNorte.add(new Casilla("Serv2", "servicio", 29, banca));
        ladoNorte.add(new Casilla("Solar17", "solar", 30, valor, banca));

    }

    
   
    //Método que inserta las casillas del lado este.
    private void insertarLadoEste() {

        ArrayList<Casilla> ladoOeste = this.posiciones.get(1);
            
        ladoNorte.add(new Casilla("Carcel", "especial", 31, banca));
        ladoNorte.add(new Casilla("Solar6", "solar", 32, valor, banca));
        ladoNorte.add(new Casilla("Serv1", "servicio", 33, banca));
        ladoNorte.add(new Casilla("Solar7", "solar", 34, valor, banca));
        ladoNorte.add(new Casilla("Solar8", "solar", 35, valor, banca));
        ladoNorte.add(new Casilla("Trans2", "transporte", 36, valor, banca));
        ladoNorte.add(new Casilla("Solar9", "solar", 37, valor, banca));
        ladoNorte.add(new Casilla("Caja", "comunidad", 38, banca));
        ladoNorte.add(new Casilla("Solar10", "solar", 39, valor, banca));
        ladoNorte.add(new Casilla("Solar11", "solar", 40, valor, banca));

    }

    //Para imprimir el tablero, modificamos el método toString().
    @Override
    public String toString() {
    }
    
    //Método usado para buscar la casilla con el nombre pasado como argumento:
    public Casilla encontrar_casilla(String nombre){
    }

    // GETTERS

    public Jugador getBanca(){
        return banca;
    }
    
    public Grupo getGrupo(String color){ //devuelve el grupo asociado a un color
        return grupos.get(color);
    }
}
