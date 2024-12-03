package exception.comandoIncorrectoException;

// AcabarTurnoException: Uso del comando 'acabar turno' cuando se debe tomar otra acción antes: pagar una deuda, lanzar los dados, coger una carta...
public class AceptarRechazarException extends ComandoIncorrectoException {
    public AceptarRechazarException() {
        super("[aceptar/rechazar] trato [id del trato]");
    }
}