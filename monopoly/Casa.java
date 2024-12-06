package monopoly;

public class Casa extends Edificio {

    public Casa(Solar solar) {
        super(solar, solar.getValor()*0.6f);
    }

    @Override
    public void generateID(Solar solar) {
        setID("Casa-" + solar.numCasas());
    }
}
