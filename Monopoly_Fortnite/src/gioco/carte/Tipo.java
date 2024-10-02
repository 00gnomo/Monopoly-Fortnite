package gioco.carte;

import java.io.Serializable;

public enum Tipo implements Serializable {
    /**
     * Enum che divide le carte Forziere tra usa e getta e permanenti.
     * Le carte usa e getta vengono rimesse nel mazzo di gioco dopo
     * l'uso, le carte permanenti rimangono nell'inventario del giocatore
     * per tutta la partita.
     */
    PERMANENTI,USAGETTA
}
