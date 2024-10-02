package controllore;

import gioco.azioni.*;
import gioco.carte.Forziere;
import gioco.casella.*;
import gioco.giocatore.Giocatore;
import org.junit.jupiter.api.*;

class ControlloreTest {
    Controllore controllore = new Controllore();

    @Test
    void rollDice() {
        int dado = controllore.rollDice();
        Assertions.assertTrue(dado>0 && dado<7);
    }

    @Test
    void rollActionDice() {
        Assertions.assertNotNull(controllore.rollActionDice());
    }

    @Test
    void alterHp() {
        Giocatore g = new Giocatore("nome",1);
        try {
            controllore.alterHp(g,-4);
        } catch (Exception ignore) {}
        Assertions.assertEquals(11,g.getHp());
    }

    @Test
    void creaGiocatore() {
        controllore.creaGiocatore("Nome");
        Assertions.assertEquals("Nome", controllore.getGioco().getGiocatori().getLast().getNome());
        Assertions.assertFalse(controllore.creaGiocatore("Nome"));
    }

    @Test
    void eliminaGiocatore() {
        Giocatore giocatore1 = new Giocatore("nome",1);
        controllore.assegnaCasella(3,giocatore1);
        Giocatore giocatore2 = new Giocatore("nome2",2);
        controllore.assegnaCasella(5,giocatore2);

        controllore.getGioco().getGiocatori().add(giocatore1);
        controllore.getGioco().getGiocatori().add(giocatore2);

        giocatore1.decreaseHp(15);
        Assertions.assertTrue(controllore.eliminaGiocatore(giocatore1));
        Assertions.assertFalse(controllore.eliminaGiocatore(giocatore2));
    }

    @Test
    void casellaPassata() {
        Giocatore g = new Giocatore("nome",1);
        controllore.assegnaCasella(31,g);

        controllore.rollDice();
        controllore.muoviGiocatore(g);
        Assertions.assertTrue(controllore.casellaPassata(0,g.getCasella().getPosizione()));
    }

    @Test
    void getVincitore() {
        Giocatore giocatore = new Giocatore("nome",1);
        controllore.getGioco().getGiocatori().add(giocatore);
        Assertions.assertEquals(giocatore, controllore.getVincitore());
    }

    @Test
    void controllaCasellaSpara(){
        Giocatore giocatore1 = new Giocatore("nome",1);
        controllore.assegnaCasella(3,giocatore1);
        Giocatore giocatore2 = new Giocatore("nome2",2);
        controllore.assegnaCasella(5,giocatore2);
        Giocatore giocatore3 = new Giocatore("nome3",3);
        controllore.assegnaCasella(20,giocatore3);

        //spara solo al giocatore2 perche' sono sulla stessa riga
        Assertions.assertTrue(controllore.controllaCasellaSpara(5,giocatore1,true));
        Assertions.assertFalse(controllore.controllaCasellaSpara(20,giocatore1,true));

        Assertions.assertFalse(controllore.controllaCasellaSpara(1,giocatore1,false));
    }

    @Test
    void rigaVuota() {
        Giocatore giocatore1 = new Giocatore("nome",1);
        controllore.assegnaCasella(3,giocatore1);
        Assertions.assertTrue(controllore.rigaVuota(giocatore1));

        Giocatore giocatore2 = new Giocatore("nome2",2);
        controllore.assegnaCasella(6,giocatore2);
        Assertions.assertFalse(controllore.rigaVuota(giocatore1));
    }

    @Test
    void stessoColore(){
        Giocatore giocatore = new Giocatore("nome",1);
        controllore.assegnaCasella(3,giocatore);
        if(controllore.getGioco().getTabellone()[0][1] instanceof CasellaCitta && controllore.getGioco().getTabellone()[0][3] instanceof CasellaCitta){
            ((CasellaCitta) controllore.getGioco().getTabellone()[0][1]).getLuogo().compra(giocatore);
            giocatore.getLuoghi().add(((CasellaCitta) controllore.getGioco().getTabellone()[0][1]).getLuogo());
            ((CasellaCitta) controllore.getGioco().getTabellone()[0][3]).getLuogo().compra(giocatore);
            giocatore.getLuoghi().add(((CasellaCitta) controllore.getGioco().getTabellone()[0][3]).getLuogo());

            Assertions.assertTrue(controllore.stessoColore(giocatore, ((CasellaCitta) controllore.getGioco().getTabellone()[0][1]).getLuogo().getColor()));
        }
    }

    @Test
    void assegnaCasella() {
        Giocatore giocatore = new Giocatore("nome",1);
        controllore.assegnaCasella(5,giocatore);
        Assertions.assertEquals(5,giocatore.getCasella().getPosizione());
    }

    @Test
    void muoviGiocatore() {
        Giocatore g = new Giocatore("nome",1);
        controllore.assegnaCasella(5,g);
        int dado = controllore.rollDice();
        controllore.muoviGiocatore(g);
        Assertions.assertEquals(5+dado,g.getCasella().getPosizione());

        controllore.assegnaCasella(31,g);
        dado = controllore.rollDice();
        controllore.muoviGiocatore(g);
        Assertions.assertEquals(dado-1,g.getCasella().getPosizione());
    }

    @Test
    void distanzaCaselle() {
        Assertions.assertEquals(8, controllore.distanzaCaselle(12,20));
        Assertions.assertEquals(2, controllore.distanzaCaselle(31,1));
    }

    @Test
    void eseguiAzione() {
        Azione azione = controllore.rollActionDice();
        controllore.creaGiocatore("nome");
        controllore.creaGiocatore("nome2");

        Giocatore giocatore = controllore.getGioco().getGiocatori().get(0);
        Giocatore g2 = controllore.getGioco().getGiocatori().get(1);

        controllore.assegnaCasella(17,giocatore);
        controllore.assegnaCasella(19,g2);
        giocatore.decreaseHp(10);

        controllore.getGioco().setTmpCasella(controllore.getGioco().getCasellaInPosizione(19));

        controllore.eseguiAzione(giocatore);
        if(azione instanceof Cura){
            Assertions.assertEquals(7,giocatore.getHp());
        }
        if(azione instanceof Bomba){
            Assertions.assertEquals(14,g2.getHp());
            Assertions.assertEquals(5,giocatore.getHp());
        }
        if(azione instanceof Spara){
            Assertions.assertEquals(14,g2.getHp());
        }
        if(azione instanceof Muro){
            Assertions.assertTrue(controllore.getGioco().getCasellaInPosizione(19).getMuro());
        }
    }

    @Test
    void azioneClinger(){
        Giocatore giocatore = new Giocatore("nome",1);
        controllore.assegnaCasella(2,giocatore);
        Giocatore giocatore2 = new Giocatore("nome2",2);
        controllore.assegnaCasella(15,giocatore2);

        controllore.azioneClinger(15,giocatore);
        Assertions.assertEquals(11,giocatore2.getHp());
    }

    @Test
    void usaCarta() {
        Giocatore giocatore = new Giocatore("nome",1);
        controllore.assegnaCasella(4,giocatore);
        Forziere forziere = controllore.pescaForziere(giocatore);
        controllore.usaCarta(giocatore,forziere);

        Assertions.assertEquals(16, controllore.getGioco().getMazzoForzieri().size());
        Assertions.assertTrue(controllore.getGioco().getMazzoForzieri().contains(forziere));
    }

    @Test
    void pescaForziere() {
        Giocatore giocatore = new Giocatore("nome",1);
        controllore.assegnaCasella(2,giocatore);
        Forziere forziere = controllore.pescaForziere(giocatore);
        Forziere forziere2 = controllore.pescaForziere(giocatore);

        Assertions.assertFalse(controllore.getGioco().getMazzoForzieri().contains(forziere));
        Assertions.assertFalse(controllore.getGioco().getMazzoForzieri().contains(forziere2));
        Assertions.assertEquals(14, controllore.getGioco().getMazzoForzieri().size());
    }

    @Test
    void pescaTempesta() {
        controllore.pescaTempesta();

        Assertions.assertEquals(14, controllore.getGioco().getMazzoTempesta().size());
    }

    @Test
    void posizionaTempesta(){
        controllore.posizionaTempesta(0,null);
        Assertions.assertTrue(controllore.getGioco().getTabellone()[0][0].getTempesta());
    }

    @Test
    void getCasellaAlta() {
        Casella casella = controllore.getCasellaAlta();
        Assertions.assertTrue(casella instanceof CasellaCitta);

        CasellaCitta casella2 = (CasellaCitta) casella;
        Assertions.assertEquals(4,casella2.getLuogo().getDanni());
    }

    @Test
    void getCasellaBassa() {
        Casella casella = controllore.getCasellaBassa();

        Assertions.assertTrue(casella instanceof CasellaCitta);

        CasellaCitta casella2 = (CasellaCitta) casella;
        Assertions.assertEquals(1,casella2.getLuogo().getDanni());
    }

    @Test
    void cittaSenzaTempesta() {
        Giocatore giocatore = new Giocatore("Nome",1);
        controllore.getGioco().getGiocatori().add(giocatore);
        if(controllore.getGioco().getTabellone()[0][1] instanceof CasellaCitta){
            ((CasellaCitta) controllore.getGioco().getTabellone()[0][1]).getLuogo().compra(giocatore);
            giocatore.getLuoghi().add(((CasellaCitta) controllore.getGioco().getTabellone()[0][1]).getLuogo());
        }
        Assertions.assertTrue(controllore.cittaSenzaTempesta(giocatore));
    }

    @Test
    void playAudio() {
        Assertions.assertDoesNotThrow(() -> controllore.playAudio("/audio/theme.wav"));
    }

    @Test
    void salvaPartita() {
        controllore.salvaPartita();
        Assertions.assertTrue(controllore.caricaPartita());
    }

    @Test
    void caricaPartita() {
        controllore.getGioco().cambiaTurno();
        controllore.salvaPartita();

        controllore.caricaPartita();
        Assertions.assertEquals(1,controllore.getGioco().getTurno());
    }
}