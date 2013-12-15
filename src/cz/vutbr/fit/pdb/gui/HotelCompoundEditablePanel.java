/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.vutbr.fit.pdb.gui;

import cz.vutbr.fit.pdb.models.ArealModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Pavel
 */
public class HotelCompoundEditablePanel extends javax.swing.JPanel implements MouseListener, MouseMotionListener, KeyListener {

    private Map<String, Shape> shapes;
    
    private String selectedBuilding;

    private boolean drawing = false;
    
    private ArealModel arealModel;
    
    /* aktualne vytvareny polygon */
    private Polygon newPolygon;
    private List<Point2D> points;
    private Line2D.Float currentLine;
    private String currentPolygonName;
    
    /**
     * Creates new form HotelCompoundEditablePanel
     */
    public HotelCompoundEditablePanel() {
        initComponents();
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        
        arealModel = new ArealModel();
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

    // <editor-fold defaultstate="collapsed" desc="Mouse listener methods">   
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
        }
        else {
            selectedBuilding = null;
        }
        
        repaint();
    }
    
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
    // </editor-fold> 
    
    // <editor-fold defaultstate="collapsed" desc="Mouse Motion listener methods">   
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
    // </editor-fold> 
    
    // <editor-fold defaultstate="collapsed" desc="Key listener methods">   
    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("pressed");
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.out.println("esc");
            if (points.size() > 0) {
                points.removeAll(points);
                shapes.remove(currentPolygonName);
                shapes.remove("line");   
            }
            
            drawing = false;
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
    
    // </editor-fold>
    
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
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        addBtn = new javax.swing.JButton();
        removeBtn = new javax.swing.JButton();

        jToolBar1.setRollover(true);

        addBtn.setText("+");
        addBtn.setFocusable(false);
        addBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(addBtn);

        removeBtn.setText("-");
        removeBtn.setFocusable(false);
        removeBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        removeBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        removeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(removeBtn);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(510, Short.MAX_VALUE)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        newObjectNameDialog();
    }//GEN-LAST:event_addBtnActionPerformed

    private void removeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBtnActionPerformed
        if (selectedBuilding == null) {
            JOptionPane.showMessageDialog(getParent(), "Vyberte nejprve objekt ke smazání.");
        }
        else {
            try {
                arealModel.deleteBuildingWithName(selectedBuilding);
                
                shapes.clear();
                shapes = null;
                
                repaint();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_removeBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton removeBtn;
    // End of variables declaration//GEN-END:variables
}
