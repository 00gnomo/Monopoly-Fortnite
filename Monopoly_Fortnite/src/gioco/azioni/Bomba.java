package gioco.azioni;

import gioco.Gioco;
import gioco.giocatore.Giocatore;

import java.io.Serializable;

public class Bomba implements Azione, Serializable {
    /**
     * Distrugge tutti i muri e toglie 1hp a tutti i giocatori, tranne al giocatore
     * che esegue l'azione e a quelli che hanno la carta Bush
     * @param giocatore giocatore che esegue l'azione
     * @param gioco gioco contenuto nel controllore
     */
    @Override
    public void esegui(Giocatore giocatore, Gioco gioco){
        for(Giocatore g : gioco.getGiocatori()){
            if(!g.equals(giocatore) && !g.ispezionaCarte("Bush")){
                g.decreaseHp(1);
            }
        }
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(i==0||i==8||j==0||j==8){
                    gioco.getTabellone()[i][j].setMuro(false);
                }
            }
        }
    }

    /**
     * Ritorna il nome dell'azione eseguita
     * @return "Bomba"
     */
    @Override
    public String toString(){
        return "Bomba";
    }
}
