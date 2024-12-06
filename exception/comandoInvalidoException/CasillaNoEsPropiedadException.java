package exception.comandoInvalidoException;

public class CasillaNoEsPropiedadException extends ComandoInvalidoException {
    public CasillaNoEsPropiedadException(String nombreCasilla) {
        super("La casilla " + nombreCasilla + " no es una propiedad.");
    }
    
}
