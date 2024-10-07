package monopoly;


import com.sun.source.tree.TryTree;
import partida.*;
import java.util.ArrayList;


//pepito


public class Casilla {

    //Atributos:
    private String nombre; //Nombre de la casilla
    private String tipo; //Tipo de casilla (Solar, Especial, Transporte, Servicios, Comunidad, Sorte, Impostos).
    private float valor; //Valor de esa casilla (en la mayoría será valor de compra, en la casilla parking se usará como el bote).
    private int posicion; //Posición que ocupa la casilla en el tablero (entero entre 1 y 40).
    private Jugador duenho; //Dueño de la casilla (por defecto sería la banca).
    private Grupo grupo; //Grupo al que pertenece la casilla (si es solar).
    private float impuesto; //Cantidad a pagar por caer en la casilla: el alquiler en solares/servicios/transportes o impuestos.
    private float hipoteca; //Valor otorgado por hipotecar una casilla
    private ArrayList<Avatar> avatares; //Avatares que están situados en la casilla.

    //Constructores:
    public Casilla() {
    }//Parámetros vacíos

    /*Constructor para casillas tipo Solar, Servicios o Transporte:
    * Parámetros: nombre casilla, tipo (debe ser solar, serv. o transporte), posición en el tablero, valor y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, float valor, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.valor = valor;
        if (tipo.equals("solar"))
            this.impuesto = ((float)(valor*0.1));
        else if (tipo.equals("transporte"))
            this.impuesto = Valor.SUMA_VUELTA;
        else //servicio
            this.impuesto = (Valor.SUMA_VUELTA/200);
        this.duenho = duenho;
        this.avatares = new ArrayList<>();
    }

    /*Constructor utilizado para inicializar las casillas de tipo IMPUESTOS.
    * Parámetros: nombre, posición en el tablero, impuesto establecido y dueño.
     */
    public Casilla(String nombre, int posicion, float impuesto, Jugador duenho) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.impuesto = impuesto;
        this.duenho = duenho;
        this.avatares = new ArrayList<>();
    }

    /*Constructor utilizado para crear las otras casillas (Suerte, Caja de comunidad y Especiales):
    * Parámetros: nombre, tipo de la casilla (será uno de los que queda), posición en el tablero y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.duenho = duenho;
        this.valor=0; //para el parking
        this.avatares = new ArrayList<>();
    }

    //Método utilizado para añadir un avatar al array de avatares en casilla.
    public void anhadirAvatar(Avatar av) {
        if (!avatares.contains(av))
            avatares.add(av);
        else{
            System.out.println("error: añadido avatar que ya estaba en casilla");
        }
    }

    //Método utilizado para eliminar un avatar del array de avatares en casilla.
    public void eliminarAvatar(Avatar av) {
        if (avatares.contains(av))
            avatares.remove(av);
        else{
                System.out.println("error: eliminado avatar que no estaba en casilla");
            }
    }

    /*Método para luar qué hacer en una casilla concreta. Parámetros:
    * - Jugador cuyo avatar está en esa casilla.
    * - La banca (para ciertas comprobaciones).
    * - El valor de la tirada: para determinar impuesto a pagar en casillas de servicios.
    * Valor devuelto: true en caso de ser solvente (es decir, de cumplir las deudas), y false
    * en caso de no cumplirlas.*/
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {  
        if (esComprable(actual, banca)){
            System.out.println("El jugador " + actual.getNombre() + " puede comprar esta casilla, por " + getValor() +" euros.");
        }

        if (getduenhoJugador() != banca && getduenhoJugador() != actual)
            return actual.pagar(calcular_coste(tirada), duenho);
        
        if (getNombre().equals("Parking")){
            actual.cobrarBote(banca);
            return true;
        }        

        if (getTipo() == "imposto"){
            return actual.pagar(impuesto);
        }

        if (getNombre().equals("IrCarcel"))
            System.out.println("El jugador " + actual.getNombre() + " va a la cárcel.");
        return true;

    }

    private float calcular_coste(int tirada){
        float coste;
        if(tipo.equals("solar")){
            coste = getImpuesto();
            }
        else if (tipo.equals("transporte")){
            coste = (getImpuesto() * (0.25f * duenho.getNumTrans()));
        }
        else { //servicio
            if (duenho.getNumServ() == 1)
                coste = (getImpuesto()*4*tirada);
            else
                coste = (getImpuesto()*10*tirada);
        } 
        return coste;
    }



    //devuelve true si se puede comprar la casilla
    public boolean esComprable(Jugador comprador, Jugador banca){
        return (("solar".equals(tipo) || "transporte".equals(tipo) || "servicio".equals(tipo)) && (duenho == banca) && (valor <= comprador.getFortuna()));
        

    }
    /*Método usado para comprar una casilla determinada. Parámetros:
    * - Jugador que solicita la compra de la casilla.
    * - Banca del monopoly (es el dueño de las casillas no compradas aún).*/
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        banca.sumarFortuna(valor);
        solicitante.sumarGastos(valor);
        this.duenho = solicitante;
        solicitante.anhadirPropiedad(this);
    }

    /*Método para añadir valor a una casilla. Utilidad:
    * - Sumar valor a la casilla de parking.
    * - Sumar valor a las casillas de solar al no comprarlas tras cuatro vueltas de todos los jugadores.
    * Este método toma como argumento la cantidad a añadir del valor de la casilla.*/
    public void sumarValor(float suma) {
        this.valor += suma;
    }

    /*Método para mostrar información sobre una casilla.
    * Devuelve una cadena con información específica de cada tipo de casilla.
    * Banca para poder imprimir el bote. */
    public String infoCasilla(Jugador banca) {

        StringBuilder output = new StringBuilder();
    
        output.append("- Tipo: ").append(getTipo()).append("\n");
        
        if (getTipo().equals("solar")) {
            output.append("- Grupo: ").append(getGrupo().getColor()).append("\n");
            output.append("- Valor: ").append(getValor()).append("\n");
            output.append("- Alquiler: ").append(getImpuesto()).append("\n");
        } 
        else if (getTipo().equals("imposto")) {
            output.append("- Imposto: ").append(getImpuesto()).append("\n");
        } 
        else if (getNombre().equals("parking")) {
            output.append("- Bote: ").append(banca.getBote()).append("\n");
        } 
        else if (getNombre().equals("carcel")) {
            output.append("- Fianza: ").append(Valor.FORTUNA_INICIAL * 0.25).append("\n");
        }
    
        if (getAvatares().size() > 0) {
            output.append("- Jugadores:\n");
            for (int i = 0; i < getAvatares().size(); i++) {
                output.append("   · ").append(getAvatares().get(i).getJugador().getNombre()).append("\n");
            }
        }
    
        output.append("\n"); 
    
        return output.toString();    }

    /* Método para mostrar información de una casilla en venta.
     * Valor devuelto: texto con esa información.
     */
    public String casEnVenta() {

        if (tipo.equals("solar") ) {
            return grupo.getColor() + "tipo: " + tipo + ", grupo: " + grupo + ", valor: " + valor + "€.";
        }
        return Valor.RESET + "tipo: " + tipo + ", valor: " + valor + "€.";
    }

    // GETTERS
    public String getNombre(){
        return nombre;
    }

    public Grupo getGrupo(){
        return grupo;
    }

    public float getValor(){
        return valor;
    }

    public float getImpuesto(){
        return impuesto;
    }

    public Jugador getduenhoJugador(){
        return duenho;
    }

    public int getPosicion(){
        return posicion;
    }

    public String getTipo(){
        return tipo;
    }

    public float getHipoteca(){
        return hipoteca;
    }

    
    public ArrayList<Avatar> getAvatares(){
        return avatares;
    }

    // SETTERS
    public void setPosicion(int p){
        this.posicion = p;
    }

    public void setGrupo(Grupo g){
        this.grupo = g;
    }

    public void setValor(float v){
        this.valor = v;
    }

    public void setDuenho(Jugador j){
        this.duenho = j;
    }



}
