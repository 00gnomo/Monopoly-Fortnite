package gioco.casella;

import java.io.Serializable;

public class CasellaTempesta extends Casella implements Serializable {
    public CasellaTempesta(String nome, int x, int y, int posizione) {
        super(nome, x, y, posizione);
    }
}
