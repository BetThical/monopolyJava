package monopoly;

public abstract class Edificio {

    // Atributos
    private float alquiler; // valor que suma al alquiler de la casilla
    private float valor; // precio de edificar
    private String id;

    public Edificio(Casilla casilla, float coste) {
        generateID(casilla); // implementado por las subclases
        this.valor = coste;
    }

    public abstract void generateID(Casilla casilla);

    public String getID() {
        return id;
    }

    public float getValor() {
        return valor;
    }

    public String getTipoNombre() {
        return this.getClass().getSimpleName();
    }

    protected void setID(String id) {
        this.id = id;
    }

    protected void setValor(float c) {
        valor = c;
    }

    public float getAlquiler() {
        return alquiler;
    }

    public void setAlquiler(float a) {
        alquiler = a;
    }

}
