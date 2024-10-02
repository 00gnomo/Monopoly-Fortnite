package gioco.grafica.listener;

import controllore.Controllore;
import gioco.grafica.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class DadoListener implements ActionListener {
    private final JButton dado, dadoAzione;
    private final JLabel tabelloneLabel;
    private final Controllore controllore;
    private final Gui gui;

    /**
     * Costruttore
     * @param dado pulsante dado numerico a cui è collegato il listener
     * @param dadoAzione pulsante dado azione a cui è collegato il listener
     * @param tabelloneLabel label contenente il tabellone
     * @param controllore controllore che gestisce il gioco
     * @param gui GUI
     */
    public DadoListener(JButton dado, JButton dadoAzione, JLabel tabelloneLabel, Controllore controllore, Gui gui){
        this.dado = dado;
        this.dadoAzione = dadoAzione;
        this.tabelloneLabel = tabelloneLabel;
        this.controllore = controllore;
        this.gui = gui;
    }

    /**
     * Quando uno dei 2 pulsanti viene premuto modifica
     * le loro icone, disattiva questi e il pulsante d'uscita
     * e crea un AzioneFrame
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        int normalDice = controllore.rollDice();
        String actionDice = controllore.rollActionDice().toString();
        setImage(normalDice,actionDice);
        gui.cambiaDadi(false);
        gui.getExitButton().setEnabled(false);
        gui.getExitButton().setBackground(Color.WHITE);

        AzioneFrame azioneFrame = new AzioneFrame(controllore, gui, normalDice, actionDice);
        azioneFrame.start();
    }

    /**
     * Modifica le icone dei JButton dei dadi
     * in base al numero e all'azione
     * ricevuti
     * @param normalDice numero generato dal dado numerico
     * @param actionDice nome dell'azione generata dal dado azione
     */
    private void setImage(int normalDice,String actionDice){
        String string="normal_dice_"+normalDice+".png"; //stringa composta da: "location"(così sappiamo che è un luogo) + "_" + numero(usato per l'inserimento ordinato nella matrice)
        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/dado/"+string)));
        Image image = imageIcon.getImage(); // Ottieni l'oggetto Image dall'ImageIcon
        Image newImage = image.getScaledInstance(tabelloneLabel.getWidth()/9, tabelloneLabel.getHeight()/9, Image.SCALE_SMOOTH); // Ridimensiona l'immagine
        imageIcon = new ImageIcon(newImage); // Crea un nuovo ImageIcon con l'immagine ridimensionata
        dado.setIcon(imageIcon);
        dado.setIconTextGap(0);

        string="action_dice_"+actionDice+".png"; //stringa composta da: "location"(così sappiamo che è un luogo) + "_" + numero(usato per l'inserimento ordinato nella matrice)
        imageIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/dado/"+string)));
        image = imageIcon.getImage(); // Ottieni l'oggetto Image dall'ImageIcon
        newImage = image.getScaledInstance(tabelloneLabel.getWidth()/9, tabelloneLabel.getHeight()/9, Image.SCALE_SMOOTH); // Ridimensiona l'immagine
        imageIcon = new ImageIcon(newImage); // Crea un nuovo ImageIcon con l'immagine ridimensionata
        dadoAzione.setIcon(imageIcon);
        dadoAzione.setIconTextGap(0);
    }
}
