package monopoly;

public class Hotel extends Edificio {

    public Hotel(Solar solar) {
        super(solar, solar.getValor()*0.6f);
    }

    @Override
    public void generateID(Solar solar) {
        setID("Hotel" + solar.numHoteles());
    }
}
