package gioco.azioni;

import gioco.Gioco;
import gioco.giocatore.Giocatore;

import java.io.Serializable;

public class Muro implements Azione, Serializable {
    /**
     * Posiziona un muro sulla casella selezionata nel gioco
     * @param giocatore giocatore che posiziona il muro
     * @param gioco gioco contenuto nel controllore
     */
    @Override
    public void esegui(Giocatore giocatore, Gioco gioco){
        gioco.getTmpCasella().setMuro(true);
    }

    /**
     * Ritorna il nome dell'azione eseguita
     * @return "Muro"
     */
    @Override
    public String toString(){
        return "Muro";
    }
}
