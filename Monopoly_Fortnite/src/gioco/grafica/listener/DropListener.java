package gioco.grafica.listener;

import controllore.Controllore;
import gioco.casella.*;
import gioco.grafica.*;

import javax.swing.*;
import java.awt.event.*;

public class DropListener implements ActionListener {
    private final Controllore controllore;
    private final Gui gui;
    private final TabelloneListener tabelloneListener;
    private boolean flag = true;
    private Casella casella;
    private int cont = 0;

    /**
     * Costruttore. Inizializza
     * il tabellone listener
     * @param controllore controllore che gestisce il gioco
     * @param gui GUI
     */
    public DropListener(Controllore controllore, Gui gui) {
        this.controllore = controllore;
        this.gui = gui;
        this.tabelloneListener = new TabelloneListener(controllore,gui);
    }

    /**
     * Se una casella città viene premuta, viene
     * assegnata come casella di partenza al giocatore
     * del turno corrente. Se viene premuto un altro tipo
     * di casella viene mostrato un messaggio d'errore.
     * Quando è stata assegnata una casella a tutti i
     * giocatori vengono cambiati i listener.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e){
        CasellaButton source = (CasellaButton) e.getSource();//salvo il bottone sorgente
        casella = source.getCasella();//salvo la casella appartenente al bottone sorgente

        if(flag){
            if(casella instanceof CasellaCitta){
                controllore.assegnaCasella(casella.getPosizione(),controllore.getGioco().getGiocatori().get(cont));
                cont++;
                gui.refreshTurno(cont%controllore.getGioco().getGiocatori().size());
                gui.refreshBoard();
            }
            else{
                JOptionPane.showMessageDialog(null,"La casella selezionata non è una città");
            }

            if(cont==controllore.getGioco().getGiocatori().size()){
                flag = false;
                switchListener();
                gui.cambiaDadi(true);
                gui.refreshToDoPanel("Girare i dadi");
            }
        }
    }

    /**
     * Getter della casella premuta
     * @return casella premuta
     */
    public Casella getCasella(){
        return this.casella;
    }

    /**
     * Rimuove tutti i DropListener, assegna
     * a tutte le caselle un tabelloneListener
     * e le attiva.
     */
    private void switchListener(){
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(i==0 || i==8 || j==0 || j==8){
                    gui.getTabellone()[i][j].removeActionListener(this);
                    gui.getTabellone()[i][j].addActionListener(tabelloneListener);
                    gui.getTabellone()[i][j].setEnabled(false);
                    gui.getTabellone()[i][j].setDisabledIcon(gui.getTabellone()[i][j].getIcon());
                }
            }
        }
        gui.setSelezioneCasella();
    }
}
