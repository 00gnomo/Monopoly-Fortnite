package gioco.grafica.listener;

import controllore.Controllore;
import gioco.grafica.*;

import javax.swing.*;
import java.awt.event.*;

public class CaricaPartitaListener implements ActionListener {
    private final Controllore controllore;
    private final StartFrame frame;

    /**
     * Costruttore
     * @param controllore controllore che gestisce il gioco
     * @param frame frame da eliminare
     */
    public CaricaPartitaListener(Controllore controllore, StartFrame frame){
        this.controllore=controllore;
        this.frame = frame;
    }

    /**
     * Quando il pulsante viene premuto prova a caricare
     * una partita salvata. Se non riesce inizia una
     * nuova partita
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(!controllore.caricaPartita()){
            JOptionPane.showMessageDialog(null,"Partita salvata non trovata. Inizia una nuova partita");
        }
        else{
            Gui g = new Gui(controllore);
            g.start();
            frame.dispose();
        }
    }
}
