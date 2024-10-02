package gioco.grafica;

import gioco.casella.Casella;

import javax.swing.*;

public class CasellaButton extends JButton {
    private final Casella casella;
    private final JPanel casellaPanel;

    /**
     * Costruttore. Crea il panel a cui aggiungere
     * le icone dei giocatori
     * @param casella casella associata al puslante
     */
    public CasellaButton(Casella casella) {
        this.casella = casella;
        this.casellaPanel = new JPanel();
        this.casellaPanel.setOpaque(false);
        this.add(casellaPanel);
    }

    /**
     * Getter della casella associata
     * @return casella
     */
    public Casella getCasella() {
        return casella;
    }

    /**
     * Aggiunge un giocatore al panel
     * dei giocatori
     * @param label label del giocatore da aggiungere
     */
    public void addPlayerImage(JLabel label){
        casellaPanel.add(label);
    }

    /**
     * Getter del panel dei giocatori
     * @return panel dei giocatori
     */
    public JPanel getCasellaPanel() {
        return casellaPanel;
    }
}
