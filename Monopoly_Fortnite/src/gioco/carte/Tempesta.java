package gioco.carte;

import java.io.Serializable;

public class Tempesta implements Serializable {
    private final String funzione;

    /**
     * Costruttore
     * @param funzione Nome della carta tempesta che indica la sua funzione
     */
    public Tempesta(String funzione) {
        this.funzione = funzione;
    }

    /**
     * Getter della funzione della carta
     * @return funzione
     */
    public String getFunzione() {
        return funzione;
    }
}
