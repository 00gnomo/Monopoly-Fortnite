package gioco.grafica;

import controllore.Controllore;

import javax.swing.*;
import java.awt.*;

public class ExitFrame extends JFrame {
    /**
     * Costruttore. Crea un frame per scegliere
     * se salvare prima di uscire oppure no.
     * Chiude anche il frame della gui.
     * @param controllore controllore che gestisce il gioco
     * @param gui frame gui da chiudere
     */
    public ExitFrame(Controllore controllore, Gui gui){
        Container container = getContentPane();

        container.setLayout(new GridLayout(2,1));
        container.add(new JLabel("Exit"));

        //pulsanti
        JPanel bottoni = new JPanel();
        bottoni.setLayout(new GridLayout(1,2));
        container.add(bottoni);
        JButton salva = new JButton("Salva");
        salva.setOpaque(false);
        salva.setContentAreaFilled(false);
        JButton nonSalvare = new JButton("Non salvare");
        nonSalvare.setOpaque(false);
        nonSalvare.setContentAreaFilled(false);

        //action listeners
        salva.addActionListener(e -> {
            controllore.salvaPartita();
            gui.dispose();
            dispose();

        });
        nonSalvare.addActionListener(e -> {
            gui.dispose();
            dispose();
        });

        bottoni.add(salva);
        bottoni.add(nonSalvare);

        setSize(200,100);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (dim.getWidth()-getWidth())/2,(int) (dim.getHeight()-getHeight())/2);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}

