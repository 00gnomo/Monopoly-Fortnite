package gioco.grafica;

import controllore.Controllore;
import gioco.giocatore.Giocatore;
import gioco.grafica.fonts.Fonts;
import gioco.grafica.listener.*;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;

public class Gui extends JFrame implements Grafica {
    private final Controllore controllore;
    private final CasellaButton[][] tabellone;
    private final DropListener dropListener;
    private JLabel tabelloneLabel;
    private final JLabel turnoLabel, toDoLabel;
    private final JLabel[] elenco;
    private final JPanel infoPanel, exitPanel;
    private final Color[] colors;
    private JButton exit;
    private boolean selezioneCasella = true;
    private ActionListener exitListener;

    /**
     * Costruttore. Crea tutti i
     * label/panel contenuti nel frame
     * @param controllore controllore che gestisce il gioco
     */
    public Gui(Controllore controllore){
        super("Monopoly di Fortnite");
        this.controllore = controllore;
        this.tabellone = new CasellaButton[9][9];
        this.setUndecorated(true);
        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        this.dropListener = new DropListener(controllore,this);
        this.infoPanel = new JPanel(new GridLayout(controllore.getGioco().getGiocatori().size()+1,1));
        this.turnoLabel = new JLabel("Turno di: " + controllore.getGioco().getGiocatori().get(0).getNome());
        this.elenco = new JLabel[controllore.getGioco().getGiocatori().size()];
        this.colors = new Color[7];
        if(controllore.getGioco().getTurno()==0){
            this.toDoLabel = new JLabel("Scegliere la casella da cui partire");
        }
        else{
            this.toDoLabel = new JLabel("Gira i dadi");
            this.turnoLabel.setText("Turno di: "+ controllore.getGioco().getGiocatori().get(controllore.getGioco().getTurno()%controllore.getGioco().getGiocatori().size()).getNome());
        }
        this.exitPanel = new JPanel();
    }

    /**
     * Metodo che crea il frame con tabellone, dadi,
     * label per i messaggi e il pulsante per uscire.
     */
    @Override
    public void start() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); //massimizza la finestra
        Container c1 = this.getContentPane();
        this.setVisible(true);

        //DIMENSIONI-------------------------------------------------------------------------------
        Dimension screenSize = getSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        int dimensioneTabellone=screenWidth/2;
        //DIMENSIONI-------------------------------------------------------------------------------

        JPanel sfondoPanel = new JPanel(); //pannello di sfondo

        //IMPOSTA SFONDO---------------------------------------------------------------------------------------------------------
        ImageIcon imageIconSfondo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/sfondo.png")));
        Image imageS = imageIconSfondo.getImage(); // Ottieni l'oggetto Image dall'ImageIcon
        Image newImageS = imageS.getScaledInstance(screenWidth, screenHeight, Image.SCALE_SMOOTH); // Ridimensiona l'immagine
        imageIconSfondo = new ImageIcon(newImageS); // Crea un nuovo ImageIcon con l'immagine ridimensionata
        JLabel sfondoLabel = new JLabel(imageIconSfondo);
        //IMPOSTA SFONDO---------------------------------------------------------------------------------------------------------

        //GRIGLIA DI GIOCO-------------------------------------------
        tabelloneLabel = new JLabel();
        tabelloneLabel.setLayout(new GridLayout(9,9));
        //GRIGLIA DI GIOCO-------------------------------------------

        //AGGIUNTA DEI DIVERSI ELEMENTI-------------------------------------------------------------------------------------------------------------------
        sfondoPanel.add(sfondoLabel);
        sfondoLabel.add(tabelloneLabel);
        tabelloneLabel.setBounds(screenWidth/2-dimensioneTabellone/2,screenHeight/2-dimensioneTabellone/2,dimensioneTabellone,dimensioneTabellone);
        //AGGIUNTA DEI DIVERSI ELEMENTI-------------------------------------------------------------------------------------------------------------------

        c1.add(sfondoPanel); //aggiungo il pannello di sfondo al container

        creaTabellone(); //riempio il tabellone di bottoni

        //DEFINISCO LE PROPRIETA' DEI BOTTONI-------------------
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(i==0 || i==8 || j==0 || j==8){
                    tabellone[i][j].setOpaque(false);
                    tabellone[i][j].setContentAreaFilled(false);
                    tabellone[i][j].setForeground(Color.BLACK);
                }
            }
        }
        //DEFINISCO LE PROPRIETA' DEI BOTTONI-------------------

        //PANEL DELLE INFO---------------------------------------------------------------------------------------------------------------------------

        colors[0]=new Color(255, 0, 0);
        colors[1]=new Color(0, 157, 255);
        colors[2]=new Color(165, 77, 189);
        colors[3]=new Color(0, 175, 27);
        colors[4]=new Color(93, 0, 0);
        colors[5]=new Color(255, 128, 0);
        colors[6]=new Color(64, 0, 255);

        infoPanel.setBounds(5,5,screenWidth/5,(screenHeight/21)*(controllore.getGioco().getGiocatori().size()+1));

        sfondoLabel.add(infoPanel);
        infoPanel.setOpaque(false);

        for(int i=0;i<controllore.getGioco().getGiocatori().size();i++){
            elenco[i]=new JLabel();
            elenco[i].setFont(Fonts.getFont((float) screenWidth/40));
            elenco[i].setForeground(colors[i]);
            elenco[i].setText(controllore.getGioco().getGiocatori().get(i).getHp() +" -- "+ controllore.getGioco().getGiocatori().get(i).getNome());
            infoPanel.add(elenco[i]);
            elenco[i].setOpaque(false);
        }
        turnoLabel.setFont(Fonts.getFont((float) screenWidth/40));
        turnoLabel.setForeground(Color.WHITE);
        infoPanel.add(turnoLabel);
        //PANEL DELLE INFO---------------------------------------------------------------------------------------------------------------------------

        //  TO DO LABEL
        JPanel panel=new JPanel();
        panel.setBounds(screenWidth/2-(screenWidth/4),0,screenWidth/2,screenHeight/20);
        sfondoLabel.add(panel);
        panel.add(toDoLabel);
        //  TO DO LABEL

        exitPanel.setLayout(new GridLayout());
        exitPanel.setBounds(screenWidth-screenWidth/20,screenHeight-screenWidth/20,screenWidth/20,screenWidth/20);
        sfondoLabel.add(exitPanel);
        exitListener = e -> new ExitFrame(controllore, this);

        exit=new JButton("EXIT");
        exitPanel.add(exit);
        exit.addActionListener(exitListener);

        exit.setEnabled(controllore.getGioco().getTurno()!=0);

        revalidate();
        repaint();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Metodo che crea il tabellone, assegnando una casella ai
     * JButton, e i dadi di gioco.
     * Se l'utente ha caricato una partita salvata inizializza
     * tutti gli elementi in base alla situazione salvata.
     */
    private void creaTabellone(){
        int riga=0,colonna;
        int cont=0,imageCont=1;

        // se il contatore è dispari la casella è una città
        // se il contatore è pari la casella può essere: un falò, una chest, una trappola, il via, la prigione, il parcheggio o il vai in prigione

        //CREA IL TABELLONE------------------------------------------------------------------------
        for(colonna=0;colonna<9;colonna++){
            tabellone[riga][colonna] = new CasellaButton(controllore.getGioco().getTabellone()[riga][colonna]);
            if (cont%2!=0) {
                setImageCasella(tabellone[riga][colonna], imageCont);
                imageCont++;
            }
            cont++;
        }

        colonna=8;
        for(riga=1;riga<9;riga++){
            tabellone[riga][colonna] = new CasellaButton(controllore.getGioco().getTabellone()[riga][colonna]);
            if (cont%2!=0) {
                setImageCasella(tabellone[riga][colonna], imageCont);
                imageCont++;
            }
            cont++;
        }

        riga=8;
        for(colonna=7;colonna>=0;colonna--){
            tabellone[riga][colonna] = new CasellaButton(controllore.getGioco().getTabellone()[riga][colonna]);
            if (cont%2!=0) {
                setImageCasella(tabellone[riga][colonna], imageCont);
                imageCont++;
            }
            cont++;
        }

        colonna=0;
        for(riga=7;riga>0;riga--){
            tabellone[riga][colonna] = new CasellaButton(controllore.getGioco().getTabellone()[riga][colonna]);
            if (cont%2!=0) {
                setImageCasella(tabellone[riga][colonna], imageCont);
                imageCont++;
            }
            cont++;
        }

        tabellone[2][2]=new CasellaButton(null);
        tabellone[6][6]=new CasellaButton(null);
        tabellone[2][2].setVisible(false);
        tabellone[6][6].setVisible(false);

        tabellone[3][5]=new CasellaButton(null);
        tabellone[5][3]=new CasellaButton(null);
        ActionListener dadoListener =new DadoListener(tabellone[3][5],tabellone[5][3],tabelloneLabel,controllore,this);
        tabellone[5][3].addActionListener(dadoListener);
        tabellone[3][5].addActionListener(dadoListener);
        if(controllore.getGioco().getTurno()==0){
            cambiaDadi(false);
        }

        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(i==0 || i==8 || j==0 || j==8){
                    tabelloneLabel.add(tabellone[i][j]);
                    if(controllore.getGioco().getTurno()==0){

                        tabellone[i][j].addActionListener(dropListener);
                    }
                    else{
                        getTabellone()[i][j].addActionListener(new TabelloneListener(controllore,this));
                        getTabellone()[i][j].setDisabledIcon(getTabellone()[i][j].getIcon());
                    }
                }
                else if(i==2 && j==2 || i==6 && j==6 || i==3 && j==5 || i==5 && j==3 ){
                    tabelloneLabel.add(tabellone[i][j]);
                }
                else{
                    JButton vuoto=new JButton();
                    vuoto.setVisible(false);
                    tabelloneLabel.add(vuoto);
                }
            }
        }

        tabellone[0][0].setIcon(setImageCaselleExtra("caselle/go.png"));
        tabellone[0][8].setIcon(setImageCaselleExtra("caselle/jail.png"));
        tabellone[8][8].setIcon(setImageCaselleExtra("caselle/park.png"));
        tabellone[8][0].setIcon(setImageCaselleExtra("caselle/go_to_jail.png"));

        tabellone[0][2].setIcon(setImageCaselleExtra("caselle/campfire.png"));
        tabellone[2][8].setIcon(setImageCaselleExtra("caselle/campfire.png"));
        tabellone[8][6].setIcon(setImageCaselleExtra("caselle/campfire.png"));
        tabellone[6][0].setIcon(setImageCaselleExtra("caselle/campfire.png"));

        tabellone[0][6].setIcon(setImageCaselleExtra("caselle/trap.png"));
        tabellone[6][8].setIcon(setImageCaselleExtra("caselle/trap.png"));
        tabellone[8][2].setIcon(setImageCaselleExtra("caselle/trap.png"));
        tabellone[2][0].setIcon(setImageCaselleExtra("caselle/trap.png"));

        tabellone[0][4].setIcon(setImageCaselleExtra("caselle/chest.png"));
        tabellone[4][8].setIcon(setImageCaselleExtra("caselle/chest.png"));
        tabellone[8][4].setIcon(setImageCaselleExtra("caselle/chest.png"));
        tabellone[4][0].setIcon(setImageCaselleExtra("caselle/chest.png"));

        refreshBoard();
        //CREA IL TABELLONE------------------------------------------------------------------------
    }

    /**
     * Metodo che aggiorna il tabellone
     * in base a giocatori, muri e tempeste
     * presenti su ogni casella
     */
    public void refreshBoard(){
        ArrayList<Giocatore> list;
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(i==0 || i==8 || j==0 || j==8){
                    list = tabellone[i][j].getCasella().getGiocatori();
                    tabellone[i][j].getCasellaPanel().removeAll();
                    for(Giocatore g : list){
                        setPlayers(g.getPlayerIcon(),tabellone[i][j],list.size());
                    }
                }
            }
        }
        revalidate();
        repaint();
    }

    /**
     * Metodo che assegna a un JButton l'immagine
     * della casella città corrispondente
     * @param button JButton a cui assegnare l'immagine
     * @param cont contatore che tiene conto delle immagini luogo già inserite
     */
    private void setImageCasella(JButton button, int cont){
        String string="location_"+cont+".png"; //stringa composta da: "location"(così sappiamo che è un luogo) + "_" + numero(usato per l'inserimento ordinato nella matrice)
        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/caselle/"+string)));
        Image image = imageIcon.getImage(); // Ottieni l'oggetto Image dall'ImageIcon
        Image newImage = image.getScaledInstance(tabelloneLabel.getWidth()/9, tabelloneLabel.getHeight()/9, Image.SCALE_SMOOTH); // Ridimensiona l'immagine
        imageIcon = new ImageIcon(newImage); // Crea un nuovo ImageIcon con l'immagine ridimensionata
        button.setIcon(imageIcon);
        button.setIconTextGap(0);
    }

    /**
     * Metodo che crea un'icona a partire dal percorso di un'immagine
     * @param string percorso dell'immagine
     * @return icona
     */
    public ImageIcon setImageCaselleExtra(String string){
        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/"+string)));
        Image image = imageIcon.getImage(); // Ottieni l'oggetto Image dall'ImageIcon
        Image newImage = image.getScaledInstance(tabelloneLabel.getWidth()/9, tabelloneLabel.getHeight()/9, Image.SCALE_SMOOTH); // Ridimensiona l'immagine
        imageIcon = new ImageIcon(newImage); // Crea un nuovo ImageIcon con l'immagine ridimensionata
        return imageIcon;
    }

    /**
     * Metodo che aggiunge le immagini tempesta sulle caselle
     * raggiunte da essa e aggiorna il label della vita.
     * @param turno turno di gioco
     */
    public void refreshTurno(int turno){
        try{
            if(turno==0){
                controllaMorti(controllore.getGioco().getGiocatori().get(0));
            }
            else{
                controllaMorti(controllore.getGioco().getGiocatori().get((turno-1)%controllore.getGioco().getGiocatori().size()));
            }
        }catch(ConcurrentModificationException ignore){}

        turnoLabel.setText("Turno di: " + controllore.getGioco().getGiocatori().get(turno).getNome());
        refreshHealth();

        if(controllore.getGioco().getGiocatori().get(turno).getContTurni()>0){
            JOptionPane.showMessageDialog(null,"Sei in prigione. Paghi 2hp per uscire");
            controllore.alterHp(controllore.getGioco().getGiocatori().get(turno),-2);
            controllore.getGioco().getGiocatori().get(turno).resetContTurni();
            cambiaDadi(true);

            try{
                controllaMorti(controllore.getGioco().getGiocatori().get(turno));
            }catch(ConcurrentModificationException ignore){}
        }

        refreshBoard();
        refreshHealth();

        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(i==0 || i==8 || j==0 || j==8){
                    if(tabellone[i][j].getCasella().getTempesta()){
                        tabellone[i][j].setIcon(setImageCaselleExtra("caselle/storm.png"));
                    }
                }
            }
        }
        if(!selezioneCasella && controllore.getGioco().getTurno()!=controllore.getGioco().getGiocatori().size()){
            exit.setEnabled(true);
            exit.setBackground(Color.GREEN);
        }
        revalidate();
        repaint();
    }

    /**
     * Aggiorna il label che indica
     * la vita di ogni giocatore.
     */
    public void refreshHealth(){
        for (JLabel jLabel : elenco) {
            jLabel.setText("");
        }
        for(int i=0;i<controllore.getGioco().getGiocatori().size();i++){
            elenco[i].setForeground(colors[i] = switch (controllore.getGioco().getGiocatori().get(i).getCont()){
                case 1 -> new Color(255, 0, 0);
                case 2 -> new Color(0, 157, 255);
                case 3 -> new Color(165, 77, 189);
                case 4 -> new Color(0, 175, 27);
                case 5 -> new Color(93, 0, 0);
                case 6 -> new Color(255, 128, 0);
                case 7 -> new Color(64, 0, 255);
                default -> Color.WHITE;
            });
            elenco[i].setText(controllore.getGioco().getGiocatori().get(i).getHp()+" -- "+controllore.getGioco().getGiocatori().get(i).getNome());
        }
        revalidate();
        repaint();
    }

    /**
     * Aggiorna l'immagine dei giocatori in una casella
     * in base al loro numero
     * @param imageIcon icon
     * @param button JButton della casella
     * @param playerNum numero di giocatori su quella casella
     */
    private void setPlayers(ImageIcon imageIcon, CasellaButton button,int playerNum){
        if(playerNum>5){
            playerNum = 5;
        }
        Image image = imageIcon.getImage(); // Ottieni l'oggetto Image dall'ImageIcon
        Image newImage = image.getScaledInstance((tabelloneLabel.getWidth()/9)/(playerNum+1), (tabelloneLabel.getHeight()/9)/(playerNum+1), Image.SCALE_SMOOTH); // Ridimensiona l'immagine
        imageIcon = new ImageIcon(newImage); // Crea un nuovo ImageIcon con l'immagine ridimensionata
        JLabel icon = new JLabel();
        icon.setIcon(imageIcon);
        button.addPlayerImage(icon);
    }

    /**
     * Aggiunge un muro su una casella
     * @param casella casella su cui aggiungere il muro
     */
    public void setMuro(CasellaButton casella){
        casella.setBorder(new MatteBorder(5,0,0,0,new Color(188, 30, 1)));

        repaint();
        revalidate();
    }

    /**
     * Rimuove il muro da una casella
     * @param casella casella da cui rimuovere il muro
     */
    public void resetMuro(CasellaButton casella){
        casella.setBorder(new MatteBorder(0,0,0,0,new Color(188, 30, 1)));
        repaint();
        revalidate();
    }

    /**
     * Aggiorna il panel dei messaggi per l'utente
     * @param string messaggio da mostrare
     */
    public void refreshToDoPanel(String string){
        toDoLabel.setText(string);
    }

    /**
     * Attiva o disattiva i JButton dei dadi
     * @param b true = attiva i JButton, false = li disattiva
     */
    public void cambiaDadi(boolean b){
        tabellone[5][3].setEnabled(b);
        tabellone[3][5].setEnabled(b);
        tabellone[3][5].setDisabledIcon(tabellone[3][5].getIcon());
        tabellone[5][3].setDisabledIcon(tabellone[5][3].getIcon());
    }

    /**
     * Getter della matrice di JButton tabellone
     * @return matrice tabellone
     */
    public CasellaButton[][] getTabellone() {
        return tabellone;
    }

    /**
     * Controlla se qualche giocatore è morto.
     * Se il giocatore del turno corrente è morto
     * passa al turno successivo.
     * Controlla anche se un giocatore ha vinto
     * @param corrente giocatore del turno corrente
     */
    public void controllaMorti(Giocatore corrente) {
        for (Giocatore g : controllore.getGioco().getGiocatori()) {
            if (controllore.eliminaGiocatore(g)) {
                JOptionPane.showMessageDialog(null, g.getNome() + " è morto");
                refreshBoard();
                refreshHealth();
            }
            if (corrente.isMorto()) {
                controllore.getGioco().cambiaTurno();
                if(controllore.getVincitore()!=null){
                    refreshTurno(controllore.getGioco().getTurno() % controllore.getGioco().getGiocatori().size());
                }
                cambiaDadi(true);
            }
            else{
                cambiaDadi(true);
            }
            if(controllore.getVincitore()!=null){
                refreshToDoPanel(controllore.getVincitore().getNome().toUpperCase() + " HA VINTO");
                cambiaDadi(false);
                exit.setEnabled(true);
                exit.setBackground(Color.GREEN);
                exit.removeActionListener(exitListener);
                exit.addActionListener(e -> {
                    controllore.eliminaPartita();
                    dispose();
                });
            }
        }
    }

    /**
     * Getter del JButton per uscire dal programma
     * @return il JButton d'uscita
     */
    public JButton getExitButton(){
        return exit;
    }

    /**
     * Setter del boolean selezioneCasella a false. Indica
     * che la fase di scelta della casella di partenza
     * è terminata.
     */
    public void setSelezioneCasella() {
        selezioneCasella = false;
    }
}
