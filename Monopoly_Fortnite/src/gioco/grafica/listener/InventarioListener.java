package gioco.grafica.listener;

import controllore.Controllore;
import gioco.carte.Forziere;
import gioco.giocatore.Giocatore;
import gioco.grafica.Gui;

import javax.swing.*;
import java.awt.event.*;
import java.util.ConcurrentModificationException;

public class InventarioListener implements ActionListener {
    private final Controllore controllore;
    private  final Gui gui;
    private final JFrame frame;

    /**
     * Costruttore
     * @param controllore controllore che gestisce il gioco
     * @param gui GUI
     * @param frame frame da eliminare
     */
    public InventarioListener(Controllore controllore, Gui gui, JFrame frame) {
        this.controllore = controllore;
        this.gui = gui;
        this.frame = frame;
    }

    /**
     * Esegue un'azione in base al nome
     * del JButton premuto.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Giocatore giocatore = controllore.getGioco().getGiocatori().get(controllore.getGioco().getTurno()%controllore.getGioco().getGiocatori().size());
        switch(((JButton) e.getSource()).getText()){
            //capisce che azione va svolta
            case "Chug Jug":
                frame.dispose();
                controllore.alterHp(giocatore,15);
                controllore.getGioco().getMazzoForzieri().add(giocatore.rimuoviCarta("Chug Jug"));
                JOptionPane.showMessageDialog(null,"Sei tornato a 15 hp!");
                controllore.getGioco().cambiaTurno();
                gui.refreshTurno(controllore.getGioco().getTurno()%controllore.getGioco().getGiocatori().size());
                gui.cambiaDadi(true);
                break;
            case "Medikit":
                frame.dispose();
                controllore.alterHp(giocatore,5);
                controllore.getGioco().getMazzoForzieri().add(giocatore.rimuoviCarta("Medikit"));
                JOptionPane.showMessageDialog(null,"Ti sono stati aggiunti 5 hp!");
                controllore.getGioco().cambiaTurno();
                gui.refreshTurno(controllore.getGioco().getTurno()%controllore.getGioco().getGiocatori().size());
                gui.cambiaDadi(true);
                break;
            case "Clinger":
                frame.dispose();
                Giocatore g = giocatore;
                while(g.equals(giocatore)){
                    g = controllore.getGioco().getGiocatori().get((int) (Math.random()*controllore.getGioco().getGiocatori().size()));
                }
                controllore.alterHp(g,-4);
                JOptionPane.showMessageDialog(null,"Giocatore colpito: " + g.getNome());
                gui.cambiaDadi(true);
                break;
        }
        Forziere f = giocatore.rimuoviCarta(((JButton) e.getSource()).getText());
        if(f!=null){
            controllore.usaCarta(giocatore,f);
        }
        
        try{
            gui.controllaMorti(controllore.getGioco().getGiocatori().get(controllore.getGioco().getTurno()%controllore.getGioco().getGiocatori().size()));
        }catch(ConcurrentModificationException ignore){}
    }
}
