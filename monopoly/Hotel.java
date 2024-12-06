package monopoly;

public class Hotel extends Edificio {

    public Hotel(Casilla casilla) {
        super(casilla, casilla.getValor()*0.6f);
    }

    @Override
    public void generateID(Casilla casilla) {
        setID("Hotel" + casilla.numHoteles());
    }
}
