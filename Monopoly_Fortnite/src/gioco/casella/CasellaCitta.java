package gioco.casella;

import gioco.carte.Luogo;

import java.io.Serializable;

public class CasellaCitta extends Casella implements Serializable {
    private final Luogo luogo;

    /**
     * Costruttore
     * @param luogo Luogo associato alla casella
     * @param x riga del tabellone
     * @param y colonna del tabellone
     * @param posizione posizione partendo dalla casella Go
     */
    public CasellaCitta(int x, int y, int posizione, Luogo luogo) {
        super(luogo.getNome(),x,y,posizione);
        this.luogo = luogo;
    }

    /**
     * Getter del luogo
     * @return luogo
     */
    public Luogo getLuogo() {
        return luogo;
    }

    /**
     * Setter della tempesta a true. I danni del luogo
     * aumentano di 1
     */
    public void aggiungiTempesta(){
        super.aggiungiTempesta();
        luogo.increaseDanni();
    }
}