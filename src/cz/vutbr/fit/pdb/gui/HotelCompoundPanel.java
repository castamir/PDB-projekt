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
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Třída pro zobrazování a vybírání objektů z tabulky 'areal'. Slouží pro výběr rezervací služeb - tam kd eje dostupno. Pokud v daném objektu nejsou žádné služby dostupné, nelze tento objekt vybrat.
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class HotelCompoundPanel extends JPanel implements MouseListener {

    private static final long serialVersionUID = 1L;
    //private double zoom = 1.0;
    private Sluzby parentPanel;
    private Map<String, Object> shapes;
    private ArealModel arealModel;
    private String selectedBuilding;
    private static final String[] includeBuildings = {"Tenisové kurty", "Hlídání dětí", "Golfové hřiště", "Wellness"};

    /**
     *
     */
    public HotelCompoundPanel() {
        this.addMouseListener(this);
        arealModel = new ArealModel();
    }

    /**
     * Vykreslí objekty
     * @param g
     */
    @Override
    public void paint(Graphics g) {

        super.paint(g);

        Graphics2D g2D = (Graphics2D) g;

        //g2D.translate(horizontalOffset, verticalOffset);

        loadShapes();

        for (Map.Entry<String, Object> entry : shapes.entrySet()) {
            if (entry.getValue() instanceof Shape) {
                drawShape(g2D, entry.getKey(), (Shape)entry.getValue());
            }
            else {
                drawPoint(g2D, entry.getKey(), (Point2D)entry.getValue());
            }
        }
    }
    
    private void drawPoint(Graphics2D g2D, String name, Point2D point) {
    
        float r = (float)2;
        
        Ellipse2D tmp = new Ellipse2D.Float((float)point.getX()-r, (float)point.getY()-r, 2*r, 2*r);
        
        drawShape(g2D, name, tmp);
    }

    private void drawShape(Graphics2D g2D, String name, Shape shape) {
        Color background = Color.darkGray;
        Color fontColor = Color.lightGray;
        Color borderColor = Color.black;
        
        if (selectedBuilding != null && selectedBuilding.equals(name)) {
            background = Color.green;
            fontColor = Color.darkGray;    
        }
        else if (!Arrays.asList(includeBuildings).contains(name)) {
            background = Color.gray;
        }

        g2D.setPaint(background);
        
        if ((shape instanceof Rectangle2D) || (shape instanceof GeneralPath) || (shape instanceof Ellipse2D)) {
            g2D.fill(shape);
        }
        
        g2D.setPaint(borderColor);
        g2D.draw(shape);
        g2D.setColor(fontColor);
        g2D.setFont(new Font("Verdana", Font.BOLD, 11));
        
        g2D.drawString(name, (int) shape.getBounds2D().getMinX() + 5, (int) shape.getBounds2D().getMaxY() - 5);
    }

    /**
     *
     * @param panel
     */
    public void setParentPanel(Sluzby panel) {
        parentPanel = panel;
    }
    
    /**
     * Aktualizuje data z databáze.
     */
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
        if (shapes != null) {
            shapes.clear();
            shapes = null;
        }
        
        selectedBuilding = null;
        
        loadShapes();
    }


    /* MouseListener methods */
    /**
     * Výběr objektů kliknutím myši.
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        String buildingName = null;
        try {
            buildingName = arealModel.getBuildingAtPoint(e.getX(), e.getY());
        } catch (SQLException ex) {
            Logger.getLogger(HotelCompoundPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(HotelCompoundPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (buildingName != null) {

            if (Arrays.asList(includeBuildings).contains(buildingName)) {
                parentPanel.updateTitle(buildingName);
                selectedBuilding = buildingName;
            }

            this.repaint();
        }
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     *
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }
}
