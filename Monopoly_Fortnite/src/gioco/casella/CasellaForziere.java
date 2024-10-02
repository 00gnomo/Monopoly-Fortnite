package gioco.casella;

import java.io.Serializable;

public class CasellaForziere extends Casella implements Serializable {
    /**
     * Costruttore
     * @param x riga del tabellone
     * @param y colonna del tabellone
     * @param posizione posizione partendo dalla casella Go
     */
    public CasellaForziere(int x, int y, int posizione) {
        super("Forziere",x,y,posizione);
    }
}
