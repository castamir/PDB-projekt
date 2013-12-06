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
    private static final short maxY = 700;
    private static final short windowZoom = 1;

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

    public void loadShapesFromDb(List<Shape> shapes) throws SQLException, Exception {
        if (shapes == null) {
            shapes = new ArrayList<>();
        }
        
        Loader loader = new Loader();
        ServiceLocator serviceLocator = new ServiceLocator(loader.getProperties());
        
        OracleDataSource ods = serviceLocator.getConnection();
        try (Connection conn = ods.getConnection(); Statement stmt = conn.createStatement(); ResultSet resultSet = stmt.executeQuery("select nazev, geometrie from mapa")) {
            while (resultSet.next()) {
                byte[] image = resultSet.getBytes("geometrie");
                JGeometry jGeometry = JGeometry.load(image);
                Shape shape = jGeometry2Shape(jGeometry);
                if (shape != null) {
                    shapes.add(shape);
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        List<Shape> shapes = new ArrayList<>();
        Graphics2D g2D = (Graphics2D) g;
        int i = 0;
        Color[] colors = {Color.GRAY, Color.YELLOW, Color.BLUE};
        try {
            loadShapesFromDb(shapes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Iterator<Shape> iterator = shapes.iterator(); iterator.hasNext();) {
            Shape shape = iterator.next();
            g2D.setPaint(colors[i%3]);
            g2D.fill(shape);
            g2D.setPaint(Color.BLACK);
            g2D.draw(shape);
            i++;
            if(i==3){
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
