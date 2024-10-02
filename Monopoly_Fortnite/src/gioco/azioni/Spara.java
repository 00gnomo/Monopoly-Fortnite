package gioco.azioni;

import gioco.Gioco;
import gioco.giocatore.Giocatore;

import java.io.Serializable;
import java.util.ArrayList;

public class Spara implements Azione, Serializable {
    /**
     * Distrugge un muro o danneggia un giocatore presente sulla casella selezionata nel gioco.
     * Il giocatore perde tanti hp quanti sono i danni del giocatore che esegue l'azione
     * @param giocatore giocatore che esegue l'azione
     * @param gioco gioco contenuto nel controllore
     */
    @Override
    public void esegui(Giocatore giocatore, Gioco gioco){
        //stink bomb
           if(giocatore.ispezionaCarte("Stink Bomb")){
               for(Giocatore g : gioco.getGiocatori()){
                   if(g.getCasella().getX()==giocatore.getCasella().getX() && g.getCasella().getX()==0 || g.getCasella().getX()==8){
                       g.decreaseHp(3);
                   }
                   else if(g.getCasella().getY()==giocatore.getCasella().getY() && g.getCasella().getY()==0 || g.getCasella().getY()==8) {
                       g.decreaseHp(3);
                   }
               }
               gioco.getMazzoForzieri().add(giocatore.rimuoviCarta("Stink Bomb"));
           }
           if(gioco.getTmpCasella().getMuro()){
               gioco.getTmpCasella().setMuro(false);
           }
           else{
               try{
                   ArrayList<Giocatore> g = gioco.getTmpCasella().getGiocatori();
                   if(g.get(0).equals(giocatore)){
                       g.get(1).decreaseHp(giocatore.getDanni());
                   }
                   else{
                       g.get(0).decreaseHp(giocatore.getDanni());
                   }
               }catch(IndexOutOfBoundsException ignore){}
           }
    }

    /**
     * Ritorna il nome dell'azione eseguita
     * @return "Spara"
     */
    @Override
    public String toString(){
        return "Spara";
    }
}