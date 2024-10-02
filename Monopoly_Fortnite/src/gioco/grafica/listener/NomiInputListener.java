package gioco.grafica.listener;

import controllore.Controllore;
import gioco.grafica.*;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class NomiInputListener implements ActionListener {
    private final JTextField[] nomiFields;
    private final JFrame frame;
    private final Controllore controllore;

    /**
     * Costruttore
     * @param nomiFields JTextField dove l'utente inserisce i nomi dei giocatori
     * @param frame frame da eliminare
     * @param controllore controllore che gestisce il gioco
     */
    public NomiInputListener(JTextField[] nomiFields, JFrame frame, Controllore controllore){
        this.nomiFields = nomiFields;
        this.frame = frame;
        this.controllore = controllore;
    }

    /**
     * Se sono presenti meno di 2 nomi o
     * se ci sono nomi doppi stampa un errore.
     * Altrimenti crea dei nuovi giocatori
     * coi nomi inseriti.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Grafica g;
        ArrayList<String> listaNomi = new ArrayList<>();
        boolean flag = true;
        int cont = 0;
        for(int i=0;i<7;i++){
            if(nomiFields[i].getText().matches(".+")){
                cont++;
                listaNomi.add(nomiFields[i].getText());
            }
        }

        if(cont>=2){//controllo del numero minimo di giocatori inseriti in caso contrario viene mostrato un messaggio (messaggio 2)
            for(String nome : listaNomi){//foreach che prende i nomi in ingresso al click del bottone conferma
                flag = controllore.creaGiocatore(nome);
                if(!flag){//se il flag è false il nome non è valido, si esce dal ciclo e viene mostrato un messaggio (messaggio 1)
                    break;
                }
            }

            if(flag){
                g = new Gui(controllore);
                g.start();
                frame.dispose();
            }
            else{
                JOptionPane.showMessageDialog(null,"Sono presenti dei nomi uguali");//(messaggio 1)
                controllore.getGioco().getGiocatori().clear();
            }
        }
        else{
            JOptionPane.showMessageDialog(null,"Inserire almeno 2 giocatori");//(messaggio 2)
        }
    }
}
