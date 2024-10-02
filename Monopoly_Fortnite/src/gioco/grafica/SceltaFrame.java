package gioco.grafica;

import controllore.Controllore;

import javax.swing.*;
import java.awt.*;

public class SceltaFrame extends JFrame {
    /**
     * Costruttore. Crea il frame da cui scegliere
     * il tipo di interfaccia da usare
     * @param controllore controllore che gestisce il gioco
     */
    public SceltaFrame(Controllore controllore){
        Container container = getContentPane();

        container.setLayout(new GridLayout(2,1));
        container.add(new JLabel("Scegli la grafica: "));

        //pulsanti
        JPanel bottoni = new JPanel();
        bottoni.setLayout(new GridLayout(1,2));
        container.add(bottoni);
        JButton cli = new JButton("CLI");
        cli.setOpaque(false);
        cli.setContentAreaFilled(false);
        JButton gui = new JButton("GUI");
        gui.setOpaque(false);
        gui.setContentAreaFilled(false);

        //action listeners
        cli.addActionListener(e -> {
            dispose();
            controllore.startGrafica(new Cli(controllore));
        });
        gui.addActionListener(e -> {
            dispose();
            controllore.startGrafica(new StartFrame(controllore));
        });

        bottoni.add(cli);
        bottoni.add(gui);

        setSize(200,100);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (dim.getWidth()-getWidth())/2,(int) (dim.getHeight()-getHeight())/2);
        setVisible(true);
    }
}
