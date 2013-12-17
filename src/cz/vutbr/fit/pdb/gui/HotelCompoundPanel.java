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
import java.util.ArrayList;
import java.util.Arrays;

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
    
    private String selectedBuilding;
    
    private static final String[] excludeBuildings = {"Hotel","Bazén","Bar+Disko","Služby u bazénu"};

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

        loadShapes();


        for (Map.Entry<String, Shape> entry : shapes.entrySet()) {
            //Shape shape = iterator.next();
            //g2D.setTransform(a);
            Color background = Color.darkGray;
            Color fontColor = Color.lightGray;
            Color borderColor = Color.black;
            
            if (Arrays.asList(excludeBuildings).contains(entry.getKey())) {
                background = Color.gray;
            }
            else if (selectedBuilding != null && selectedBuilding.equals(entry.getKey())) {
                background = Color.green;
                fontColor = Color.darkGray;
            }
            
            g2D.setPaint(background);
            g2D.fill(entry.getValue());
            g2D.setPaint(borderColor);
            g2D.draw(entry.getValue());
            g2D.setColor(fontColor);
            g2D.setFont(new Font("Verdana", Font.BOLD, 11));
            g2D.drawString(entry.getKey(), (int) entry.getValue().getBounds2D().getMinX() + 5, (int) entry.getValue().getBounds2D().getMaxY() - 5);
        }
    }

    public void setParentPanel(Sluzby panel) {
        parentPanel = panel;
    }
    
    public void update() {
        reloadShapes();
    }
    
    private void loadShapes() {
        if (shapes == null) {
            try {
                shapes = arealModel.loadShapes();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void reloadShapes() {
        shapes.clear();
        shapes = null;
        
        selectedBuilding = null;
        
        loadShapes();
    }


    /* MouseListener methods */
    @Override
    public void mouseClicked(MouseEvent e) {
        String buildingName = null;
        
        try {
            buildingName = arealModel.getBuildingAtPoint(e.getX(), e.getY());
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        
        if (buildingName != null) {
            
            if (!Arrays.asList(excludeBuildings).contains(buildingName)) {
                parentPanel.updateTitle(buildingName);    
                selectedBuilding = buildingName;
            }
            
            this.repaint();
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
