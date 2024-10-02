package gioco.carte;

import java.io.Serializable;

public class Forziere implements Serializable {
    private final String funzione;
    private final Tipo tipo;

    /**
     * Costruttore
     * @param funzione nome della carta che indica l'azione che svolge
     * @param tipo Tipo di carta forziere (usa e getta / permanente)
     */
    public Forziere(String funzione, Tipo tipo) {
        this.funzione = funzione;
        this.tipo = tipo;
    }

    /**
     * Getter della stringa contenente la funzione della carta
     * @return La funzione della carta
     */
    public String getFunzione() {
        return funzione;
    }

    /**
     * Getter del tipo di forziere
     * @return Tipo di forziere
     */
    public Tipo getTipo() {
        return tipo;
    }
}
