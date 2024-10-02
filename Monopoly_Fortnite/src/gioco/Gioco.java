package gioco;

import gioco.carte.*;
import gioco.casella.*;
import gioco.giocatore.Giocatore;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Gioco implements Serializable {
    private int turno = 0;
    private final Casella[][] tabellone;
    private final ArrayList<Giocatore> giocatori;
    private final ArrayList<Tempesta> mazzoTempesta;
    private final ArrayList<Forziere> mazzoForzieri;
    private Casella tmpCasella;

    /**
     * Costruttore. Crea i mazzi Forziere e Tempesta, i luoghi, i dadi e il tabellone.
     *
     */
    public Gioco(){
        mazzoForzieri = new ArrayList<>(16);

        mazzoForzieri.add(new Forziere("Clinger",Tipo.USAGETTA));
        mazzoForzieri.add(new Forziere("Clinger",Tipo.USAGETTA));
        for(int i=0;i<2;i++){
            mazzoForzieri.add(new Forziere("Epic Item", Tipo.PERMANENTI));
            mazzoForzieri.add(new Forziere("Rare Item 2", Tipo.PERMANENTI));
            mazzoForzieri.add(new Forziere("Rare Item 1", Tipo.PERMANENTI));
            mazzoForzieri.add(new Forziere("Legendary Item", Tipo.PERMANENTI));

            mazzoForzieri.add(new Forziere("Bouncer Trap",Tipo.USAGETTA));
        }
        mazzoForzieri.add(new Forziere("Stink Bomb",Tipo.USAGETTA));
        mazzoForzieri.add(new Forziere("Bush", Tipo.PERMANENTI));
        mazzoForzieri.add(new Forziere("Medikit",Tipo.USAGETTA));
        mazzoForzieri.add(new Forziere("Chug Jug",Tipo.USAGETTA));
        //Collections.shuffle(mazzoForzieri);

        ArrayList<Luogo> mazzoLuoghi = new ArrayList<>(16);
        mazzoLuoghi.add(new Luogo("Paradise Palms",1, new Color(88, 57, 39)));
        mazzoLuoghi.add(new Luogo("Dusty Divot",1, new Color(88, 57, 39)));
        mazzoLuoghi.add(new Luogo("Tomato Town",1, new Color(134, 222, 248)));
        mazzoLuoghi.add(new Luogo("Snobby Shores",1, new Color(134, 222, 248)));
        mazzoLuoghi.add(new Luogo("Viking Village",2, Color.MAGENTA));
        mazzoLuoghi.add(new Luogo("Retail Row",2, Color.MAGENTA));
        mazzoLuoghi.add(new Luogo("Lonely Lodge",2, new Color(255,117,20)));
        mazzoLuoghi.add(new Luogo("Pleasant Park",2, new Color(255,117,20)));
        mazzoLuoghi.add(new Luogo("Flush Factory",3, Color.RED));
        mazzoLuoghi.add(new Luogo("Wailing Woods",3, Color.RED));
        mazzoLuoghi.add(new Luogo("Salty Springs",3, Color.YELLOW));
        mazzoLuoghi.add(new Luogo("Haunted Hills",3, Color.YELLOW));
        mazzoLuoghi.add(new Luogo("Greasy Grove",4, Color.GREEN));
        mazzoLuoghi.add(new Luogo("Loot Lake",4, Color.GREEN));
        mazzoLuoghi.add(new Luogo("Lazy Links",4, new Color(34,113,179)));
        mazzoLuoghi.add(new Luogo("Tilted Towers",4, new Color(34,113,179)));

        mazzoTempesta = new ArrayList<>();
        for(int i=0;i<3;i++){
            mazzoTempesta.add(new Tempesta("Falo'"));
            mazzoTempesta.add(new Tempesta("Luogo"));
        }
        for(int i=0;i<2;i++){
            mazzoTempesta.add(new Tempesta("Dado"));
            mazzoTempesta.add(new Tempesta("Valore Alto"));
        }
        mazzoTempesta.add(new Tempesta("Valore Basso"));
        mazzoTempesta.add(new Tempesta("Vai In Prigione"));
        mazzoTempesta.add(new Tempesta("Prigione"));
        mazzoTempesta.add(new Tempesta("Go"));
        mazzoTempesta.add(new Tempesta("Luogo 2"));
        Collections.shuffle(mazzoTempesta);

        //TABELLONE
        tabellone = new Casella[9][9];
        //angoli
        tabellone[0][0] = new CasellaGo();
        tabellone[0][8] = new CasellaPrigione();
        tabellone[8][0] = new CasellaVaiInPrigione();
        tabellone[8][8] = new CasellaParcheggio();
        //falo'
        tabellone[0][2] = new CasellaFalo(0,2,2);
        tabellone[2][8] = new CasellaFalo(2,8,10);
        tabellone[8][6] = new CasellaFalo(8,6,18);
        tabellone[6][0] = new CasellaFalo(6,0,26);
        //forzieri
        tabellone[0][4] = new CasellaForziere(0,4,4);
        tabellone[4][8] = new CasellaForziere(4,8,12);
        tabellone[8][4] = new CasellaForziere(8,4,20);
        tabellone[4][0] = new CasellaForziere(4,0,28);
        //trappole
        tabellone[0][6] = new CasellaTrappola(0,6,6);
        tabellone[6][8] = new CasellaTrappola(6,8,14);
        tabellone[8][2] = new CasellaTrappola(8,2,22);
        tabellone[2][0] = new CasellaTrappola(2,0,30);
        for(int colonna=1;colonna<8;colonna+=2){
            tabellone[0][colonna] = new CasellaCitta(0,colonna,colonna, mazzoLuoghi.removeFirst());
        }
        for(int riga=1;riga<8;riga+=2){
            tabellone[riga][8] = new CasellaCitta(riga,8,riga+8, mazzoLuoghi.removeFirst());
        }
        for(int colonna=7;colonna>0;colonna-=2){
            tabellone[8][colonna] = new CasellaCitta(8,colonna,24-colonna, mazzoLuoghi.removeFirst());
        }
        for(int riga=7;riga>0;riga-=2){
            tabellone[riga][0] = new CasellaCitta(riga,0,32-riga, mazzoLuoghi.removeFirst());
        }
        giocatori = new ArrayList<>();
    }

    /**
     * Getter dei giocatori nella partita
     * @return ArrayList dei giocatori
     */
    public ArrayList<Giocatore> getGiocatori() {
        return giocatori;
    }

    /**
     * Metodo che aggiunge un giocatore all'ArrayList di giocatori
     * @param giocatore giocatore da aggiungere
     */
    public void aggiungiGiocatore(Giocatore giocatore){
        giocatori.add(giocatore);
    }

    /**
     * Getter della matrice di Caselle tabellone
     * @return tabellone
     */
    public Casella[][] getTabellone() {
        return tabellone;
    }

    /**
     * Metodo che cerca una casella in base alla sua posizione
     * @param posizione posizione della casella richiesta
     * @return Casella nella posizione data
     */
    public Casella getCasellaInPosizione(int posizione){
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(i==0||i==8||j==0||j==8){
                    if(tabellone[i][j].getPosizione()==posizione){
                        return tabellone[i][j];
                    }
                }

            }
        }
        return null;
    }

    /**
     * Getter del mazzo delle carte Tempesta
     * @return mazzo di carte Tempesta
     */
    public ArrayList<Tempesta> getMazzoTempesta() {
        return mazzoTempesta;
    }
    /**
     * Getter del mazzo delle carte Forziere
     * @return mazzo di carte Forziere
     */
    public ArrayList<Forziere> getMazzoForzieri() {
        return mazzoForzieri;
    }

    /**
     * Setter della casella temporanea su cui sparare/posizionare oggetti
     * @param tmpCasella casella scelta dal giocatore
     */
    public void setTmpCasella(Casella tmpCasella) {
        this.tmpCasella = tmpCasella;
    }

    /**
     * Getter della casella temporanea
     * @return casella temporanea
     */
    public Casella getTmpCasella() {
        return tmpCasella;
    }

    /**
     * Getter del turno di gioco
     * @return turno di gioco
     */
    public int getTurno() {
        return turno;
    }

    /**
     * Metodo che aumenta di 1 il turno di gioco
     */
    public void cambiaTurno(){
        turno++;
    }
}
