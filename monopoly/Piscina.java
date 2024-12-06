package monopoly;

public class Piscina extends Edificio {

    public Piscina(Casilla casilla) {
        super(casilla, casilla.getValor()*0.4f);
    }

    @Override
    public void generateID(Casilla casilla) {
        setID("Piscina-" + casilla.numPiscinas());
    }
}
