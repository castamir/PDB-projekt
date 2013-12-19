package cz.vutbr.fit.pdb.application;

import cz.vutbr.fit.pdb.gui.MainWindow;

/**
 * Hlavni aplikace
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class Application{

    /**
     *
     */
    public Application() {
    }
    
    /**
     * Hlavni smycka aplikace.
     * @param args parametry z prikazove radky
     */
    public static void main(String[] args) {
        MainWindow mainWin = new MainWindow();
        mainWin.setLocationRelativeTo(null);
        mainWin.setVisible(true);
    }
}
