package gioco.grafica.listener;

import controllore.Controllore;
import gioco.carte.Forziere;
import gioco.casella.*;
import gioco.giocatore.Giocatore;
import gioco.grafica.*;

import javax.swing.*;
import java.awt.event.*;
import java.util.ConcurrentModificationException;

public class MoveListener implements ActionListener {
    private final Controllore controllore;
    private final Gui gui;
    private final JFrame frame;
    private final CasellaButton[] buttons;

    /**
     * Costruttore
     * @param controllore controllore che gestisce il gioco
     * @param gui GUI
     * @param frame frame da eliminare
     */
    public MoveListener(Controllore controllore, Gui gui, JFrame frame){
        this.controllore = controllore;
        this.gui = gui;
        this.frame = frame;
        this.buttons = new CasellaButton[32];
    }

    /**
     * Esegue un'azione in base alla casella
     * raggiunta dal giocatore.
     * Chiede una casella se l'azione da eseguire
     * è Spara o Muro
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        int cont=0;
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(i==0 || i==8 || j==0|| j==8){
                    buttons[cont] = gui.getTabellone()[i][j];
                    cont++;
                }
            }
        }

        Giocatore giocatore = controllore.getGioco().getGiocatori().get(controllore.getGioco().getTurno()%controllore.getGioco().getGiocatori().size());
        frame.dispose();
        controllore.muoviGiocatore(giocatore);

        if(giocatore.getCasella() instanceof CasellaFalo && !giocatore.getCasella().getTempesta()){//falò senza tempesta allora si cura
            gui.refreshBoard();
            controllore.alterHp(giocatore,1);
            gui.refreshHealth();
            JOptionPane.showMessageDialog(null,giocatore.getNome()+" + 1 hp: "+giocatore.getHp());
        }
        else if(giocatore.getCasella() instanceof CasellaFalo && giocatore.getCasella().getTempesta()){//falò con tempesta prende danno
            gui.refreshBoard();
            controllore.alterHp(giocatore,-1);
            try{
                gui.controllaMorti(giocatore);
            }catch(ConcurrentModificationException ignore){}
            gui.refreshHealth();
        }

        if(giocatore.getCasella() instanceof CasellaTrappola){//trappola prende danno
            gui.refreshBoard();
            controllore.alterHp(giocatore,-1);
            try{
                gui.controllaMorti(giocatore);
            }catch(ConcurrentModificationException ignore){}
            gui.refreshHealth();
            JOptionPane.showMessageDialog(null,giocatore.getNome()+" - 1 hp: "+giocatore.getHp());
        }

        else if(giocatore.getCasella() instanceof CasellaForziere){//forziere pesca
            gui.refreshBoard();
            Forziere f = controllore.pescaForziere(giocatore);
            JOptionPane.showMessageDialog(null,"Hai pescato: "+ f.getFunzione());
        }
        else if(giocatore.getCasella() instanceof CasellaVaiInPrigione && !giocatore.getCasella().getTempesta()){
            //vai in prigione
            controllore.getGioco().getCasellaInPosizione(24).rimuoviGiocatore(giocatore);
            controllore.assegnaCasella(8,giocatore);
            giocatore.incrementaContTurni();
            gui.refreshBoard();
        }
        else if(giocatore.getCasella() instanceof CasellaVaiInPrigione && giocatore.getCasella().getTempesta()){
            //non vai in prigione ma prendi danno
            controllore.alterHp(giocatore,-1);

            //controllo morti
            try{
                gui.controllaMorti(giocatore);
            }catch(ConcurrentModificationException ignore){}
            gui.refreshBoard();
        }
        else if(giocatore.getCasella().getMuro()){//se c'è il muro dovrebbe toglierlo
            gui.resetMuro(gui.getTabellone()[giocatore.getCasella().getX()][giocatore.getCasella().getY()]);
        }
        else if(giocatore.getCasella().getTempesta()){
            if(giocatore.getCasella() instanceof CasellaCitta){
                controllore.alterHp(giocatore,-(((CasellaCitta) giocatore.getCasella()).getLuogo().getDanni())+1);
            }
            else{
                controllore.alterHp(giocatore,-1);
            }
            try{
                gui.controllaMorti(giocatore);
            }catch(ConcurrentModificationException ignore){}
            if(controllore.getVincitore()!=null) return;
        }
        else{
            gui.refreshBoard();
        }

        //via
        if(controllore.casellaPassata(0,giocatore.getCasella().getPosizione()) && !controllore.getGioco().getTabellone()[0][0].getTempesta()) {
            controllore.alterHp(giocatore,2);
            setTempesta();
            JOptionPane.showMessageDialog(null,"hai pescato una carta tempesta");
        }
        else if(controllore.casellaPassata(0,giocatore.getCasella().getPosizione()) && controllore.getGioco().getTabellone()[0][0].getTempesta()){
            setTempesta();
            JOptionPane.showMessageDialog(null,"hai pescato una carta tempesta");
        }

        //azione
        switch (controllore.getAzione().toString()){
            case "Spara":
                if(controllore.rigaVuota(giocatore)){
                    JOptionPane.showMessageDialog(null,"Non puoi sparare a nessuno,\nla tua linea di tiro è vuota");
                    new InventarioFrame(controllore,gui).start();
                    //lascio scorrere il gioco
                }
                else{
                    //lascio che il giocatore selezioni una casella a cui sparare
                    gui.refreshToDoPanel("Selezionare una casella contenete almeno un giocatore a cui sparare");
                    switchTabelloneButtons();
                }
                break;

            case "Muro":
                gui.refreshToDoPanel("Selezionare una casella sulla quale posizionare un muro");
                switchTabelloneButtons();
                break;

            default:
                controllore.eseguiAzione(giocatore);
                new InventarioFrame(controllore,gui).start();
                break;
        }
    }

    /**
     * Abilita i pulsanti
     * delle caselle
     */
    private void switchTabelloneButtons(){
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(i==0 || i==8 || j==0 || j==8){
                    gui.getTabellone()[i][j].setEnabled(true);
                    gui.getTabellone()[i][j].setDisabledIcon(gui.getTabellone()[i][j].getIcon());
                }
            }
        }
    }

    /**
     * Posiziona la tempesta su
     * una casella casuale
     */
    private void setTempesta(){
        int index = (int) (Math.random()*32);

        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(buttons[index].equals(gui.getTabellone()[i][j])){
                    gui.getTabellone()[i][j].getCasella().aggiungiTempesta();
                }
            }
        }
    }
}
