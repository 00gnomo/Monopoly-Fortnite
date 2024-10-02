package gioco.grafica;

import controllore.Controllore;
import gioco.grafica.listener.MoveListener;

import javax.swing.*;
import java.awt.*;

public class AzioneFrame extends JFrame implements Grafica {
    private final Controllore controllore;
    private final Gui gui;
    private final String action;
    private final int moves;

    /**
     * Costruttore
     * @param controllore controllore che gestisce il gioco
     * @param gui frame GUI
     * @param moves mosse che il giocatore deve fare
     * @param action nome della funzione da eseguire
     */
    public AzioneFrame(Controllore controllore, Gui gui, int moves, String action){
        this.controllore = controllore;
        this.gui = gui;
        this.moves = moves;
        this.action = action;
    }

    /**
     * Metodo che crea il frame per far procedere
     * il giocatore dopo aver tirato i dadi e dopo
     * la fine del turno
     */
    @Override
    public void start(){
        Container container = getContentPane();

        container.setLayout(new GridLayout(2,1));
        container.add(new JLabel("Muoviti di: "+ moves +"   Azione:"+ action));

        //pulsanti
        JPanel bottoni = new JPanel();
        bottoni.setLayout(new GridLayout(1,1));
        container.add(bottoni);

        JButton continua = new JButton("Continua il turno");
        continua.setOpaque(false);
        continua.setContentAreaFilled(false);

        //action listeners
        continua.addActionListener(new MoveListener(controllore,gui,this));

        bottoni.add(continua);

        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        setSize(400,200);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (dim.getWidth()-getWidth())/2,(int) (dim.getHeight()-getHeight())/2);
        setVisible(true);
    }

}
