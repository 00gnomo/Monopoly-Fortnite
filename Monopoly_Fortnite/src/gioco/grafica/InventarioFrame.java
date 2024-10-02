package gioco.grafica;

import controllore.Controllore;
import gioco.giocatore.Giocatore;
import gioco.grafica.listener.InventarioListener;

import javax.swing.*;
import java.awt.*;
import java.util.ConcurrentModificationException;

public class InventarioFrame extends JFrame implements Grafica{

    private final Controllore controllore;
    private final Gui gui;
    private final InventarioListener inventarioListener;

    /**
     * Costruttore
     * @param controllore controllore che gestisce il gioco
     * @param gui Gui
     */
    public InventarioFrame(Controllore controllore, Gui gui){
        this.controllore = controllore;
        this.gui = gui;
        this.inventarioListener = new InventarioListener(controllore,gui,this);
    }

    /**
     * Metodo che crea un frame dove è mostrato
     * l'inventario di un giocatore
     */
    @Override
    public void start(){
        int turno = controllore.getGioco().getTurno()%controllore.getGioco().getGiocatori().size();
        Giocatore giocatore = controllore.getGioco().getGiocatori().get(turno);
        Container container = getContentPane();

        int size=0;

        for(int i=0;i<giocatore.getCarte().size();i++){
            switch(giocatore.getCarte().get(i).getFunzione()){
                case "Clinger","Medikit","Chug Jug":
                    size++;
                    break;
                default:
                    break;
            }
        }

        container.setLayout(new GridLayout(size+2,1));
        container.add(new JLabel("Vuoi usare qualche carta?"));

        //pulsanti
        JButton[] buttons=new JButton[giocatore.getCarte().size()];
        JButton none=new JButton("Passa il turno");

        for(int i=0;i<giocatore.getCarte().size();i++){
            switch(giocatore.getCarte().get(i).getFunzione()){
                case "Clinger","Medikit","Chug Jug":
                    buttons[i] = new JButton();
                    buttons[i].setText(giocatore.getCarte().get(i).getFunzione());
                    buttons[i].addActionListener(inventarioListener);
                    container.add(buttons[i]);
                    break;
            }
        }
        //action listeners
        container.add(none);

        none.addActionListener(e -> {
            dispose();
            controllore.getGioco().cambiaTurno();
            try{
                if(controllore.getVincitore()==null){
                    gui.refreshTurno(controllore.getGioco().getTurno()%controllore.getGioco().getGiocatori().size());
                    gui.refreshHealth();
                }
            }catch(ConcurrentModificationException ignore){}
        });

        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        setSize(400,200);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (dim.getWidth()-getWidth())/2,(int) (dim.getHeight()-getHeight())/2);
        setVisible(true);
    }

}
