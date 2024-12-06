package monopoly;

public class Piscina extends Edificio {

    public Piscina(Solar solar) {
        super(solar, solar.getValor()*0.4f);
    }

    @Override
    public void generateID(Solar solar) {
        setID("Piscina-" + solar.numPiscinas());
    }
}
