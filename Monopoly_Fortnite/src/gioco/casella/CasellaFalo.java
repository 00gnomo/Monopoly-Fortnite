package gioco.casella;

import java.io.Serializable;

public class CasellaFalo extends Casella implements Serializable {
    /**
     * Costruttore
     * @param x riga del tabellone
     * @param y colonna del tabellone
     * @param posizione posizione partendo dalla casella Go
     */
    public CasellaFalo(int x, int y, int posizione) {
        super("Falo'", x,  y,posizione);
    }
}
