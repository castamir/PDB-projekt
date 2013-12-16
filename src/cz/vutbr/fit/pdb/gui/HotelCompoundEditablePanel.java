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
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Pavel
 */
public class HotelCompoundEditablePanel extends javax.swing.JPanel implements MouseListener, MouseMotionListener, KeyListener {

    private Map<String, Shape> shapes;
    private Map<String, Shape> newShapes;
    
    private List<String> buildingsToDelete;
    
    private String selectedBuilding;

    private boolean drawing = false;
    
    private ArealModel arealModel;
    
    
    /* aktualne vytvareny tvar */
    private List<Point2D> points;
    private Line2D.Float currentLine;    
    private String currentPolygonName;
    
    private Polygon newPolygon;
    private Path2D newPath;
    private Ellipse2D newEllipse;
   
    
    private final String tmpLineKey = "tmpLinePDB";
    
    private final int verticalOffset = 30;
    private final int horizontalOffset = 0;
    
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
        
        g2D.translate(horizontalOffset, verticalOffset);
        
        loadShapes();

        for (Map.Entry<String, Shape> entry : shapes.entrySet()) {
            drawShape(g2D, entry, false);
        }
        
        for (Map.Entry<String, Shape> entry : newShapes.entrySet()) {
            drawShape(g2D, entry, true);
        }
    }
    
    private void drawShape(Graphics2D g2D, Map.Entry<String, Shape> entry, boolean newShape) {
        Color background = Color.darkGray;
        Color fontColor = Color.lightGray;
        Color borderColor = Color.black;

        if (selectedBuilding != null && selectedBuilding.equals(entry.getKey())) {
            background = Color.green;
            fontColor = Color.darkGray;
        }
        else if (newShape) {
            background = Color.yellow;
        }

        g2D.setPaint(background);
        g2D.fill(entry.getValue());
        g2D.setPaint(borderColor);
        g2D.draw(entry.getValue());
        g2D.setColor(fontColor);
        g2D.setFont(new Font("Verdana", Font.BOLD, 11));

        if (!entry.getKey().equals(tmpLineKey))
            g2D.drawString(entry.getKey(), (int) entry.getValue().getBounds2D().getMinX() + 5, (int) entry.getValue().getBounds2D().getMaxY() - 5);
    }

    // <editor-fold defaultstate="collapsed" desc="Mouse listener methods">   
    @Override
    public void mouseClicked(MouseEvent e) {
        
        if (drawing)
            return;
        
        String buildingName = null;
        
        try {
            buildingName = arealModel.getBuildingAtPoint(e.getX()-horizontalOffset, e.getY()-verticalOffset);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        
        if (buildingName != null) {
            selectedBuilding = buildingName;
        }
        else {
            
            selectedBuilding = null;
            
            for (Map.Entry<String, Shape> entry : newShapes.entrySet()) {
                
                Shape obj = entry.getValue();
                
                if (obj.contains(e.getX()-horizontalOffset, e.getY()-verticalOffset)) {
                    selectedBuilding = entry.getKey();
                    break;
                }
            } 
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
        

        newShapes.remove(currentPolygonName);
        newShapes.remove(tmpLineKey);
        
        if (objectTypeEllipse.isSelected()) {
        
            newEllipse = new Ellipse2D.Float(e.getX()-horizontalOffset, e.getY()-verticalOffset, 0, 0);
            
            newShapes.put(currentPolygonName, newEllipse);
        }
        
        else {
            if (points == null) {
                points = new ArrayList<>();
            }

            points.add(new Point2D.Float(e.getX()-horizontalOffset, e.getY()-verticalOffset));
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

            if (objectTypeLine.isSelected()) {
                newPath = new Path2D.Float(newPolygon);
            }

            if (objectTypeLine.isSelected()) {
                newShapes.put(currentPolygonName, newPath);
                System.out.println("line");
            }
            else
                newShapes.put(currentPolygonName, newPolygon);
        }
        
        repaint();
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        
        if (objectTypeEllipse.isSelected()) {
        
            if (newEllipse.getWidth() == 0 || newEllipse.getHeight() == 0) {
                newShapes.remove(currentPolygonName);
                return;
            }
            
            drawing = false;
            newEllipse = null;
            
            repaint();
        }
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
        
       if (objectTypeEllipse.isSelected()) {
           int w, h;
           
           w = (int)((e.getX()-horizontalOffset) - newEllipse.getX());
           h = (int)((e.getY()-verticalOffset) - newEllipse.getY());
           
           newEllipse.setFrame(newEllipse.getX(), newEllipse.getY(), w, h);
                   
           newShapes.put(currentPolygonName, newEllipse);
       }
       else {
            if (points == null || points.size() == 0)
                return;

             Point2D point = points.get(points.size()-1);
             Point2D point2 = new Point2D.Float((float)e.getX()-horizontalOffset, (float)e.getY()-verticalOffset);

             currentLine = new Line2D.Float(point, point2);
             if (newShapes.get(tmpLineKey) != null) {
                 newShapes.remove(tmpLineKey);
             }

             newShapes.put(tmpLineKey, currentLine);
       }
        
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
                newShapes.remove(currentPolygonName);
                newShapes.remove(tmpLineKey);   
            }
            
            drawing = false;
        }
        else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            
            points.removeAll(points);
            newShapes.remove(tmpLineKey);
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
            
            if (shapes.get(s) != null || newShapes.get(s) != null) {
                JOptionPane.showMessageDialog(getParent(), "Objekt se zadaným názvem již existuje. Zvolte jiný název.");
                
                newObjectNameDialog();
            }
            else {
                currentPolygonName = s;
                drawing = true;
            }
        }
    }
    
    
    private void loadShapes() {
        if (shapes == null) {
            try {
                shapes = arealModel.loadShapes();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if (newShapes == null) {
            newShapes = new LinkedHashMap<>();
        }
        
        if (buildingsToDelete == null) {
            buildingsToDelete = new ArrayList<>();
        }
    }
    
    private void reloadShapes() {
        shapes.clear();
        shapes = null;
        
        newShapes.clear();
        newShapes = null;
        
        buildingsToDelete.removeAll(buildingsToDelete);
        
        selectedBuilding = null;
        
        loadShapes();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        objectTypeButtonGroup = new javax.swing.ButtonGroup();
        jToolBar1 = new javax.swing.JToolBar();
        addBtn = new javax.swing.JButton();
        removeBtn = new javax.swing.JButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(30, 0), new java.awt.Dimension(30, 0), new java.awt.Dimension(30, 32767));
        objectTypeLine = new javax.swing.JRadioButton();
        objectTypePolygon = new javax.swing.JRadioButton();
        objectTypeEllipse = new javax.swing.JRadioButton();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(50, 0), new java.awt.Dimension(50, 0), new java.awt.Dimension(50, 32767));
        saveBtn = new javax.swing.JButton();
        cancelChangesBtn = new javax.swing.JButton();

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
        jToolBar1.add(filler2);

        objectTypeButtonGroup.add(objectTypeLine);
        objectTypeLine.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        objectTypeLine.setText("Line");
        objectTypeLine.setFocusable(false);
        objectTypeLine.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        objectTypeLine.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        objectTypeLine.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(objectTypeLine);

        objectTypeButtonGroup.add(objectTypePolygon);
        objectTypePolygon.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        objectTypePolygon.setSelected(true);
        objectTypePolygon.setText("Polygon");
        objectTypePolygon.setFocusable(false);
        objectTypePolygon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        objectTypePolygon.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        objectTypePolygon.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(objectTypePolygon);

        objectTypeButtonGroup.add(objectTypeEllipse);
        objectTypeEllipse.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        objectTypeEllipse.setText("Ellipse");
        objectTypeEllipse.setFocusable(false);
        objectTypeEllipse.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        objectTypeEllipse.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        objectTypeEllipse.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(objectTypeEllipse);
        jToolBar1.add(filler3);

        saveBtn.setText("Uložit");
        saveBtn.setFocusable(false);
        saveBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(saveBtn);

        cancelChangesBtn.setText("Zrušit změny");
        cancelChangesBtn.setFocusable(false);
        cancelChangesBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cancelChangesBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cancelChangesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelChangesBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(cancelChangesBtn);

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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(508, Short.MAX_VALUE))
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
            
            if (shapes.get(selectedBuilding) != null) {
                shapes.remove(selectedBuilding);
                buildingsToDelete.add(selectedBuilding);
            }
            else if (newShapes.get(selectedBuilding) != null)
                newShapes.remove(selectedBuilding);
            
            selectedBuilding = null;
            
            repaint();
            
            /*try {
                arealModel.deleteBuildingWithName(selectedBuilding);
                
                reloadShapes();
                
                repaint();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }*/
        }
    }//GEN-LAST:event_removeBtnActionPerformed

    private void cancelChangesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelChangesBtnActionPerformed
        reloadShapes();
        repaint();
    }//GEN-LAST:event_cancelChangesBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JButton cancelChangesBtn;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.ButtonGroup objectTypeButtonGroup;
    private javax.swing.JRadioButton objectTypeEllipse;
    private javax.swing.JRadioButton objectTypeLine;
    private javax.swing.JRadioButton objectTypePolygon;
    private javax.swing.JButton removeBtn;
    private javax.swing.JButton saveBtn;
    // End of variables declaration//GEN-END:variables
}
