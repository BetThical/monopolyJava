package monopoly;



import partida.*;
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

        generarCasillas();
        toString();
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

        ArrayList<Casilla> ladoSur = new ArrayList<>();
        ladoSur.add(new Casilla("Salida", "especial", 1, banca));
        ladoSur.add(new Casilla("Solar1", "solar", 2, 600000, banca));
        ladoSur.add(new Casilla("Caja", "comunidad", 3, banca));
        ladoSur.add(new Casilla("Solar2", "solar", 4, 600000,banca));
        ladoSur.add(new Casilla("Imp1", "imposto", 5, banca));
        ladoSur.add(new Casilla("Trans1", "transporte", 6, 0, banca));
        ladoSur.add(new Casilla("Solar3", "solar", 7, 520000, banca));
        ladoSur.add(new Casilla("Suerte", "solar", 8, banca));
        ladoSur.add(new Casilla("Solar4", "servicio", 9, 520000, banca));
        ladoSur.add(new Casilla("Solar5", "solar", 10, 520000, banca));
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
        posiciones.add(ladoOeste);

    }


    //Método para insertar las casillas del lado norte.
    private void insertarLadoNorte() {

        ArrayList<Casilla> ladoNorte = new ArrayList<>();         
        ladoNorte.add(new Casilla("Parking", "especial", 21, banca));
        ladoNorte.add(new Casilla("Solar12", "solar", 22, 1142440 , banca));
        ladoNorte.add(new Casilla("Suerte", "suerte", 23, banca));
        ladoNorte.add(new Casilla("Solar13", "solar", 24, 1142440, banca));
        ladoNorte.add(new Casilla("Solar14", "solar", 25, 1142440, banca));
        ladoNorte.add(new Casilla("Trans3", "transporte", 26, 0, banca));
        ladoNorte.add(new Casilla("Solar15", "solar", 27, 1485172,  banca));
        ladoNorte.add(new Casilla("Solar16", "solar", 28, 1485172, banca));
        ladoNorte.add(new Casilla("Serv2", "servicio", 29, banca));
        ladoNorte.add(new Casilla("Solar17", "solar", 30, 1485172, banca));
        posiciones.add(ladoNorte);

    }

    
   
    //Método que inserta las casillas del lado este.
    private void insertarLadoEste() {

        ArrayList<Casilla> ladoEste = new ArrayList<>();
            
        ladoEste.add(new Casilla("IrCarcel", "especial", 31, banca));
        ladoEste.add(new Casilla("Solar18", "solar", 32, (float)1930723.6, banca));
        ladoEste.add(new Casilla("Solar19", "solar", 33, (float)1930723.6, banca));
        ladoEste.add(new Casilla("Caja", "comunidad", 34, banca));
        ladoEste.add(new Casilla("Solar20", "solar", 35, (float)1930723.6, banca));
        ladoEste.add(new Casilla("Trans4", "transporte", 36, 0, banca));
        ladoEste.add(new Casilla("Suerte", "suerte", 37, banca));
        ladoEste.add(new Casilla("Solar21", "solar", 38, (float)3764922.02, banca));
        ladoEste.add(new Casilla("Imp2", "imposto", 39, banca));
        ladoEste.add(new Casilla("Solar22", "solar", 40, (float)3764922.02, banca));
        posiciones.add(ladoEste);


    }

    //Para imprimir el tablero, modificamos el método toString().
    @Override
    public String toString() {
        String[][] tableroArr = new String[11][11];

        for (int j = 0; j<40; j++){
            int fila,col;

            if (j<11){ //lado sur
                fila = 10;
                col = 10-j;
            }
            else if (j<21){ //lado oeste
                fila = 20-j;
                col = 0;
            }
            else if (j<31){ //lado norte
                fila = 0;
                col = j-20;
            }
            else {
                fila = j-30; //lado este
                col = 10;
            }
        
        tableroArr[fila][col] = getCasilla(j).getNombre();
    }


    for (int i = 0; i < 11; i++) {
        for (int k = 0; k < 11; k++) {
            if(tableroArr[i][k] == null)             
                System.out.printf("%-12s", "");
            else
                System.out.printf("|%-10s|", tableroArr[i][k]);
        }
        System.out.println();
    }
    
    

    return "";



    }  
    
    
    
    //Método usado para buscar la casilla con el nombre pasado como argumento:
    // public Casilla encontrar_casilla(String nombre){
    // }

    // GETTERS

    public Jugador getBanca(){
        return banca;
    }
    
    public Grupo getGrupo(String color){ //devuelve el grupo asociado a un color
        return grupos.get(color);
    }

    public Casilla getCasilla(int pos){
        
        
        return posiciones.get((int) pos/10).get(pos%10);
    }
}
