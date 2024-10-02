package gioco.casella;

import gioco.carte.*;
import gioco.giocatore.Giocatore;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Casella implements Serializable {
    private final String nome;
    private final int x, y, posizione;
    private boolean muro, tempesta;
    private ArrayList<Forziere> inventario;
    private final ArrayList<Giocatore> giocatori;

    /**
     * Costruttore
     * @param nome nome della casella
     * @param x riga del tabellone
     * @param y colonna del tabellone
     * @param posizione posizione partendo dalla casella Go
     */
    public Casella(String nome, int x, int y, int posizione) {
        this.nome = nome;
        this.x = x;
        this.y = y;
        this.posizione = posizione;
        this.giocatori = new ArrayList<>(7);
        this.inventario = new ArrayList<>();
    }

    /**
     * Getter del nome della casella
     * @return nome
     */
    public String getNome(){
        return nome;
    }

    /**
     * Getter del nome colorato
     * @return nome della casella colorato in base alla sua funzione (falò, città, trappola, forziere, altro)
     */
    public String getNomeColorato(){
        return switch (posizione%8){
            case 0 -> "\u001B[36m" + nome + "\u001B[0m";
            case 2 -> "\u001B[31m" + nome + "\u001B[0m";
            case 4 -> "\u001B[34m" + nome + "\u001B[0m";
            case 6 -> "\u001B[37m" + nome + "\u001B[0m";
            default -> nome;
        };
    }

    /**
     * Getter dei giocatori presenti sulla casella
     * @return Arraylist di giocatori
     */
    public ArrayList<Giocatore> getGiocatori() {
        return giocatori;
    }

    /**
     * Aggiunge un giocatore all'ArrayList dei giocatori
     * presenti sulla casella
     * @param giocatore giocatore da aggiungere
     */
    public void aggiungiGiocatore(Giocatore giocatore){
        giocatori.add(giocatore);
    }

    /**
     * Rimuove un giocatore all'ArrayList dei giocatori
     * presenti sulla casella
     * @param giocatore giocatore da rimuovere
     */
    public void rimuoviGiocatore(Giocatore giocatore){
        giocatori.remove(giocatore);
    }

    /**
     * Getter della riga
     * @return riga
     */
    public int getX(){
        return x;
    }

    /**
     * Getter della colonna
     * @return colonna
     */
    public int getY() {
        return y;
    }

    /**
     * Getter della posizione
     * @return posizione
     */
    public int getPosizione() {
        return posizione;
    }

    /**
     * Setter della tempesta a true
     */
    public void aggiungiTempesta() {
        this.tempesta = true;
    }

    /**
     * Getter della tempesta
     * @return True se la casella è stata raggiunta dalla tempesta
     */
    public boolean getTempesta() {
        return tempesta;
    }

    /**
     * Getter del muro
     * @return True se è presente un muro sulla casella
     */
    public boolean getMuro() {
        return muro;
    }

    /**
     * Setter del muro.
     * @param muro boolean che indica se il muro va aggiunto o tolto
     */
    public void setMuro(boolean muro) {
        this.muro = muro;
    }

    /**
     * Setter dell'inventario della casella
     * @param inventario ArrayList contenente i Forzieri della casella
     */
    public void setInventario(ArrayList<Forziere> inventario) {
        this.inventario = inventario;
    }

    /**
     * Getter dell'inventario della casella
     * @return ArrayList dell'inventario
     */
    public ArrayList<Forziere> getInventario() {
        return inventario;
    }
}