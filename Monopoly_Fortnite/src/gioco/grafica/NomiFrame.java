package gioco.grafica;

import controllore.Controllore;
import gioco.grafica.fonts.Fonts;
import gioco.grafica.listener.NomiInputListener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.Objects;

public class NomiFrame extends JFrame implements Grafica{
    private final Controllore controllore;

    /**
     * Costruttore
     * @param controllore controllore che gestisce il gioco
     */
    public NomiFrame(Controllore controllore){
        super();
        this.controllore=controllore;
    }

    /**
     * Metodo che crea il frame per inserire
     * i nomi dei giocatori
     */
    @Override
    public void start() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        Container c1 = this.getContentPane();
        this.setVisible(true);

        Dimension screenSize = getSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        int signInPanelHeight=screenHeight/2;
        int signInPanelWidth=screenWidth/4;

        JPanel sfondoPanel = new JPanel();

        //IMMAGINE DI SFONDO----------------------------------------------------------------------------------------------------------
        ImageIcon imageIconSfondo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/starting_screen.jpg")));
        Image imageS = imageIconSfondo.getImage(); // Ottieni l'oggetto Image dall'ImageIcon
        Image newImageS = imageS.getScaledInstance(screenWidth, screenHeight, Image.SCALE_SMOOTH); // Ridimensiona l'immagine
        imageIconSfondo = new ImageIcon(newImageS); // Crea un nuovo ImageIcon con l'immagine ridimensionata
        JLabel sfondoLabel = new JLabel(imageIconSfondo);
        //IMMAGINE DI SFONDO----------------------------------------------------------------------------------------------------------

        sfondoPanel.add(sfondoLabel);//aggiungo lo sfondo allo sfondoPanel

        //PANEL REGISTRAZIONI--------------------------------------------------------
        JPanel signInPanel=new JPanel(new GridLayout(9,1));
        signInPanel.setBounds(screenWidth/8,screenHeight/2-signInPanelHeight/2,signInPanelWidth,signInPanelHeight);
        signInPanel.setOpaque(false);
        //PANEL REGISTRAZIONI--------------------------------------------------------

        //LABEL "INSERIRE I NOMI"------------------------------------
        JLabel title=new JLabel("INSERIRE I NOMI");
        title.setForeground(Color.WHITE);
        title.setFont(Fonts.getFont((float) screenWidth/32));
        title.setOpaque(false);

        signInPanel.add(title);
        //LABEL "INSERIRE I NOMI"------------------------------------

        //JTETXFIELD PER L'INSERIMENTO--------------------------------------------------------
        JTextField[] nomi=new JTextField[7];

        for(int i=0;i<7;i++){
            nomi[i]=new JTextField();
            nomi[i].setBackground(new Color(104,147,238));
            nomi[i].setFont(Fonts.getFont((float) screenWidth/32));
            nomi[i].setBorder(new EtchedBorder());
            nomi[i].setForeground(Color.WHITE);
            signInPanel.add(nomi[i]);
        }
        //JTETXFIELD PER L'INSERIMENTO--------------------------------------------------------

        //JBUTTON PER LA CONFERMA-------------------------------------------------------------
        JButton conferma=new JButton("CONFERMA");
        conferma.setFont(Fonts.getFont((float) screenWidth/40));
        conferma.setBackground(Color.ORANGE);
        conferma.setForeground(Color.DARK_GRAY);
        conferma.setBorder(new EtchedBorder());
        NomiInputListener listener=new NomiInputListener(nomi,this,controllore);
        conferma.addActionListener(listener);
        signInPanel.add(conferma);
        //JBUTTON PER LA CONFERMA-------------------------------------------------------------

        sfondoLabel.add(signInPanel);


        c1.add(sfondoPanel);

        revalidate();
        repaint();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
