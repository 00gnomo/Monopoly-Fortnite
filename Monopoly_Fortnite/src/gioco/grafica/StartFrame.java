package gioco.grafica;

import controllore.Controllore;
import gioco.grafica.fonts.Fonts;
import gioco.grafica.listener.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.Objects;

public class StartFrame extends JFrame implements Grafica {
    private final Controllore controllore;
    /**
     * Costruttore
     * @param controllore controllore che gestisce il gioco
     */
    public StartFrame(Controllore controllore){
        super("Monopoly di Fortnite");
        this.controllore=controllore;
    }

    /**
     * Metodo che crea il frame per scegliere
     * se iniziare una nuova partita o caricarne
     * una già iniziata
     */
    @Override
    public void start() {

        int BUTTON_HEIGHT, BUTTON_WIDTH;

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        Container c1 = this.getContentPane();
        this.setVisible(true);

        controllore.playAudio("/audio/theme.wav");

        Dimension screenSize = getSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        BUTTON_HEIGHT=screenHeight/12;
        BUTTON_WIDTH=screenWidth/9;

        JPanel sfondoPanel = new JPanel();

        //IMMAGINE DI SFONDO----------------------------------------------------------------------------------------------------------
        ImageIcon imageIconSfondo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/starting_screen.jpg")));
        Image imageS = imageIconSfondo.getImage(); // Ottieni l'oggetto Image dall'ImageIcon
        Image newImageS = imageS.getScaledInstance(screenWidth, screenHeight, Image.SCALE_SMOOTH); // Ridimensiona l'immagine
        imageIconSfondo = new ImageIcon(newImageS); // Crea un nuovo ImageIcon con l'immagine ridimensionata
        JLabel sfondoLabel = new JLabel(imageIconSfondo);
        //IMMAGINE DI SFONDO----------------------------------------------------------------------------------------------------------

        sfondoPanel.add(sfondoLabel);//aggiungo lo sfondo allo sfondoPanel

        //TITOLO-----------------------------------------------------
        JPanel titlePanel=new JPanel(new GridLayout(1,1));
        titlePanel.setBounds(screenWidth/8,screenHeight/2-BUTTON_HEIGHT/2*7,BUTTON_WIDTH*2+10,BUTTON_HEIGHT+300);
        titlePanel.setOpaque(false);
        sfondoLabel.add(titlePanel);

        JPanel titoloPanel=new JPanel();
        titoloPanel.setBounds(screenWidth/8,screenHeight/3,screenWidth/4,300);
        titoloPanel.setOpaque(false);
        sfondoLabel.add(titoloPanel);

        JLabel titoloLabel=new JLabel("MONOPOLY FORTNITE");
        titoloLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        titoloLabel.setFont(Fonts.getFont((float) screenWidth/32));
        titoloLabel.setForeground(Color.white);
        titlePanel.add(titoloLabel);
        //TITOLO-----------------------------------------------------

        //LABEL PER BOTTONI-----------------------------------------------------------------------------------------------------------
        JPanel caricaPartitaPanel=new JPanel(new GridLayout(1,1));
        JPanel nuovaPartitaPanel=new JPanel(new GridLayout(1,1));
        sfondoLabel.add(caricaPartitaPanel);
        sfondoLabel.add(nuovaPartitaPanel);

        caricaPartitaPanel.setBounds(screenWidth/8,screenHeight/2-BUTTON_HEIGHT/2,BUTTON_WIDTH,BUTTON_HEIGHT);
        caricaPartitaPanel.setOpaque(false);

        nuovaPartitaPanel.setBounds(screenWidth/8+BUTTON_WIDTH+10,screenHeight/2-BUTTON_HEIGHT/2,BUTTON_WIDTH,BUTTON_HEIGHT);
        nuovaPartitaPanel.setOpaque(false);
        //LABEL PER BOTTONI-----------------------------------------------------------------------------------------------------------

        //BOTTONI E LE LORO PROPRIETA'------------------------------------
        JButton caricaPartita=new JButton("CARICA PARTITA");
        caricaPartita.setFont(Fonts.getFont((float) screenWidth/68));
        caricaPartita.setBorder(new EtchedBorder());
        caricaPartita.setBackground(Color.ORANGE);
        caricaPartita.setForeground(Color.DARK_GRAY);

        JButton nuovaPartita=new JButton("NUOVA PARTITA");
        nuovaPartita.setFont(Fonts.getFont((float) screenWidth/68));
        nuovaPartita.setBorder(new EtchedBorder());
        nuovaPartita.setBackground(Color.ORANGE);
        nuovaPartita.setForeground(Color.DARK_GRAY);

        nuovaPartita.addActionListener(new NuovaPartitaListener(this,controllore));
        caricaPartita.addActionListener(new CaricaPartitaListener(controllore,this));
        //BOTTONI E LE LORO PROPRIETA'------------------------------------

        caricaPartitaPanel.add(caricaPartita);
        nuovaPartitaPanel.add(nuovaPartita);

        c1.add(sfondoPanel);

        revalidate();
        repaint();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
