package exception.comandoIncorrectoException;

// PropiedadNoIndicadaException: Uso incorrecto de un comando que requiere especificar la propiedad (hipotecar, deshipotecar)
public class PropiedadNoIndicadaException extends ComandoIncorrectoException {

    public PropiedadNoIndicadaException(String comando) {
        super(comando + " [propiedad]");
    }
}
