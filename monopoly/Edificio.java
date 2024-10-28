package monopoly;



public class Edificio {
    // Atributos
    private final String tipo;
    private float alquiler;


    public Edificio(String tipo) {
        this.tipo = tipo;
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
