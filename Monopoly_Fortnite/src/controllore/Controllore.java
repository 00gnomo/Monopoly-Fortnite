package controllore;

import gioco.*;
import gioco.azioni.*;
import gioco.carte.*;
import gioco.casella.*;
import gioco.giocatore.Giocatore;
import gioco.grafica.*;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.*;
import java.util.Objects;

public class  Controllore {
    private Gioco gioco;
    private Azione azione;
    private int mosse;
    private final File file;

    /**
     * Costruttore. Crea il gioco, il file dove salvare le partite e
     * il frame per scegliere l'interfaccia
     */
    public Controllore(){
        this.gioco = new Gioco();
        this.file = new File("Partita.ser");
        try {
            file.createNewFile();
        } catch (IOException ignore) {}
        new SceltaFrame(this);
    }

    /**
     * Getter dell'azione
     * @return azione
     */
    public Azione getAzione() {
        return azione;
    }

    /**
     * Getter del gioco
     * @return gioco
     */
    public Gioco getGioco(){
        return gioco;
    }

    /**
     * Metodo che fa partire la cli/gui all'inizio della partita
     * @param grafica classe che gestisce la grafica (linea di comando/interfaccia grafica)
     */
    public void startGrafica(Grafica grafica){
        grafica.start();
    }

    /**
     * Calcola il risultato del dado numerico e lo salva
     * @return il risultato del dado
     */
    public int rollDice(){
        mosse = Dado.rollDice();
        return mosse;
    }

    /**
     * Calcola il risultato del dado azioni e lo salva
     * @return il risultato del dado
     */
    public Azione rollActionDice(){
        azione = Dado.rollActionDice();
        return azione;
    }

    /**
     * Aumenta o diminuisce la vita di un giocatore
     * @param giocatore giocatore al quale modificare la vita
     * @param hp numero di punti vita da aggiungere/sottrarre
     */
    public void alterHp(Giocatore giocatore,int hp){
        if(hp>0){
            giocatore.increaseHp(hp);
        }
        else{
            giocatore.decreaseHp(-hp);
        }
    }

    /**
     * Se riceve un nome non ancora presente nella lista dei giocatori, crea un giocatore e lo aggiunge al gioco
     * @param nome nome del nuovo giocatore
     * @return true se il giocatore è stato creato, false se esiste già un giocatore con quel nome
     */
    public boolean creaGiocatore(String nome){
        nome.trim();
        for(Giocatore g : gioco.getGiocatori()){
            if(g.getNome().equals(nome)){
                return false;
            }
        }
        gioco.aggiungiGiocatore(new Giocatore(nome,gioco.getGiocatori().size()+1));
        return true;
    }

    /**
     * Se un giocatore è morto, lo rimuove dal gioco
     * @param giocatore giocatore da rimuovere
     * @return se il giocatore è stato eliminato o no
     */
    public boolean eliminaGiocatore(Giocatore giocatore){
        if(giocatore.isMorto()){
            gioco.getGiocatori().remove(giocatore);
            return true;
        }
        return false;
    }

    /**
     * Controlla se il giocatore è passato su una certa casella durante il turno corrente
     * @param passata casella su cui dovrebbe essere passato
     * @param corrente casella su cui si trova il giocatore
     * @return se il giocatore è passato sulla casella o no
     */
    public boolean casellaPassata(int passata, int corrente){
        int distanza = corrente - passata;
        if(distanza<0){
            distanza += 32;
        }
        return distanza<=mosse;
    }

    /**
     * Controlla se un giocatore ha vinto la partita
     * @return Il giocatore vincente o null se nessuno ha ancora vinto
     */
    public Giocatore getVincitore() {
        if (gioco.getGiocatori().size() == 1) {
            return gioco.getGiocatori().get(0);
        }
        return null;
    }

    /**
     * Controlla se si può sparare o piazzare una Bouncer Trap su una casella
     * @param casella casella su cui sparare/posizionare la Bouncer trap
     * @param giocatore giocatore che sta eseguendo l'azione
     * @param spara se è true l'azione è Spara, se è false l'azione è la Bouncer Trap
     * @return se su quella casella è possibile eseguire l'azione
     */
    public boolean controllaCasellaSpara(int casella, Giocatore giocatore, boolean spara){
        Casella tmp = gioco.getCasellaInPosizione(casella);
        if(!giocatore.ispezionaCarte("Rare Item 1") && spara){
            if(!(tmp.getX()==giocatore.getCasella().getX() && (tmp.getX()==0 || tmp.getX()==8)) && !(tmp.getY()==giocatore.getCasella().getY() && (tmp.getY()==0 || tmp.getY()==8))){
                return false;
            }
        }
        if(tmp.getMuro() && spara) return true;
        for(Giocatore g : tmp.getGiocatori()){
            if(!g.equals(giocatore)){
                return true;
            }
        }
        return false;
    }

    /**
     * Controlla se su una riga siano presenti giocatori o muri da colpire
     * @param giocatore giocatore che sta eseguendo l'azione
     * @return true se non sono presenti nè altri giocatori, nè muri
     */
    public boolean rigaVuota(Giocatore giocatore){
        int casella = giocatore.getCasella().getPosizione();

        if(giocatore.ispezionaCarte("Rare Item 1")){
            return false;
        }
        if(casella<9){
            for(int i=0;i<9;i++){
                if(controllaCasellaSpara(i,giocatore,true)){
                    return false;
                }
            }
        }
        if(casella>7 && casella<17){
            for(int i=8;i<17;i++){
                if(controllaCasellaSpara(i,giocatore,true)){
                    return false;
                }
            }
        }
        if(casella>15 && casella<25){
            for(int i=16;i<25;i++){
                if(controllaCasellaSpara(i,giocatore,true)){
                    return false;
                }
            }
        }
        if(casella>23){
            for(int i=24;i<32;i++){
                if(controllaCasellaSpara(i,giocatore,true)){
                    return false;
                }
            }
            return !controllaCasellaSpara(0,giocatore,true);
        }
        return true;
    }

    /**
     * Controlla se un giocatore possiede due luoghi che hanno lo stesso colore assegnato
     * @param giocatore possessore dei luoghi
     * @param color colore dei due luoghi da controllare
     * @return True se il giocatore possiede i due luoghi
     */
    public boolean stessoColore(Giocatore giocatore, Color color){
        int luoghi = 0;
        Casella[][] tab = gioco.getTabellone();

        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(i==0||i==8||j==0||j==8){
                    if(tab[i][j] instanceof CasellaCitta l){
                        try{
                            if(l.getLuogo().getPossessore().equals(giocatore)&&l.getLuogo().getColor().equals(color)){
                                luoghi++;
                            }
                        }catch(NullPointerException ignore){}
                    }
                }
            }

        }
        return luoghi==2;
    }

    /**
     * Assegna una nuova casella al giocatore
     * @param posizione numero della casella dove posizionare il giocatore
     * @param giocatore giocatore da spostare
     */
    public void assegnaCasella(int posizione, Giocatore giocatore){
        giocatore.setCasella(gioco.getCasellaInPosizione(posizione));
        giocatore.getCasella().aggiungiGiocatore(giocatore);

        if(!giocatore.getCasella().getInventario().isEmpty()){
            giocatore.aggiungiInventario(giocatore.getCasella().getInventario());
        }
    }

    /**
     * Sposta il giocatore in una nuova casella in base alle mosse
     * @param giocatore giocatore da spostare
     */
    public void muoviGiocatore(Giocatore giocatore){
        giocatore.getCasella().rimuoviGiocatore(giocatore);
        int casella = nuovaCasella(giocatore.getCasella().getPosizione(),mosse);
        assegnaCasella(casella,giocatore);
    }

    /**
     * Calcola la casella dove spostare il giocatore in base al risultato del dado numerico
     * @param casellaCorrente casella dove si trova il giocatore prima dello spostamento
     * @param mosse numero di caselle di cui si deve spostare il giocatore
     * @return la posizione della casella dove spostare il giocatore
     */
    private int nuovaCasella(int casellaCorrente, int mosse){
        for(int i=0;i<mosse;i++){
            casellaCorrente++;
            if(casellaCorrente>31){
                casellaCorrente = 32 - casellaCorrente;
            }
            //muro -> return
            if(gioco.getCasellaInPosizione(casellaCorrente).getMuro()){
                this.mosse = i;
                casellaCorrente--;
                if(casellaCorrente<0){
                    casellaCorrente += 32;
                }
                return casellaCorrente;
            }
        }
        return casellaCorrente;
    }

    /**
     * Calcola la minima distanza tra 2 caselle
     * @param c1 posizione della prima casella
     * @param c2 posizione della seconda casella
     * @return la distanza tra le caselle
     */
    public int distanzaCaselle(int c1, int c2){
        int d = Math.abs(c1-c2);
        return Math.min(d,32-d);
    }

    /**
     * Esegue l'azione scelta dal dado azioni
     * @param giocatore giocatore che esegue l'azione
     */
    public void eseguiAzione(Giocatore giocatore) {
        azione.esegui(giocatore,gioco);
    }

    /**
     * Quando un giocatore usa la carta Clinger toglie 4hp a tutti i giocatori che si trovano su una casella
     * @param scelta posizione della casella da colpire
     * @param giocatore giocatore che esegue l'azione
     */
    public void azioneClinger(int scelta, Giocatore giocatore) {
        for(Giocatore g : gioco.getCasellaInPosizione(scelta).getGiocatori()){
            if(!g.equals(giocatore)){
                alterHp(g,-4);
            }
        }
    }

    /**
     * Toglie una carta a un giocatore e la rimette nel mazzo di gioco
     * @param giocatore giocatore a cui sottrarre la carta
     * @param carta carta da rimettere nel mazzo
     */
    public void usaCarta(Giocatore giocatore, Forziere carta){
        giocatore.rimuoviCarta(carta.getFunzione());
        gioco.getMazzoForzieri().add(carta);
    }

    /**
     * Pesca una carta Forziere dal mazzo di gioco e la assegna a un giocatore
     * @param giocatore giocatore a cui assegnare la carta
     * @return la carta pescata
     */
    public Forziere pescaForziere(Giocatore giocatore){
        Forziere tmp = gioco.getMazzoForzieri().removeFirst();
        giocatore.aggiungiCarta(tmp);
        return tmp;
    }

    /**
     * Pesca una carta Tempesta dal mazzo di gioco
     * @return la carta pescata
     */
    public Tempesta pescaTempesta() {
        return gioco.getMazzoTempesta().removeFirst();
    }

    /**
     * Posiziona la tempesta su una casella o rimette la carta Tempesta nel mazzo di gioco
     * @param casella casella su cui posizionare la tempesta
     * @param tempesta carta Tempesta da rimettere nel mazzo
     */
    public void posizionaTempesta(int casella, Tempesta tempesta){
        if(casella<0) {
            gioco.getMazzoTempesta().add(tempesta);
        }
        else{
            gioco.getCasellaInPosizione(casella).aggiungiTempesta();
        }
    }

    /**
     * Calcola quale sia la città non ancora ricoperta dalla tempesta col danno più alto
     * @return la casella città
     */
    public Casella getCasellaAlta(){
        Casella[][] tabellone = gioco.getTabellone();
        CasellaCitta tmp = (CasellaCitta) tabellone[0][1];
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(i==0||i==8||j==0||j==8){
                    if(tabellone[i][j] instanceof CasellaCitta){
                        if(((CasellaCitta) tabellone[i][j]).getLuogo().getDanni()>=tmp.getLuogo().getDanni()){
                            if(!tabellone[i][j].getTempesta()){
                                tmp = (CasellaCitta) tabellone[i][j];
                            }
                        }
                    }
                }
            }
        }
        return tmp;
    }

    /**
     * Calcola quale sia la città non ancora ricoperta dalla tempesta col danno più basso
     * @return la casella città
     */
    public Casella getCasellaBassa(){
        Casella[][] tabellone = gioco.getTabellone();
        CasellaCitta tmp = (CasellaCitta) tabellone[0][1];
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(i==0||i==8||j==0||j==8){
                    if(tabellone[i][j] instanceof CasellaCitta){
                        if(((CasellaCitta) tabellone[i][j]).getLuogo().getDanni()<=tmp.getLuogo().getDanni()){
                            if(!tabellone[i][j].getTempesta()){
                                tmp = (CasellaCitta) tabellone[i][j];
                            }
                        }
                    }
                }
            }
        }
        return tmp;
    }

    /**
     * Controlla se un giocatore possiede città non ancora coperte dalla tempesta
     * @param giocatore giocatore su cui eseguire il controllo
     * @return true se il giocatore possiede almeno una città senza tempesta
     */
    public boolean cittaSenzaTempesta(Giocatore giocatore) {
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(i==0||i==8||j==0||j==8){
                    Casella c = getGioco().getTabellone()[i][j];
                    if(c instanceof CasellaCitta){
                        if(((CasellaCitta) c).getLuogo().getPossessore().equals(giocatore) && !c.getTempesta()){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Metodo che fa partire l'audio del gioco
     * @param filePath percorso dell'audio .wav
     */
    public void playAudio(String filePath) {
        try {
            Thread audioThread = new Thread(() -> {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(this.getClass().getResourceAsStream(filePath)));
                    clip.open(inputStream);
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    Thread.sleep(Long.MAX_VALUE);
                    Thread.interrupted();
                } catch (Exception e) {
                    System.err.println("Errore durante la riproduzione del file audio: " + e.getMessage());
                }
            });
            audioThread.start();
        } catch (Exception e) {
            System.err.println("Errore durante la creazione del thread audio: " + e.getMessage());
        }
    }

    /**
     * Metodo che salva i progressi della partita sul file Partita.ser
     */
    public void salvaPartita(){
        try {
            ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(file));
            o.writeObject(gioco);
        } catch (IOException ignore) {}
    }

    /**
     * Metodo che carica i progressi salvati di una vecchia partita
     * @return True se è stato possibile caricare una partita salvata
     */
    public boolean caricaPartita(){
        try {
            ObjectInputStream i = new ObjectInputStream(new FileInputStream(file));
            this.gioco = (Gioco) i.readObject();
            return true;
        } catch (IOException | ClassNotFoundException ignore) {}
        return false;
    }

    /**
     * Cancella la partita salvata nel file
     */
    public void eliminaPartita(){
        try {
            ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(file));
            o.writeObject(null);
        } catch (IOException ignore) {}
    }
}
