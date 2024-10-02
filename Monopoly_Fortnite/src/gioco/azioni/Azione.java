package gioco.azioni;

import gioco.Gioco;
import gioco.giocatore.Giocatore;

public interface Azione{
    /**
     * Esegue un'azione tra le 4 del dado azioni
     * @param giocatore giocatore che esegue l'azione
     * @param gioco gioco contenuto nel controllore
     */
    void esegui(Giocatore giocatore, Gioco gioco);

    /**
     * Ritorna il nome dell'azione eseguita
     * @return nome azione
     */
    @Override
    String toString();
}
