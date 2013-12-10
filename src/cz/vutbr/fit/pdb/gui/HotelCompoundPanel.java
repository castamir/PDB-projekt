package cz.vutbr.fit.pdb.gui;

import cz.vutbr.fit.pdb.application.ServiceLocator;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.HashMap;
import oracle.jdbc.pool.OracleDataSource;
import oracle.spatial.geometry.JGeometry;
import java.awt.Shape;
import javax.swing.JPanel;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class HotelCompoundPanel extends JPanel implements MouseListener {

    private static final long serialVersionUID = 1L;
    //private double zoom = 1.0;
    private Sluzby parentPanel;
    private Map<String, Shape> shapes;

    public HotelCompoundPanel() {
        this.addMouseListener(this);
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

    public void loadShapesFromDb() throws SQLException, Exception {
        if (shapes == null) {
            shapes = new HashMap<>();
        }


        ServiceLocator serviceLocator = new ServiceLocator();

        OracleDataSource ods = serviceLocator.getConnection();
        try (Connection conn = ods.getConnection(); Statement stmt = conn.createStatement(); ResultSet resultSet = stmt.executeQuery("select nazev, geometrie from areal")) {
            while (resultSet.next()) {
                byte[] image = resultSet.getBytes("geometrie");
                JGeometry jGeometry = JGeometry.load(image);
                Shape shape = jGeometry2Shape(jGeometry);
                if (shape != null) {
                    shapes.put(resultSet.getString("nazev"), shape);
                    //shapes.add(shape);
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        //g.clearRect(0, 0, getWidth(), getHeight());
        super.paint(g);
        /*AffineTransform a = new AffineTransform();
         AffineTransform a2 = new AffineTransform();
         a2.setToIdentity();
         a2.translate(getParent().getWidth() - this.getWidth(), getParent().getHeight() - this.getHeight());
         a.concatenate(a2);
         a.scale(zoom, zoom);*/

        Graphics2D g2D = (Graphics2D) g;
        //super.paint(g2D);

        try {
            loadShapesFromDb();
        } catch (Exception e) {
            e.printStackTrace();
        }


        for (Map.Entry<String, Shape> entry : shapes.entrySet()) {
            //Shape shape = iterator.next();
            //g2D.setTransform(a);
            g2D.setPaint(Color.GRAY);
            g2D.fill(entry.getValue());
            g2D.setPaint(Color.BLACK);
            g2D.draw(entry.getValue());
            g2D.setColor(Color.lightGray);
            g2D.setFont(new Font("Verdana", Font.BOLD, 11));
            g2D.drawString(entry.getKey(), (int) entry.getValue().getBounds2D().getMinX() + 5, (int) entry.getValue().getBounds2D().getMaxY() - 5);
        }
    }

    private void objectAtPoint(int x, int y) throws SQLException, Exception {
        ServiceLocator serviceLocator = new ServiceLocator();

        OracleDataSource ods = serviceLocator.getConnection();
        try (Connection conn = ods.getConnection(); Statement stmt = conn.createStatement(); ResultSet resultSet = stmt.executeQuery("select nazev from areal where SDO_RELATE(geometrie, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE(" + x + "," + y + ",NULL), NULL, NULL), 'mask=contains') = 'TRUE'")) {
            while (resultSet.next()) {
                System.out.println(resultSet.getString("nazev"));
                parentPanel.updateTitle(resultSet.getString("nazev"));
            }
        }

    }

    public void setParentPanel(Sluzby panel) {
        parentPanel = panel;
    }


    /* MouseListener methods */
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse clicked at (" + e.getX() + "," + e.getY() + ")");

        try {
            this.objectAtPoint(e.getX(), e.getY());
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    // povinna implementace metod
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    /*
     public void setZoom(double zoomFactor) {
     zoom = zoomFactor;
     repaint();
     }*/
}
