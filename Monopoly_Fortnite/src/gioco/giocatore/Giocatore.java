package gioco.giocatore;

import gioco.carte.*;
import gioco.casella.Casella;

import javax.swing.*;
import java.io.Serializable;
import java.util.*;

public class Giocatore implements Serializable {
    private final String nome;
    private int hp, danni, contTurni;
    private final int contColori;
    private Casella casella;
    private final ArrayList<Forziere> carte;
    private final ArrayList<Luogo> luoghi;
    private boolean morto;
    private final ImageIcon playerIcon;

    /**
     * Costruttore
     * @param nome username del giocatore
     * @param cont contatore per assegnare le immagini delle pedine
     */
    public Giocatore(String nome, int cont) {
        this.nome = nome;
        this.contColori = cont;
        this.hp = 15;
        this.danni = 1;
        this.contTurni = 0;
        this.carte = new ArrayList<>();
        this.luoghi = new ArrayList<>();
        this.playerIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/giocatori/giocatore_"+cont+".png")));
    }

    /**
     * Getter del nome
     * @return nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Getter del nome col rispettivo colore
     * @return nome colorato
     */
    public String getNomeColorato(){
        return "\u001B[3" + contColori + "m" + nome + "\u001B[0m";
    }

    /**
     * Getter del contatore
     * @return numero del giocatore
     */
    public int getCont(){
        return contColori;
    }

    /**
     * Diminuisce gli hp di un certo valore. Se scendono sotto 0 il giocatore è morto
     * @param hp punti salute da togliere al giocatore
     */
    public void decreaseHp(int hp){
        if(this.hp-hp>0){
            this.hp-=hp;
        }
        else{
            this.hp=0;
            morto = true;
            casella.setInventario(carte);
            carte.clear();
            casella.rimuoviGiocatore(this);
        }
    }

    /**
     * Diminuisce gli hp di un certo valore. Il massimo è 15
     * @param hp punti salute da aggiungere al giocatore
     */
    public void increaseHp(int hp){
        if(this.hp+hp<=15){
            this.hp+=hp;
        }
        else{
            this.hp=15;
        }
    }

    /**
     * Getter dei punti salute
     * @return punti salute
     */
    public int getHp() {
        return hp;
    }

    /**
     * Getter dello stato della vita del giocatore
     * @return True se il giocatore è morto
     */
    public boolean isMorto() {
        return morto;
    }

    /**
     * Metodo che aumenta il contatore dei turni passati
     * in prigione dal giocatore
     */
    public void incrementaContTurni(){
        contTurni++;
    }

    /**
     * Metodo che riporta a 0 il contatore dei turni passati
     * in prigione dal giocatore
     */
    public void resetContTurni(){
        contTurni = 0;
    }

    /**
     * Getter del contatore dei turni passati in
     * prigione dal giocatore
     * @return turni passati in prigione
     */
    public int getContTurni() {
        return contTurni;
    }

    /**
     * Getter dei danni che il giocatore può infliggere ai nemici
     * @return danni
     */
    public int getDanni() {
        return danni;
    }

    /**
     * Getter della casella sopra cui si trova
     * il giocatore
     * @return casella
     */
    public Casella getCasella(){
        return casella;
    }

    /**
     * Setter della casella sopra cui si
     * trova il giocatore
     * @param casella casella da assegnare al giocatore
     */
    public void setCasella(Casella casella){
        this.casella = casella;
    }

    /**
     * Metodo che aggiunge una carta Forziere
     * all'inventario del giocatore
     * @param forziere carta da aggiungere all'inventario
     */
    public void aggiungiCarta(Forziere forziere){
        carte.add(forziere);
        switch (forziere.getFunzione()){
            case "Rare Item 2" : if(danni<2) danni = 2; break;
            case "Epic Item" : if(danni<3) danni = 3; break;
            case "Legendary Item" :  if(danni<4) danni = 4; break;
        }
    }

    /**
     Metodo che aggiunge un'ArrayList di carte Forziere
     * all'inventario del giocatore
     * @param inventario ArrayList di carte da aggiungere all'inventario
     */
    public void aggiungiInventario(ArrayList<Forziere> inventario){
        carte.addAll(inventario);
    }

    /**
     * Getter dell'inventario del giocatore
     * @return ArrayList di carte Forziere possedute dal giocatore
     */
    public ArrayList<Forziere> getCarte(){
        return carte;
    }

    /**
     * Controlla se il giocatore possiede
     * una carta Forziere
     * @param funzione funzione della carta da cercare
     * @return True se l'inventario contiene una carta con quella funzione
     */
    public boolean ispezionaCarte(String funzione){
        for(Forziere f : carte){
            if(f.getFunzione().equals(funzione)){
                return true;
            }
        }
        return false;
    }

    /**
     * Metodo che rimuove una carta Forziere dall'inventario
     * data la sua funzione
     * @param funzione funzione della carta da rimuovere
     * @return La carta eliminata o null se non è stata trovata
     */
    public Forziere rimuoviCarta(String funzione){
        for(Forziere f : carte){
            if(f.getFunzione().equals(funzione)){
                carte.remove(f);
                return f;
            }
        }
        return null;
    }

    /**
     * Getter dei luoghi di cui il giocatore è il possessore
     * @return ArrayList dei luoghi posseduti
     */
    public ArrayList<Luogo> getLuoghi() {
        return luoghi;
    }

    /**
     * Getter dell'immagine della pedina
     * @return immagine
     */
    public ImageIcon getPlayerIcon(){
        return playerIcon;
    }

    /**
     * Getter della stringa che mostra nome, hp, danni e posizione del giocatore
     * @return Stringa contenente il profilo del giocatore
     */
    @Override
    public String toString(){
        return "Nome: " + nome + "\nVita: " + hp + "hp\nDanni: " + danni + "\nPosizione: " + casella.getPosizione();
    }

}