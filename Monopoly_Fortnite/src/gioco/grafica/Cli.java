package gioco.grafica;

import controllore.Controllore;
import gioco.azioni.*;
import gioco.carte.*;
import gioco.casella.*;
import gioco.giocatore.Giocatore;

import java.io.Serializable;
import java.util.*;

public class Cli implements Grafica, Serializable {
    private final Controllore controllore;
    private final Scanner scanner;
    private boolean esci;
    private int giocatori;
    //errori
    private static final String Input_Mismatch_Error = "Inserisci un numero";
    private static final String Invalid_Choice_Error = "Scelta non valida";
    //messaggi
    private static final String Prison_Exit = "Sei uscito di prigione";

    /**
     * Costruttore. Crea lo scanner
     * e inizializza il controllore
     * @param controllore controllore che gestisce il gioco
     */
    public Cli(Controllore controllore) {
        this.controllore = controllore;
        scanner = new Scanner(System.in);
    }

    /**
     * Metodo che fa scegliere se iniziare o caricare una
     * partita, inserire i giocatori e che gestisce i turni
     * di gioco.
     */
    @Override
    public void start(){
        String titolo = "MONOPOLY FORTNITE ";
        for(int i=0;i<titolo.length();i+=3){
            System.out.print("\u001B[34m" + titolo.charAt(i));
            System.out.print("\u001B[36m" + titolo.charAt(i+1));
            System.out.print("\u001B[35m" + titolo.charAt(i+2) + "\u001B[0m");
        }
       int scelta;
        //scelta nuova/carica
        do{
            System.out.print("\n\n0 - Nuova partita\n1 - Carica partita\nScelta: ");
            try{
                scelta = scanner.nextInt();
                if(scelta!=0&&scelta!=1){
                    showError(Invalid_Choice_Error);
                }
            }catch(InputMismatchException e){
                scanner.nextLine();
                showError(Input_Mismatch_Error);
                scelta = -1;
            }
        }while(scelta!=0&&scelta!=1);

        if(scelta==1){
            if(!controllore.caricaPartita()){
                showError("Partita salvata non trovata");
                showMessage("NUOVA PARTITA");
                scelta = 0;
            }
            else{
                giocatori = controllore.getGioco().getGiocatori().size();
            }
        }
        if(scelta==0){
            showMessage("\nINSERIMENTO GIOCATORI");
            do{
                try{
                    System.out.print("Numero giocatori (2-7) : ");
                    giocatori = scanner.nextInt();
                    if(giocatori>7||giocatori<2){
                        showError("Inserisci un numero di giocatori tra 2 e 7");
                    }
                }catch (InputMismatchException e){
                    scanner.nextLine();
                    giocatori = -1;
                    showError(Input_Mismatch_Error);
                }
            }while(giocatori>7 || giocatori<2);
            for (int i=0;i<giocatori;i++){
                String user;
                boolean continua = false;
                do{
                    showMessage("Giocatore " + (i+1));
                    System.out.print("Username: ");
                    user = scanner.next();
                    scanner.nextLine();
                    if(controllore.creaGiocatore(user)){
                        continua = true;
                        System.out.println("\u001B[32mGiocatore creato\u001B[0m");
                    }
                    else{
                        showError("Giocatore già esistente");
                    }
                }while(!continua);
            }
            //casella di partenza
            stampaTabellone(null);
            for(int i=0;i<giocatori;i++){
                Giocatore tmp = controllore.getGioco().getGiocatori().get(i);
                showMessage("Giocatore " + tmp.getNome());
                int casella;
                do{
                    System.out.print("Casella di partenza: ");
                    try {
                        casella = scanner.nextInt();
                        if(casella<0||casella>31){
                            showError(Invalid_Choice_Error);
                        }
                        else if(casella%2==0){
                            showError("Seleziona una casella citta'");
                            casella = -1;
                        }
                    }catch(InputMismatchException e){
                        scanner.nextLine();
                        casella = -1;
                        showError(Input_Mismatch_Error);
                    }
                }while(casella<1||casella>31);
                controllore.assegnaCasella(casella,tmp);
            }
        }

        //turno di gioco
        while(!esci){
            Giocatore tmp = controllore.getGioco().getGiocatori().get(controllore.getGioco().getTurno()%giocatori);
            stampaTabellone(tmp);
            showMessage("\nTurno di " + tmp.getNome());
            System.out.println(tmp);
            int dado = 6;
            if(tmp.getContTurni()==1||tmp.getContTurni()==2){
                showMessage("SEI IN PRIGIONE");
                do{
                    System.out.print("Vuoi pagare o tirare il dado per uscire? (0/1): ");
                    try {
                        scelta = scanner.nextInt();
                        if(scelta!=0&&scelta!=1){
                            showError(Invalid_Choice_Error);
                        }
                    }catch(InputMismatchException e){
                        scanner.nextLine();
                        scelta = -1;
                        showError(Input_Mismatch_Error);
                    }
                }while(scelta!=0&&scelta!=1);
                if(scelta==0){
                    controllore.alterHp(tmp,-2);
                    tmp.resetContTurni();
                    showMessage(Prison_Exit);
                    //MORTO
                    if(controllaMorti()!=null){
                        showMessage(controllore.getVincitore().getNome() + " HA VINTO");
                        controllore.eliminaPartita();
                        return;
                    }
                }
                else{
                    dado = controllore.rollDice();
                    System.out.println("Risultato dado: " + dado);
                    if(dado==6){
                        showMessage(Prison_Exit);
                        tmp.resetContTurni();
                    }
                    else{
                        System.out.println("Ritenta dopo");
                        tmp.incrementaContTurni();
                    }
                }
            }
            else if(tmp.getContTurni()==3){
                showMessage(Prison_Exit);
                tmp.resetContTurni();
            }
            if(dado==6){
                showMessage("RISULTATI DADI");
                System.out.println("0 - Mosse: " + controllore.rollDice());
                Azione azione = controllore.rollActionDice();
                System.out.println("1 - Azione: " + azione);
                //dadi
                scelta = 0;
                if(!(azione instanceof Muro)){
                    do{
                        System.out.print("Cosa vuoi fare prima? (0/1): ");
                        try {
                            scelta = scanner.nextInt();
                            if(scelta!=0&&scelta!=1){
                                showError(Invalid_Choice_Error);
                            }
                        }catch(InputMismatchException e){
                            scanner.nextLine();
                            scelta = -1;
                            showError(Input_Mismatch_Error);
                        }
                    }while(scelta!=0&&scelta!=1);
                }
                if(scelta==0){
                    //prima si muove
                    controllore.muoviGiocatore(tmp);
                    stampaTabellone(tmp);
                    if(azione instanceof Muro){
                        showError("Risultato dado azione: muro. Sei obbligato a muoverti subito.");
                    }
                    azioneCasella(tmp);
                    if(controllaMorti()!=null){
                        showMessage(controllore.getVincitore().getNome() + " HA VINTO");
                        return;
                    }
                    //azione
                    if(tmp.getContTurni()==0){
                        richiesteAzione(azione,tmp,false);
                    }
                }
                else{
                    stampaTabellone(tmp);
                    //prima fa l'azione
                    richiesteAzione(azione,tmp,true); //muove il giocatore in richiesteAzione()
                    //MORTO
                    if(controllaMorti()!=null){
                        showMessage(controllore.getVincitore().getNome() + " HA VINTO");
                        return;
                    }
                }
                if(controllaMorti()!=null){
                    showMessage(controllore.getVincitore().getNome() + " HA VINTO");
                    return;
                }

                //CARTE
                if(tmp.getContTurni()==0){
                    showMessage("INVENTARIO");
                    int cont = 1;
                    for(Forziere f : tmp.getCarte()){
                        if(f.getTipo()== Tipo.USAGETTA && !f.getFunzione().equals("Stink Bomb")){
                            System.out.println(cont + " - " + f.getFunzione());
                            cont++;
                        }
                    }
                    if(cont==1){
                        showError("Non hai carte utilizzabili nell'inventario");
                    }
                    else{
                        do{
                            System.out.print("Numero carta da usare (0->niente): ");
                            try {
                                scelta = scanner.nextInt();
                                if(scelta<0||scelta>tmp.getCarte().size()){
                                    showError(Invalid_Choice_Error);
                                }
                            }catch(InputMismatchException e){
                                scanner.nextLine();
                                scelta = -1;
                                showError(Input_Mismatch_Error);
                            }
                        }while(scelta<0||scelta>tmp.getCarte().size());
                        if(scelta!=0){
                            Forziere f = tmp.getCarte().get(scelta-1);
                            usaForziere(f,tmp);
                            controllore.usaCarta(tmp,f);
                        }
                    }
                }
            }
            if(controllaMorti()!=null){
                showMessage(controllore.getVincitore().getNome() + " HA VINTO");
                return;
            }
            //PASSAGGIO DAL VIA
            if(controllore.casellaPassata(0,tmp.getCasella().getPosizione()) && !tmp.isMorto()){
                showMessage("\nSei passato dal via: peschi una tempesta.");
                Tempesta tempesta = controllore.pescaTempesta();
                azioneTempesta(tempesta,tmp);

                if(controllaMorti()!=null){
                    showMessage(controllore.getVincitore().getNome() + " HA VINTO");
                    return;
                }
            }
            showMessage("PROFILO");
            System.out.println(tmp + "\n");

            //SCEGLI SE USCIRE
            do{
                showMessage("FINE TURNO");
                System.out.println("\u001B[33m0 - continua la partita\u001B[0m\n1 - esci senza salvare\n2 - : esci e salva");
                System.out.print("Scelta: ");
                try{
                    scelta = scanner.nextInt();
                    if(scelta>2||scelta<0){
                        showError(Invalid_Choice_Error);
                    }
                }catch(InputMismatchException e){
                    scanner.nextLine();
                    scelta = -1;
                    showError(Input_Mismatch_Error);
                }
            }while(scelta>2||scelta<0);

            controllore.getGioco().cambiaTurno();

            switch(scelta){
                case 0 -> {
                    System.out.print("\n\u001B[35m--------------------------------------------------\u001B[0m");
                    System.out.println("\u001B[35m--------------------------------------------------\u001B[0m");
                }
                case 1 -> esci = true;
                case 2 -> {
                    controllore.salvaPartita();
                    showMessage("Partita salvata.");
                    esci = true;
                }
            }
        }
    }

    /**
     * Stampa tutte le caselle coi rispettivi giocatori,
     * tempeste e muri.
     * Il giocatore corrente e i luoghi che possiede
     * sono evidenziati.
     * Stampa anche la vita di tutti i giocatori.
     * @param giocatore gicoatore corrente
     */
    private void stampaTabellone(Giocatore giocatore) {
        Casella[][] tab = controllore.getGioco().getTabellone();
        //riga sopra
        for (int i = 0; i < 9; i++) {
            System.out.print("___________");
        }
        System.out.println("_");
        //nome casella
        for (int i = 0; i < 9; i++) {
            if (i%2 == 1) {
                try{
                    if(((CasellaCitta)tab[0][i]).getLuogo().getPossessore().equals(giocatore)){
                        System.out.print("|\u001B[43m\u001B[30mCitta'    \u001B[0m");
                    }
                    else{
                        System.out.print("|\u001B[32mCitta'    \u001B[0m");
                    }
                }catch (NullPointerException e){
                    System.out.print("|\u001B[32mCitta'    \u001B[0m");
                }
            } else {
                System.out.print("|" + tab[0][i].getNomeColorato());
                for (int j = 0; j < 10 - tab[0][i].getNome().length(); j++) {
                    System.out.print(" ");
                }
            }
        }
        System.out.println("|");
        //giocatori
        for (int i = 0; i < 9; i++) {
            System.out.print("| ");
            for (Giocatore g: tab[0][i].getGiocatori()) {
                if(g.equals(giocatore)){
                    System.out.print("\u001B[45m\u001B[30mX\u001B[0m");
                }
                else{
                    System.out.print("\u001B[3" + g.getCont() + "mX\u001B[0m");
                }
            }
            for(int j=0;j<9-tab[0][i].getGiocatori().size();j++){
                System.out.print(" ");
            }
        }
        System.out.println("|");
        //numero casella
        for (int i = 0; i < 9; i++) {
            if(tab[0][i].getTempesta() && tab[0][i].getMuro()){
                System.out.print("\u001B[0m|\u001B[45m     \u001B[30m" + i + "\u001B[47m    ");
            }
            else if(tab[0][i].getTempesta()){
                System.out.print("\u001B[0m|\u001B[45m     \u001B[30m" + i + "\u001B[45m    ");
            }
            else if(tab[0][i].getMuro()){
                System.out.print("\u001B[0m|\u001B[47m     \u001B[30m" + i + "\u001B[47m    ");
            }
            else{
                System.out.print("\u001B[0m|     " + i + "    ");
            }
        }
        System.out.println("\u001B[0m|");
        for (int i = 0; i < 9; i++) {
            System.out.print("___________");
        }
        System.out.println("_");

        //righe in mezzo
        for (int i = 0; i < 7; i++) {
            //colonna sinistra
            if (i%2 == 0) {
                try{
                    if(((CasellaCitta)tab[i+1][0]).getLuogo().getPossessore().equals(giocatore)){
                        System.out.print("|\u001B[43m\u001B[30mCitta'    \u001B[0m|");
                    }
                    else{
                        System.out.print("|\u001B[32mCitta'    \u001B[0m|");
                    }
                }catch (NullPointerException e){
                    System.out.print("|\u001B[32mCitta'    \u001B[0m|");
                }
            } else {
                System.out.print("|" + tab[i+1][0].getNomeColorato());
                for (int j = 0; j < 10 - tab[i+1][0].getNome().length(); j++) {
                    System.out.print(" ");
                }
                System.out.print("|");
            }
            //vuoto
            for(int j=0;j<7;j++){
                if(j!=6){
                    System.out.print("           ");
                }
                else{
                    System.out.print("          ");
                }
            }
            //colonna destra
            if (i%2 == 0) {
                try{
                    if(((CasellaCitta)tab[i+1][8]).getLuogo().getPossessore().equals(giocatore)){
                        System.out.print("|\u001B[43m\u001B[30mCitta'    \u001B[0m|");
                    }
                    else{
                        System.out.print("|\u001B[32mCitta'    \u001B[0m|");
                    }
                }catch (NullPointerException e){
                    System.out.print("|\u001B[32mCitta'    \u001B[0m|");
                }
            } else {
                System.out.print("|" + tab[i+1][8].getNomeColorato());
                for (int j = 0; j < 10 - tab[i+1][8].getNome().length(); j++) {
                    System.out.print(" ");
                }
                System.out.print("|");
            }
            System.out.println();
            //giocatori
            System.out.print("|");
            for (Giocatore g: tab[i+1][0].getGiocatori()) {
                if(g.equals(giocatore)){
                    System.out.print("\u001B[45m\u001B[30mX\u001B[0m");
                }
                else{
                    System.out.print("\u001B[3" + g.getCont() + "mX\u001B[0m");
                }
            }
            for(int j=0;j<10-tab[i+1][0].getGiocatori().size();j++){
                System.out.print(" ");
            }
            System.out.print("|");
            for(int j=0;j<7;j++){
                if(j!=6){
                    System.out.print("            ");
                }
                else{
                    System.out.print("    ");
                }
            }
            System.out.print("|");
            for (Giocatore g: tab[i+1][8].getGiocatori()) {
                if(g.equals(giocatore)){
                    System.out.print("\u001B[45m\u001B[30mX\u001B[0m");
                }
                else{
                    System.out.print("\u001B[3" + g.getCont() + "mX\u001B[0m");
                }
            }

            for(int j=0;j<10-tab[i+1][8].getGiocatori().size();j++){
                System.out.print(" ");
            }
            System.out.println("|");
            //numero casella
            if(tab[i+1][0].getTempesta() && tab[i+1][0].getMuro()){
                System.out.print("\u001B[0m|\u001B[45m    \u001B[30m" + tab[i+1][0].getPosizione() + "\u001B[47m    ");
            }
            else if(tab[i+1][0].getTempesta()){
                System.out.print("\u001B[0m|\u001B[45m    \u001B[30m" + tab[i+1][0].getPosizione() + "\u001B[45m    ");
            }
            else if(tab[i+1][0].getMuro()){
                System.out.print("\u001B[0m|\u001B[47m    \u001B[30m" + tab[i+1][0].getPosizione() + "\u001B[47m    ");
            }
            else{
                System.out.print("\u001B[0m|    " + tab[i+1][0].getPosizione() + "    ");
            }

            System.out.print("\u001B[0m|");
            for(int j=0;j<7;j++){
                if(j!=6){
                    System.out.print("           ");
                }
                else{
                    System.out.print("          ");
                }
            }
            //posizione destra
            if(tab[i+1][8].getPosizione()==9){
                if(tab[i+1][8].getTempesta() && tab[i+1][8].getMuro()){
                    System.out.print("\u001B[0m|\u001B[45m     \u001B[30m" + tab[i+1][8].getPosizione() + "\u001B[47m    ");
                }
                else if(tab[i+1][8].getTempesta()){
                    System.out.print("\u001B[0m|\u001B[45m     \u001B[30m" + tab[i+1][8].getPosizione() + "\u001B[45m    ");
                }
                else if(tab[i+1][8].getMuro()){
                    System.out.print("\u001B[0m|\u001B[47m     \u001B[30m" + tab[i+1][8].getPosizione() + "\u001B[47m    ");
                }
                else{
                    System.out.print("\u001B[0m|     " + tab[i+1][8].getPosizione() + "    ");
                }
            }
            else{
                if(tab[i+1][8].getTempesta() && tab[i+1][8].getMuro()){
                    System.out.print("\u001B[0m|\u001B[45m    \u001B[30m" + tab[i+1][8].getPosizione() + "\u001B[47m    ");
                }
                else if(tab[i+1][8].getTempesta()){
                    System.out.print("\u001B[0m|\u001B[45m    \u001B[30m" + tab[i+1][8].getPosizione() + "\u001B[45m    ");
                }
                else if(tab[i+1][8].getMuro()){
                    System.out.print("\u001B[0m|\u001B[47m    \u001B[30m" + tab[i+1][8].getPosizione() + "\u001B[47m    ");
                }
                else{
                    System.out.print("\u001B[0m|    " + tab[i+1][8].getPosizione() + "    ");
                }
            }

            System.out.println("\u001B[0m|");
            if(i!=6){
                System.out.print("____________");
                for(int j=0;j<7;j++){
                    if(j!=6){
                        System.out.print("           ");
                    }
                    else{
                        System.out.print("          ");
                    }
                }
                System.out.println("____________");
            }
            else{
                for(int j = 0; j < 9; j++) {
                    System.out.print("___________");
                }
            }
        }
        System.out.println("_");
        //riga sotto
        System.out.print("|\u001B[36mGoToPrison\u001B[0m");
        for (int i = 1; i < 9; i++) {
            if (i % 2 == 1) {
                try{
                    if(((CasellaCitta)tab[8][i]).getLuogo().getPossessore().equals(giocatore)){
                        System.out.print("|\u001B[43m\u001B[30mCitta'    \u001B[0m");
                    }
                    else{
                        System.out.print("|\u001B[32mCitta'    \u001B[0m");
                    }
                }catch (NullPointerException e){
                    System.out.print("|\u001B[32mCitta'    \u001B[0m");
                }
            } else {
                System.out.print("|" + tab[8][i].getNomeColorato());
                for (int j = 0; j < 10 - tab[8][i].getNome().length(); j++) {
                    System.out.print(" ");
                }
            }
        }
        System.out.println("|");
        //giocatori
        for (int i = 0; i < 9; i++) {
            System.out.print("| ");
            for (Giocatore g: tab[8][i].getGiocatori()) {
                if(g.equals(giocatore)){
                    System.out.print("\u001B[45m\u001B[30mX\u001B[0m");
                }
                else{
                    System.out.print("\u001B[3" + g.getCont() + "mX\u001B[0m");
                }
            }

            for(int j=0;j<9-tab[8][i].getGiocatori().size();j++){
                System.out.print(" ");
            }
        }
        System.out.println("|");
        //numero casella
        for (int i = 0; i < 9; i++) {
            if(tab[8][i].getTempesta() && tab[8][i].getMuro()){
                System.out.print("\u001B[0m|\u001B[45m    \u001B[30m" + tab[8][i].getPosizione() + "\u001B[47m    ");
            }
            else if(tab[8][i].getTempesta()){
                System.out.print("\u001B[0m|\u001B[45m    \u001B[30m" + tab[8][i].getPosizione() + "\u001B[45m    ");
            }
            else if(tab[8][i].getMuro()){
                System.out.print("\u001B[0m|\u001B[47m    \u001B[30m" + tab[8][i].getPosizione() + "\u001B[47m    ");
            }
            else{
                System.out.print("\u001B[0m|    " + tab[8][i].getPosizione() + "    ");
            }
        }
        System.out.println("\u001B[0m|");
        for (int i = 0; i < 9; i++) {
            System.out.print("___________");
        }
        System.out.println("_");
        stampaVitaGiocatori();
    }

    /**
     * Metodo che stampa delle richieste in base
     * alla carta tempesta pescata dal giocatore
     * @param tempesta carta Tempesta
     * @param giocatore giocatore corrente
     */
    private void azioneTempesta(Tempesta tempesta, Giocatore giocatore){
        int scelta = -1;
        switch(tempesta.getFunzione()){
            case "Falo'" ->{
                do{
                    System.out.print("Seleziona un falo' su cui posizionare la tempesta: ");
                    try{
                        scelta = scanner.nextInt();
                        if(scelta<0||scelta>31){
                            showError(Invalid_Choice_Error);
                        }
                        else if(!(controllore.getGioco().getCasellaInPosizione(scelta) instanceof CasellaFalo)){
                            showError(Invalid_Choice_Error);
                            scelta = -1;
                        }
                    }catch(InputMismatchException e){
                        showError(Input_Mismatch_Error);
                    }
                }while(scelta<0||scelta>31);
                controllore.posizionaTempesta(scelta,null);
            }
            case "Luogo" ->{
                do{
                    System.out.print("Citta' su cui posizionare la tempesta: ");
                    try{
                        scelta = scanner.nextInt();
                        if(scelta<0||scelta>31){
                            showError(Invalid_Choice_Error);
                        }
                        else if(!(controllore.getGioco().getCasellaInPosizione(scelta) instanceof CasellaCitta)){
                            showError(Invalid_Choice_Error);
                            scelta = -1;
                        }
                        else if((controllore.getGioco().getCasellaInPosizione(scelta).getTempesta())){
                            showError(Invalid_Choice_Error);
                            scelta = -1;
                        }
                    }catch(InputMismatchException e){
                        showError(Input_Mismatch_Error);
                    }
                }while(scelta<0||scelta>31);
                controllore.posizionaTempesta(scelta,null);
            }
            case "Luogo 2" ->{
                do{
                    if(controllore.cittaSenzaTempesta(giocatore)){
                        System.out.print("Citta' di tua proprieta' su cui posizionare la tempesta: ");
                    }
                    else{
                        System.out.print("Citta' su cui posizionare la tempesta: ");
                    }
                    try{
                        scelta = scanner.nextInt();
                        if(scelta<0||scelta>31){
                            showError(Invalid_Choice_Error);
                        }
                        else if(!(controllore.getGioco().getCasellaInPosizione(scelta) instanceof CasellaCitta)){
                            showError(Invalid_Choice_Error);
                            scelta = -1;
                        }
                        else if(!(((CasellaCitta) controllore.getGioco().getCasellaInPosizione(scelta))).getLuogo().getPossessore().equals(giocatore) && controllore.cittaSenzaTempesta(giocatore)){
                            showError(Invalid_Choice_Error);
                            scelta = -1;
                        }
                        else if(!(controllore.getGioco().getCasellaInPosizione(scelta).getTempesta())){
                            showError(Invalid_Choice_Error);
                            scelta = -1;
                        }
                    }catch(InputMismatchException e){
                        showError(Input_Mismatch_Error);
                    }
                }while(scelta<0||scelta>31);
                controllore.posizionaTempesta(scelta,null);
            }
            case "Vai In Prigione" ->{
                System.out.println("La tempesta ha raggiunto la casella Vai In Prigione");
                controllore.posizionaTempesta(24,null);
            }
            case "Prigione" ->{
                System.out.println("La tempesta ha raggiunto la prigione");
                controllore.posizionaTempesta(8,null);
            }
            case "Go" ->{
                System.out.println("La tempesta ha raggiunto il Via");
                controllore.posizionaTempesta(0,null);
            }
            case "Valore Alto" ->{
                System.out.println("La tempesta ha raggiunto il luogo con maggior danno");
                controllore.posizionaTempesta(controllore.getCasellaAlta().getPosizione(),null);
            }
            case "Valore Basso" ->{
                System.out.println("La tempesta ha raggiunto il luogo con minor danno");
                controllore.posizionaTempesta(controllore.getCasellaBassa().getPosizione(),null);
            }
            case "Dado" ->{
                System.out.println("Tutti i giocatori tirano il dado e perdono vita");
                for(Giocatore g: controllore.getGioco().getGiocatori()) {
                    int dado = controllore.rollDice();
                    System.out.println(g.getNome() + " perde " + dado + "hp");
                    controllore.alterHp(g,-dado);
                    if(controllaMorti()!=null){
                        System.out.println(controllore.getVincitore().getNome() + "HA VINTO");
                    }
                }
                controllore.posizionaTempesta(-1,tempesta);
            }
        }
    }

    /**
     * Metodo che stampa delle richieste in base
     * alla carta Forziere usata dal giocatore
     * @param f carta Forziere usata
     * @param giocatore giocatore corrente
     */
    private void usaForziere(Forziere f,Giocatore giocatore){
        int scelta;
        switch(f.getFunzione()){
            case "Medikit" ->{
                System.out.println("Guadagni 5hp.");
                controllore.alterHp(giocatore,5);
            }
            case "Clinger" ->{
                do{
                    System.out.print("Casella su cui sparare: ");
                    try{
                        scelta = scanner.nextInt();
                        if(scelta<0||scelta>31){
                            showError(Invalid_Choice_Error);
                        }
                    }catch(InputMismatchException e){
                        scanner.nextLine();
                        showError(Input_Mismatch_Error);
                        scelta = -1;
                    }
                }while(scelta<0||scelta>31);
                System.out.println("Tutti i giocatori nella casella " + controllore.getGioco().getCasellaInPosizione(scelta).getNome() + " perdono 4hp.");
                controllore.azioneClinger(scelta,giocatore);
            }
            case "Bouncer Trap" ->{
                //casella
                int casella;
                do{
                    System.out.print("Casella su cui posizionare la Bouncer Trap: ");
                    try{
                        casella = scanner.nextInt();
                        if(casella<0||casella>31){
                            showError(Invalid_Choice_Error);
                        }
                        else if(!controllore.controllaCasellaSpara(casella,giocatore,false)){
                            showError(Invalid_Choice_Error);
                            casella = -1;
                        }
                    }catch(InputMismatchException e){
                        scanner.nextLine();
                        showError(Input_Mismatch_Error);
                        casella = -1;
                    }
                }while(casella<0||casella>31);
                //distanza a cui spararlo
                int nuovaCasella;
                do{
                    System.out.print("Casella su cui spostarlo(max 4 caselle avanti/indietro): ");
                    try {
                        nuovaCasella = scanner.nextInt();
                        if(nuovaCasella<0||nuovaCasella>31){
                            showError(Invalid_Choice_Error);
                        }
                        else if(controllore.distanzaCaselle(casella,nuovaCasella)>4){
                            showError(Invalid_Choice_Error);
                            nuovaCasella = -1;
                        }
                    }catch(InputMismatchException e){
                        scanner.nextLine();
                        showError(Input_Mismatch_Error);
                        nuovaCasella = -1;
                    }
                }while(nuovaCasella<0||nuovaCasella>31);

                Giocatore g;
                if(!controllore.getGioco().getCasellaInPosizione(casella).getGiocatori().get(0).equals(giocatore)){
                    g = controllore.getGioco().getCasellaInPosizione(casella).getGiocatori().getFirst();
                }
                else{
                    g = controllore.getGioco().getCasellaInPosizione(casella).getGiocatori().get(1);
                }
                controllore.getGioco().getCasellaInPosizione(casella).getGiocatori().remove(g);
                controllore.assegnaCasella(nuovaCasella,g);
            }
            case "Chug Jug" ->{
                System.out.println("Vita ripristinata completamente: hai 15hp.");
                controllore.alterHp(giocatore,15);
            }
        }
    }

    /**
     * Metodo che stampa delle richieste in base
     * all'azione scelta dal dado azione
     * @param a azione da eseguire
     * @param giocatore giocatore che esegue l'azione
     * @param avanza indica se, dopo l'azione, il giocatore deve avanzare
     */
    private void richiesteAzione(Azione a, Giocatore giocatore, boolean avanza){
        //BOMBA
        if(a instanceof Bomba){
            controllore.eseguiAzione(giocatore);
            //mosse
            if(avanza){
                controllore.muoviGiocatore(giocatore);
                stampaTabellone(giocatore);
            }
            System.out.print("\u001B[32mBOMBA\u001B[0m");
            showMessage("Gli altri giocatori perdono 1hp e i muri vengono distrutti.");
            if(avanza){
                azioneCasella(giocatore);
            }
        }
        //CURA
        if(a instanceof Cura){
            controllore.eseguiAzione(giocatore);
            if(avanza){
                controllore.muoviGiocatore(giocatore);
                stampaTabellone(giocatore);
            }
            System.out.print("\u001B[32mCURA\u001B[0m");
            showMessage("Guadagni 2hp.");
            if(avanza){
                azioneCasella(giocatore);
            }
        }
        //SPARA
        if(a instanceof Spara){
            //stampa tutti i numeri delle caselle
            int casella;
            System.out.print("\u001B[32mSPARA\u001B[0m");
            if(controllore.rigaVuota(giocatore)){
                if(avanza){
                    controllore.muoviGiocatore(giocatore);
                    stampaTabellone(giocatore);
                }
                showError("\nNon ci sono target a cui sparare");
            }
            else{
                do{
                    System.out.print("\nNumero casella da colpire: ");
                    try{
                        casella = scanner.nextInt();
                        if(casella<0||casella>31){
                            showError(Invalid_Choice_Error);
                        }
                        else if(!controllore.controllaCasellaSpara(casella,giocatore,true)){
                            showError("Casella non valida");
                            casella = -1;
                        }
                    }catch(InputMismatchException e){
                        scanner.nextLine();
                        showError(Input_Mismatch_Error);
                        casella = -1;
                    }
                }while(casella<0||casella>31);
                Casella c = controllore.getGioco().getCasellaInPosizione(casella);
                controllore.getGioco().setTmpCasella(c);

                if(avanza){
                    controllore.muoviGiocatore(giocatore);
                    stampaTabellone(giocatore);
                }
                if(giocatore.ispezionaCarte("Stink Bomb")){
                    showMessage("Stink Bomb: tutti i giocatori sulla tua stessa riga perdono 3hp.");
                }
                if(c.getMuro()){
                    controllore.eseguiAzione(giocatore);
                    showMessage("Muro sulla casella " + c.getPosizione() + " distrutto.");
                }
                else{
                    controllore.eseguiAzione(giocatore);
                    if(!c.getGiocatori().getFirst().equals(giocatore)){
                        showMessage("Hai tolto " + giocatore.getDanni() + "hp a " + c.getGiocatori().getFirst().getNome());
                    }
                    else{
                        showMessage("Hai tolto " + giocatore.getDanni() + "hp a " + c.getGiocatori().get(1).getNome());
                    }
                }
            }
            if(avanza){
                azioneCasella(giocatore);
            }
        }
        //MURO
        if(a instanceof Muro){
            int casella;
            do{
                System.out.print("\u001B[32mMURO\u001B[0m\nNumero casella su cui costruire il muro: ");
                try{
                    casella = scanner.nextInt();
                    if(casella<0||casella>31){
                        showError(Invalid_Choice_Error);
                    }
                    else if(controllore.getGioco().getCasellaInPosizione(casella).getMuro()){
                        showError("Muro gia' presente su questa casella");
                    }
                    else if(!controllore.casellaPassata(casella,giocatore.getCasella().getPosizione())){
                        showError("Non sei passato su questa casella");
                        casella = -1;
                    }
                }catch(InputMismatchException e){
                    scanner.nextLine();
                    showError(Input_Mismatch_Error);
                    casella = -1;
                }
            }while(casella<0||casella>31);
            controllore.getGioco().setTmpCasella(controllore.getGioco().getCasellaInPosizione(casella));
            controllore.eseguiAzione(giocatore);
            showMessage("Muro posizionato sulla casella " + casella);
        }
    }

    /**
     * Metodo che stampa delle richieste in base
     * alla casella raggiunta dal giocatore
     * @param giocatore giocatore corrente
     */
    private void azioneCasella(Giocatore giocatore){
        Casella nuovaCasella = giocatore.getCasella();

        if(nuovaCasella instanceof CasellaPrigione){
            System.out.println("\u001B[32mCasella raggiunta: \u001B[0mTransito");
        }
        else{
            System.out.println("\u001B[32mCasella raggiunta: \u001B[0m" + nuovaCasella.getNome());
        }

        //CASELLA COPERTA DALLA TEMPESTA
        if(nuovaCasella.getTempesta()){
            if(nuovaCasella instanceof CasellaCitta){
                System.out.println("Luogo raggiunto dalla tempesta : perdi " + ((CasellaCitta) nuovaCasella).getLuogo().getDanni() + "hp");
                controllore.alterHp(giocatore,-((CasellaCitta) nuovaCasella).getLuogo().getDanni());
            }
            else{
                System.out.println("Casella raggiunta dalla tempesta : perdi 2hp");
                controllore.alterHp(giocatore,-2);
            }
            showError("Non potrai eseguire l'azione della casella raggiunta");
        }
        else{
            if(nuovaCasella instanceof CasellaFalo){
                System.out.println("Guadagni 1hp");
                controllore.alterHp(giocatore,1);
            }
            if(nuovaCasella instanceof CasellaTrappola){
                System.out.println("Perdi 1hp");
                controllore.alterHp(giocatore,-1);
            }
            if(nuovaCasella instanceof CasellaForziere){
                System.out.println("Carta forziere pescata: " + controllore.pescaForziere(giocatore).getFunzione());
            }
            if(nuovaCasella instanceof CasellaCitta c){
                //nessuno ha il luogo
                if(c.getLuogo().getPossessore()==null){
                    int scelta;
                    do{
                        try {
                            System.out.print("Vuoi comprare questo luogo? (0-si, 1-no): ");
                            scelta = scanner.nextInt();
                            if(scelta!=1&&scelta!=0){
                                showError(Invalid_Choice_Error);
                            }
                        }catch(InputMismatchException e){
                            scanner.nextLine();
                            scelta = -1;
                            showError(Input_Mismatch_Error);
                        }
                    }while(scelta!=1&&scelta!=0);
                    if(scelta==0){
                        System.out.println("Hai ottenuto questo luogo.");
                        c.getLuogo().compra(giocatore);
                        giocatore.getLuoghi().add(c.getLuogo());
                    }
                }
                //possiede gia' il luogo
                else if(c.getLuogo().getPossessore().equals(giocatore)){
                    if(controllore.stessoColore(giocatore,c.getLuogo().getColor())){
                        System.out.println("Possiedi due luoghi di questo colore: guadagni 2hp.");
                        controllore.alterHp(giocatore,2);
                    }
                    else{
                        System.out.println("Possiedi già questo luogo: non perdi hp.");
                    }
                }
                else{
                    //qualcun'altro ha il luogo
                    System.out.println("Questo luogo appartiene a " + c.getLuogo().getPossessore().getNome() + ": perdi " + c.getLuogo().getDanni() + "hp.");
                    controllore.alterHp(giocatore,-c.getLuogo().getDanni());
                }
            }
            if(nuovaCasella instanceof CasellaGo || controllore.casellaPassata(0, giocatore.getCasella().getPosizione())){
                if(!controllore.getGioco().getCasellaInPosizione(0).getTempesta()){
                    System.out.println("Sei passato dal via: guadagni 2hp");
                    controllore.alterHp(giocatore,2);
                }
            }

            if(nuovaCasella instanceof CasellaVaiInPrigione){
                controllore.getGioco().getCasellaInPosizione(24).getGiocatori().remove(giocatore);
                controllore.assegnaCasella(8,giocatore);
                if(controllore.getGioco().getTabellone()[0][8].getTempesta()){
                    System.out.println("La tempesta ha raggiunto la prigione: perdi 2hp e finisci nel transito");
                    controllore.alterHp(giocatore,-2);
                }
                else{
                    System.out.println("Sei finito in prigione!");
                    giocatore.incrementaContTurni();
                }
            }
        }
    }

    /**
     * Stampa l'elenco dei nomi e dei punti vita
     * di tutti i giocatori
     */
    private void stampaVitaGiocatori(){
        showMessage("VITA GIOCATORI");
        for (Giocatore g : controllore.getGioco().getGiocatori()) {
            System.out.println(g.getNomeColorato() + ": " + g.getHp() + "hp");
        }
    }

    /**
     * Stampa se dei giocatori sono morti.
     * Controlla se un giocatore ha vinto.
     * @return Il giocatore che ha vinto
     */
    private Giocatore controllaMorti(){
        for(Giocatore g : controllore.getGioco().getGiocatori()){
            if(controllore.eliminaGiocatore(g)){
                showError(g.getNome() + " e' morto");
                giocatori--;
            }
        }
        return controllore.getVincitore();
    }

    /**
     * Stampa in rosso un messaggio d'errore
     * @param error messaggio d'errore da stampare
     */
    private void showError(String error){
        System.out.println("\u001B[31m" + error + "\u001B[0m");
    }

    /**
     * Stampa in blu un messaggio
     * @param message messaggio da stampare
     */
    private void showMessage(String message){
        System.out.println("\u001B[34m\n" + message + "\u001B[0m");
    }
}
