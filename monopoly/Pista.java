package monopoly;

public class Pista extends Edificio {

    public Pista(Casilla casilla) {
        super(casilla, casilla.getValor()*1.25f);
    }

    @Override
    public void generateID(Casilla casilla) {
        setID("Pista-" + casilla.numPistas());
    }
}
