package monopoly;

import partida.*;
import java.util.ArrayList;

class Grupo {

    // Atributos
    private ArrayList<Casilla> miembros; // Casillas miembros del grupo.
    private String colorGrupo; // Color del grupo
    private int numCasillas; // Número de casillas del grupo.

    // Constructor vacío.
    public Grupo() {
    }

    /*
     * Constructor para cuando el grupo está formado por DOS CASILLAS:
     * Requiere como parámetros las dos casillas miembro y el color del grupo.
     */
    public Grupo(Casilla cas1, Casilla cas2, String colorGrupo) {
        this.colorGrupo = colorGrupo;
        this.miembros = new ArrayList<>();

        anhadirCasilla(cas2);
        anhadirCasilla(cas1);
        this.numCasillas = 2;

    }

    /*
     * Constructor para cuando el grupo está formado por TRES CASILLAS:
     * Requiere como parámetros las tres casillas miembro y el color del grupo.
     */
    public Grupo(Casilla cas1, Casilla cas2, Casilla cas3, String colorGrupo) {
        this.colorGrupo = colorGrupo;
        this.miembros = new ArrayList<>();
        anhadirCasilla(cas3);
        anhadirCasilla(cas2);
        anhadirCasilla(cas1);
        this.numCasillas = 3;
    }

    /*
     * Método que añade una casilla al array de casillas miembro de un grupo.
     * Parámetro: casilla que se quiere añadir.
     */
    private void anhadirCasilla(Casilla miembro) {
        if (!this.miembros.contains(miembro)) {
            this.miembros.add(miembro);
            miembro.setGrupo(this);
        }
    }

    /*
     * Método que comprueba si el jugador pasado tiene en su haber todas las
     * casillas del grupo:
     * Parámetro: jugador que se quiere evaluar.
     * Valor devuelto: true si es dueño de todas las casillas del grupo, false en
     * otro caso.
     */
    public boolean esDuenhoGrupo(Jugador jugador) {
        boolean esDuenho = true;
        for (int i = 0; i < this.miembros.size(); i++) {
            if (miembros.get(i).getduenhoJugador() != jugador)
                esDuenho = false;
        }
        return esDuenho;
    }

    // GETTERS
    public String getColor() {
        return colorGrupo;
    }

    public int getNumCasillas() {
        return numCasillas;
    }

}
