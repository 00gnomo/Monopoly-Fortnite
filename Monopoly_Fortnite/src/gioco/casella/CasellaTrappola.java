package gioco.casella;

import java.io.Serializable;

public class CasellaTrappola extends Casella implements Serializable {
    /**
     * Costruttore
     * @param x riga del tabellone
     * @param y colonna del tabellone
     * @param posizione posizione partendo dalla casella Go
     */
    public CasellaTrappola(int x, int y, int posizione) {
        super("Trappola",x,y,posizione);
    }
}
