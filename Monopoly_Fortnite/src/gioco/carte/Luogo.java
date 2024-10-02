package gioco.carte;

import gioco.giocatore.Giocatore;

import java.io.Serializable;
import java.awt.*;

public class Luogo implements Serializable {
    private final String nome;
    private Giocatore possessore;
    private int danni;
    private final Color color;

    /**
     * Costruttore
     * @param nome nome del luogo
     * @param danni hp tolti ai nemici che si fermano su questo luogo
     * @param color colore del luogo (possedere 2 lughi dello stesso colore fa guadagnare hp)
     */
    public Luogo(String nome, int danni, Color color) {
        this.nome = nome;
        this.danni = danni;
        this.color = color;
    }

    /**
     * Metodo che assegna un giocatore possessore a questo luogo
     * @param giocatore nuovo possessore del luogo
     */
    public void compra(Giocatore giocatore){
        this.possessore = giocatore;
    }

    /**
     * Getter del giocatore che possiede questo luogo
     * @return possessore del luogo
     */
    public Giocatore getPossessore() {
        return possessore;
    }

    /**
     * Getter del nome
     * @return nome del luogo
     */
    public String getNome() {
        return nome;
    }

    /**
     * Getter dei danni
     * @return danni
     */
    public int getDanni() {
        return danni;
    }

    /**
     * Aumenta di 1 i danni causati
     */
    public void increaseDanni(){
        this.danni++;
    }

    /**
     * Getter del colore
     * @return colore
     */
    public Color getColor() {
        return color;
    }
}
