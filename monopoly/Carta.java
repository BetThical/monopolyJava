package monopoly;
import java.util.HashMap;

public class Carta{

    int id; 
    String texto;

    public Carta(int id, String texto){
        this.id = id;
        this.texto = texto;
    }

    public String getCarta(){
        return texto;
    }
    
}
