package cz.vutbr.fit.pdb.application;

import cz.vutbr.fit.pdb.Loader;
import cz.vutbr.fit.pdb.ServiceLocator;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import oracle.jdbc.pool.OracleDataSource;

import oracle.spatial.geometry.JGeometry;
import java.awt.Shape;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class Application extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final short maxX = 1000;
    private static final short maxY = 650;
    private static final short windowZoom = 1;
    
    //public List<Shape> shapes;
    //public HashMap<String, Shape> myShapes;

    public Application() {
    }

    public Shape jGeometry2Shape(JGeometry jGeometry) {
        Shape shape;
        switch (jGeometry.getType()) {
            case JGeometry.GTYPE_POLYGON:
                shape = jGeometry.createShape();
                break;
            default:
                return null; // TODO: throw exception
        }
        return shape;
    }

    public void loadShapesFromDb(HashMap<String, Shape> myShapes) throws SQLException, Exception {
        /*if (shapes == null) {
            shapes = new ArrayList<>();
        }*/
        if(myShapes == null) {
            myShapes = new HashMap<String, Shape>();
        }
        
        Loader loader = new Loader();
        ServiceLocator serviceLocator = new ServiceLocator(loader.getProperties());
        
        OracleDataSource ods = serviceLocator.getConnection();
        try (Connection conn = ods.getConnection(); Statement stmt = conn.createStatement(); ResultSet resultSet = stmt.executeQuery("select nazev, geometrie from mapa")) {
            while (resultSet.next()) {
                byte[] image = resultSet.getBytes("geometrie");
                String name = new String(resultSet.getBytes("nazev"), "UTF-8");
                //System.out.println(name);
                JGeometry jGeometry = JGeometry.load(image);
                Shape shape = jGeometry2Shape(jGeometry);
                /*if (shape != null) {
                    shapes.add(shape);
                }*/
                if(shape != null) {
                    myShapes.put(name, shape);
                }
            }
        }
    }

    
    @Override
    public void paint(Graphics g) {
        //shapes = new ArrayList<>();
        HashMap<String, Shape> myShapes = new HashMap<String, Shape>();
        Graphics2D g2D = (Graphics2D) g;
        Shape shape;
        int i = 0;
        Color[] colors = {Color.GRAY, Color.YELLOW, Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE, Color.PINK};
        try {
            loadShapesFromDb(myShapes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Set<Entry<String, Shape>> grabset = myShapes.entrySet();
        //Iterator<Map.Entry<String,Shape>> iterator = grabset.iterator();
        Iterator<String> iterator = myShapes.keySet().iterator();
        while( iterator.hasNext()) {
            String key = iterator.next();
            //Map.Entry<String,Shape> key = (Map.Entry<String,Shape>)iterator.next();
            //shape = key.getValue();
            shape = myShapes.get(key);
            g2D.setPaint(colors[i%7]);
            g2D.fill(shape);
            g2D.setPaint(Color.BLACK);
            g2D.draw(shape);
            i++;
            if(i==7){
                i=0;
            }
        }
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(new Application());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(maxX * windowZoom, maxY * windowZoom);
        frame.setVisible(true);
    }
}
