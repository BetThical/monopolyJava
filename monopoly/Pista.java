package monopoly;

public class Pista extends Edificio {

    public Pista(Solar solar) {
        super(solar, solar.getValor()*1.25f);
    }

    @Override
    public void generateID(Solar solar) {
        setID("Pista-" + solar.numPistas());
    }
}
