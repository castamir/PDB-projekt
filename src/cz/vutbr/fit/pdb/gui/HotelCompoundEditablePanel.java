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
import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class HotelCompoundEditablePanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    private static final long serialVersionUID = 1L;
    //private double zoom = 1.0;
    private Sluzby parentPanel;
    private Map<String, Shape> shapes;
    
    private ArealModel arealModel;
    
    private String selectedBuilding;

    private boolean drawing = false;
    
    
    /* nove polygonu */
    private Map<String,Shape> newShapes;
    
    /* aktualne vytvareny polygon */
    private Polygon newPolygon;
    private List<Point2D> points;
    private Line2D.Float currentLine;
    private String currentPolygonName;
    
    public HotelCompoundEditablePanel() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        
        arealModel = new ArealModel();
        
        JButton addButton = new JButton("+");
        //addButton.setPreferredSize(new Dimension(10,10));
        addButton.setBounds(200, 20, 10, 10);
        
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newObjectNameDialog();
            }
        });
        
        JButton addButton2 = new JButton("-");
        //addButton.setPreferredSize(new Dimension(10,10));
        addButton2.setBounds(250, 20, 10, 10);
        this.add(addButton2);
    }

    @Override
    public void paint(Graphics g) {
        
        super.paint(g);

        Graphics2D g2D = (Graphics2D) g;
        
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
            Color background = Color.darkGray;
            Color fontColor = Color.lightGray;
            Color borderColor = Color.black;
            
            if (selectedBuilding != null && selectedBuilding.equals(entry.getKey())) {
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
            
            //parentPanel.updateTitle(buildingName);    
            selectedBuilding = buildingName;
            
            repaint();
        }
    }

    // povinna implementace metod
    @Override
    public void mouseEntered(MouseEvent e) {
        requestFocus();
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        
        if (drawing == false)
            return;
        
        
        System.out.println("pressed");
        
        if (points == null) {
            points = new ArrayList<>();
        }
        
        points.add(new Point2D.Float(e.getX(), e.getY()));
        // polygon
        
        int i=0;
        int npoints = points.size();
        int[] xpoints = new int[npoints];
        int[] ypoints = new int[npoints];
        for (Point2D point : points) {
            xpoints[i] = (int)point.getX();
            ypoints[i] = (int)point.getY();
            i++;
        }
        newPolygon = new Polygon(xpoints, ypoints, npoints);
        
        if (shapes.get(currentPolygonName) != null) {
            shapes.remove(currentPolygonName);
        }
        shapes.remove("line");
        
        shapes.put(currentPolygonName, newPolygon);
        
        repaint();
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
    
    
    /* mouse motion listener methods */
    
    @Override
    public void mouseMoved(MouseEvent e) {
        
        if (drawing == false)
            return;
        
        System.out.println("motion");
        
       if (points == null || points.size() == 0)
           return;
        
        Point2D point = points.get(points.size()-1);
        Point2D point2 = new Point2D.Float((float)e.getX(), (float)e.getY());
        
        currentLine = new Line2D.Float(point, point2);
        if (shapes.get("line") != null) {
            shapes.remove("line");
        }
        
        shapes.put("line", currentLine);
        repaint();
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        
    }
    
    /* key listener */
    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("pressed");
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.out.println("esc");
            if (points.size() > 0) {
                points.removeAll(points);
                shapes.remove("new");
                shapes.remove("line");   
            }
            else {
                drawing = false;
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            
            points.removeAll(points);
            shapes.remove("line");
            drawing = false;
        }
        
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    
    /* dialog pro ziskani nazvu kresleneho objektu */
    public void newObjectNameDialog() {
    
        String s = (String)JOptionPane.showInputDialog(
                    getParent(),
                    "Zadejte název nového objektu:\n"
                    + "(název musí být unikátní)",
                    "Nový objekt",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "Nový objekt");
        
        if (s != null) {
            currentPolygonName = s;
            drawing = true;
        }
    }
    
    
    
}
