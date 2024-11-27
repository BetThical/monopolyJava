package exception.comandoIncorrectoException;

public class PropiedadNoIndicadaException extends ComandoIncorrectoException {
    public PropiedadNoIndicadaException(String comando) {
        super(comando + " [propiedad]");
    }
}
