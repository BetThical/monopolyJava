package partida;
import java.util.concurrent.ThreadLocalRandom;

public class Dado {
    //El dado solo tiene un atributo en nuestro caso: su valor.
    private int valor;

    //Metodo para simular lanzamiento de un dado: devolverá un valor aleatorio entre 1 y 6.
    public int hacerTirada() {
        return ThreadLocalRandom.current().nextInt(1, 7);
    }

}
