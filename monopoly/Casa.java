package monopoly;

public class Casa extends Edificio {

    public Casa(Casilla casilla) {
        super(casilla, casilla.getValor()*0.6f);
    }

    @Override
    public void generateID(Casilla casilla) {
        setID("Casa-" + casilla.numCasas());
    }
}
