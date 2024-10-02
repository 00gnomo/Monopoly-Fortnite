package gioco.grafica.listener;

import controllore.Controllore;
import gioco.giocatore.Giocatore;
import gioco.grafica.*;

import javax.swing.*;
import java.awt.event.*;

public class TabelloneListener implements ActionListener {
    private final Controllore controllore;
    private final Gui gui;

    /**
     * Controllore
     * @param controllore controllore che gestisce il gioco
     * @param gui GUI
     */
    public TabelloneListener(Controllore controllore, Gui gui){
        this.controllore = controllore;
        this.gui = gui;
    }

    /**
     * Mostra dei messaggi in base all'azione
     * da eseguire generata dal dado azioni
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e){
        int turno = controllore.getGioco().getTurno()%controllore.getGioco().getGiocatori().size();
        Giocatore giocatore = controllore.getGioco().getGiocatori().get(turno);
        //blocco tutti i bottoni tranne il tabellone mentre aspetto una casella valida, eseguo l'azione e sblocco tutto

        switch(controllore.getAzione().toString()){
            case "Spara":
                if(controllore.controllaCasellaSpara(((CasellaButton)e.getSource()).getCasella().getPosizione(),giocatore,true)){
                    controllore.getGioco().setTmpCasella(((CasellaButton)e.getSource()).getCasella());//setta la casella alla cui sparare
                    if(((CasellaButton) e.getSource()).getCasella().getMuro()){
                        gui.resetMuro(((CasellaButton) e.getSource()));//se c'è un muro lo toglie
                    }
                    controllore.eseguiAzione(giocatore);
                    for(Giocatore g :controllore.getGioco().getTmpCasella().getGiocatori()){
                        if(g.isMorto()){
                            controllore.getGioco().getGiocatori().remove(g);//se qualcuno muore lo toglie
                            controllore.getGioco().getTmpCasella().getGiocatori().remove(g);
                        }
                    }
                    //cambia lo stato dei bottoni del tabellone refresha la gui e fa visualizzare l'inventario
                    switchTabelloneButtons();
                    gui.refreshHealth();
                    new InventarioFrame(controllore,gui).start();
                    gui.refreshToDoPanel("Girare i dadi");
                }
                else{
                    JOptionPane.showMessageDialog(null,"La casella selezionata non è idonea all'azione,\nselezionare una casella con almeno un giocatore");
                }
                break;

            case "Muro":
                if(controllore.casellaPassata(((CasellaButton) e.getSource()).getCasella().getPosizione(), giocatore.getCasella().getPosizione())){
                    //setta il muro nella casella giusta
                    ((CasellaButton) e.getSource()).getCasella().setMuro(true);
                    gui.setMuro((CasellaButton) e.getSource());
                    switchTabelloneButtons();
                    gui.refreshHealth();
                    new InventarioFrame(controllore,gui).start();
                    gui.refreshToDoPanel("Girare i dadi");
                }
                else{
                    JOptionPane.showMessageDialog(null,"La casella selezionata non è valida per posizionare un muro,\nselezionare una delle caselle attraversate in questo turno");
                }
                break;
        }
    }

    /**
     * Disattiva i pulsanti del tabellone
     */
    private void switchTabelloneButtons(){
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(i==0 || i==8 || j==0 || j==8){
                    gui.getTabellone()[i][j].setEnabled(false);
                    gui.getTabellone()[i][j].setDisabledIcon(gui.getTabellone()[i][j].getIcon());
                }
            }
        }
    }
}
