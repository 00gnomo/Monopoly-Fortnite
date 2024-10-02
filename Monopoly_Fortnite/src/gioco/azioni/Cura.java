package gioco.azioni;

import gioco.Gioco;
import gioco.giocatore.Giocatore;

import java.io.Serializable;

public class Cura implements Azione, Serializable {
    /**
     * Aumenta di 2hp la vita del giocatore
     * @param giocatore giocatore a cui aumentare la vita
     * @param gioco gioco contenuto nel controllore
     */
    @Override
    public void esegui(Giocatore giocatore, Gioco gioco){
            giocatore.increaseHp(2);
    }

    /**
     * Ritorna il nome dell'azione eseguita
     * @return "Cura"
     */
    @Override
    public String toString(){
        return "Cura";
    }
}
