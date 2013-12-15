package cz.vutbr.fit.pdb.application;

import cz.vutbr.fit.pdb.gui.MainWindow;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class Application{

    public Application() {
    }
    
    public static void main(String[] args) {
        /*JFrame frame = new JFrame();
         Application myApp = new Application();
        
         Sluzby sluzby = new Sluzby();
         //frame.getContentPane().add(sluzby);
         frame.getContentPane().add(sluzby);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setSize(maxX * windowZoom, maxY * windowZoom);
         frame.setVisible(true);*/
        MainWindow mainWin = new MainWindow();
        mainWin.setLocationRelativeTo(null);
        mainWin.setVisible(true);
    }
}
