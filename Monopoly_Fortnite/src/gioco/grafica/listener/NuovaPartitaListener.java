package gioco.grafica.listener;

import controllore.Controllore;
import gioco.grafica.*;

import javax.swing.*;
import java.awt.event.*;

public class NuovaPartitaListener implements ActionListener {
    private final JFrame frame;
    private final Grafica g;

    /**
     * Costruttore
     * @param frame frame da eliminare
     * @param controllore controllore che gestisce il gioco
     */
    public NuovaPartitaListener(JFrame frame, Controllore controllore){
        this.frame = frame;
        this.g = new NomiFrame(controllore);
    }

    /**
     * Fa iniziare una nuova partita
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        g.start();
        frame.dispose();
    }
}
