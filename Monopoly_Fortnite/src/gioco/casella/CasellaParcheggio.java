package gioco.casella;

import java.io.Serializable;

public class CasellaParcheggio extends Casella implements Serializable {
    /**
     * Costruttore
     */
    public CasellaParcheggio() {
        super("Parcheggio",8,8,16);
    }
}