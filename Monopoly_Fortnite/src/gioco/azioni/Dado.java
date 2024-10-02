package gioco.azioni;

import java.io.Serializable;

public class Dado implements Serializable {
    /**
     * Metodo statico che genera un numero random da 1 a 6
     * @return il numero random
     */
    public static int rollDice(){
        return (int)(Math.random()*6)+1;
    }

    /**
     * Metodo statico che genera un numero random da 1 a 6
     * e in base a quello crea un'Azione casuale
     * @return l'azione generata
     */
    public static Azione rollActionDice() {
         return switch((int)(Math.random()*6)+1){
            case 1,2-> new Spara();
            case 3,4-> new Cura();
            case 5-> new Bomba();
            case 6-> new Muro();
            default -> null;
        };
    }
}
