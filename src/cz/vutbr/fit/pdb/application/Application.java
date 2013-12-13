package cz.vutbr.fit.pdb.application;

import cz.vutbr.fit.pdb.gui.MainWindow;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import oracle.jdbc.pool.OracleDataSource;
import oracle.spatial.geometry.JGeometry;
import java.awt.Shape;
import java.util.HashMap;
import javax.swing.JPanel;

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
        mainWin.setVisible(true);
    }
}
