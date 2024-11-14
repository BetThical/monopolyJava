package monopoly;



public class Edificio {
    // Atributos
    private final String tipo;
    private float alquiler;
    private final int id;


    public Edificio(String tipo, Casilla casilla) {
        this.tipo = tipo;
        this.id = casilla.contarEdificiosPorTipo().getOrDefault(tipo, 0)+1;
    }
    public String getID(){
        return tipo + "-" + String.valueOf(id);
    }
    public String getTipo(){
        return tipo;
    }

    public float getAlquiler() {
        return alquiler;
    }

    public void setAlquiler(float a) {
        alquiler = a;
    }


}
