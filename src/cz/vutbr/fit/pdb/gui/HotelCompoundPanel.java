package cz.vutbr.fit.pdb.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.util.Map;
import java.awt.Shape;
import javax.swing.JPanel;

import cz.vutbr.fit.pdb.models.ArealModel;

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
    
    private ArealModel arealModel;

    public HotelCompoundPanel() {
        this.addMouseListener(this);
        arealModel = new ArealModel();
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

        if (shapes == null) {
            try {
                shapes = arealModel.loadShapes();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public void setParentPanel(Sluzby panel) {
        parentPanel = panel;
    }


    /* MouseListener methods */
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse clicked at (" + e.getX() + "," + e.getY() + ")");

        try {
            String buildingName = arealModel.getBuildingAtPoint(e.getX(), e.getY());

            System.out.println(buildingName);
            parentPanel.updateTitle(buildingName);    
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
