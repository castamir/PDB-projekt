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
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Třída, která umožňuje editaci areálu interaktivní formou.
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class HotelCompoundEditablePanel extends javax.swing.JPanel implements MouseListener, MouseMotionListener, KeyListener {

    private Map<String, Object> shapes;
    private Map<String, Object> newShapes;
    
    private List<String> buildingsToDelete;
    private String selectedBuilding;
    private boolean drawing = false;
    private ArealModel arealModel;
    /* aktualne vytvareny tvar */
    private List<Point2D> points;
    private Line2D.Float currentLine;
    private String currentObjectName;
    private Polygon newPolygon;
    private Path2D newPath;
    private Point2D newPoint;
    private Ellipse2D newCircle;
    private Rectangle2D newRectangle;
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

    private boolean areChangesSaved() {
        return (newShapes == null || newShapes.isEmpty());
    }
    
    private void saveChanges() {

        try {
            for (String name : buildingsToDelete) {
                arealModel.deleteBuildingWithName(name);
            }
        } catch (SQLException ex) {
            Logger.getLogger(HotelCompoundEditablePanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        // ulozeni novych

        for (Map.Entry<String, Object> entry : newShapes.entrySet()) {
            try {
                if (entry.getValue() instanceof Shape) {
                    arealModel.saveShape(entry.getKey(), (Shape)entry.getValue());
                }
                else { // point
                    arealModel.savePoint(entry.getKey(), (Point2D)entry.getValue());
                }
            } catch (SQLException ex) {
                Logger.getLogger(HotelCompoundEditablePanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(HotelCompoundEditablePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        reloadShapes();
        repaint();
    }

    /**
     * Vykreslí data
     * @param g
     */
    @Override
    public void paint(Graphics g) {

        super.paint(g);

        Graphics2D g2D = (Graphics2D) g;

        g2D.translate(horizontalOffset, verticalOffset);

        loadShapes();

        for (Map.Entry<String, Object> entry : shapes.entrySet()) {
            if (entry.getValue() instanceof Shape) {
                drawShape(g2D, entry.getKey(), (Shape)entry.getValue(), false);
            }
            else {
                drawPoint(g2D, entry.getKey(), (Point2D)entry.getValue(), false);
            }
        }

        for (Map.Entry<String, Object> entry : newShapes.entrySet()) {
            if (entry.getValue() instanceof Shape) {
                drawShape(g2D, entry.getKey(), (Shape)entry.getValue(), true);
            }
            else {
                drawPoint(g2D, entry.getKey(), (Point2D)entry.getValue(), true);
            }
        }
    }
    
    private void drawPoint(Graphics2D g2D, String name, Point2D point, boolean newShape) {
    
        float r = (float)2;
        
        Ellipse2D tmp = new Ellipse2D.Float((float)point.getX()-r, (float)point.getY()-r, 2*r, 2*r);
        
        drawShape(g2D, name, tmp, newShape);
    }

    private void drawShape(Graphics2D g2D, String name, Shape shape, boolean newShape) {
        Color background = Color.darkGray;
        Color fontColor = Color.lightGray;
        Color borderColor = Color.black;
        
        if (selectedBuilding != null && selectedBuilding.equals(name)) {
            background = Color.green;
            fontColor = Color.darkGray;
        } else if (newShape) {
            background = Color.yellow;
        }

        g2D.setPaint(background);
        
        if ((shape instanceof Rectangle2D) || (shape instanceof GeneralPath) || (shape instanceof Ellipse2D) || (shape instanceof Polygon)) {
            g2D.fill(shape);
        }
        
        //System.out.println(shape+" je trida: "+ shape.getClass().toString());
        
        g2D.setPaint(borderColor);
        g2D.draw(shape);
        g2D.setColor(fontColor);
        g2D.setFont(new Font("Verdana", Font.BOLD, 11));
        
        

        if (!name.equals(tmpLineKey)) {
            g2D.drawString(name, (int) shape.getBounds2D().getMinX() + 5, (int) shape.getBounds2D().getMaxY() - 5);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Mouse listener methods">   
    /**
     * Vybírání objektů v mapě.
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {

        if (drawing) {
            return;
        }

        String buildingName = null;

        selectedBuilding = null;

        for (Map.Entry<String, Object> entry : newShapes.entrySet()) {

            if (entry.getValue() instanceof Shape) {
                Shape obj = (Shape)entry.getValue();

                if (obj.contains(e.getX() - horizontalOffset, e.getY() - verticalOffset)) {
                    selectedBuilding = entry.getKey();
                    break;
                }
            }
            else {
                
                Point2D point = (Point2D)entry.getValue();
                
                if (point.distance(e.getX() - horizontalOffset, e.getY() - verticalOffset) <= 4) {
                    selectedBuilding = entry.getKey();
                    break;
                }
            }
        }

        if (selectedBuilding == null) {
            try {
                buildingName = arealModel.getBuildingAtPoint(e.getX() - horizontalOffset, e.getY() - verticalOffset);
            } catch (Exception exc) {
                exc.printStackTrace();
            }

            if (buildingName != null) {
                selectedBuilding = buildingName;
            }
        }

        repaint();
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        requestFocus();
    }

    /**
     * Počátek kreslení objektů
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {

        if (drawing == false) {
            return;
        }

        System.out.println("pressed");

        if (objectTypeCircle.isSelected()) {

            newCircle = new Ellipse2D.Float(e.getX() - horizontalOffset, e.getY() - verticalOffset, 0, 0);

            newShapes.put(currentObjectName, newCircle);
        } else if (objectTypeRectangle.isSelected()) {

            newRectangle = new Rectangle2D.Float(e.getX() - horizontalOffset, e.getY() - verticalOffset, 0, 0);

            newShapes.put(currentObjectName, newRectangle);
        } else if (objectTypePolygon.isSelected()) {
            
            newShapes.remove(tmpLineKey);
            newShapes.remove(currentObjectName);
              
            if (points == null) {
                points = new ArrayList<>();
            }
            
            points.add(new Point2D.Float(e.getX() - horizontalOffset, e.getY() - verticalOffset));
            // polygon

            int i = 0;
            int npoints = points.size();
            int[] xpoints = new int[npoints];
            int[] ypoints = new int[npoints];
            for (Point2D point : points) {
                xpoints[i] = (int) point.getX();
                ypoints[i] = (int) point.getY();
                i++;
            }

            newPolygon = new Polygon(xpoints, ypoints, npoints);
            
            newShapes.put(currentObjectName, newPolygon);
        }
        else if (objectTypeLine.isSelected()) {
            newShapes.remove(tmpLineKey);
            
            if (newPath == null) {
                newPath = new Path2D.Float();
                newPath.moveTo(e.getX() - horizontalOffset, e.getY() - verticalOffset);
                
                newShapes.put(currentObjectName, newPath);
            }
            else {
                newPath.lineTo(e.getX() - horizontalOffset, e.getY() - verticalOffset);
            }
        }
        else if (objectTypePoint.isSelected()) {
            
            newPoint = new Point2D.Float(e.getX() - horizontalOffset, e.getY() - verticalOffset);
            
            newShapes.put(currentObjectName, newPoint);
            
            newPoint = null;
            drawing = false;
        }

        repaint();
    }

    /**
     * Ukončení kreslení některých objektů.
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {

        if (objectTypeCircle.isSelected() && newCircle != null) {

            if (newCircle.getWidth() == 0 || newCircle.getHeight() == 0) {
                newShapes.remove(currentObjectName);
                return;
            }

            drawing = false;
            newCircle = null;
            selectedBuilding = currentObjectName;

            repaint();
        }
        else if (objectTypeRectangle.isSelected() && newRectangle != null) {
            if (newRectangle.getWidth() == 0 || newRectangle.getHeight() == 0) {
                newShapes.remove(currentObjectName);
                return;
            }

            drawing = false;
            newRectangle = null;
            selectedBuilding = currentObjectName;

            repaint();
        }
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="Mouse Motion listener methods">   
    /**
     *
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {

        if (drawing == false) {
            return;
        }

        if (objectTypePolygon.isSelected()) {
            if (points == null || points.isEmpty()) {
                return;
            }

            Point2D point = points.get(points.size() - 1);
            Point2D point2 = new Point2D.Float((float) e.getX() - horizontalOffset, (float) e.getY() - verticalOffset);

            currentLine = new Line2D.Float(point, point2);

            newShapes.remove(tmpLineKey);
            newShapes.put(tmpLineKey, currentLine);
        }
        else if (objectTypeLine.isSelected()) {
            
            if (newPath == null) {
                return;
            }
            
            Point2D point = newPath.getCurrentPoint();
            Point2D point2 = new Point2D.Float((float) e.getX() - horizontalOffset, (float) e.getY() - verticalOffset);

            currentLine = new Line2D.Float(point, point2);

            newShapes.remove(tmpLineKey);
            newShapes.put(tmpLineKey, currentLine); 
        }

        repaint();
    }

    /**
     * Tažením myši lze vytvářet kruh nebo čtverec/obdélník.
     * @param e
     */
    @Override
    public void mouseDragged(MouseEvent e) {

        if (objectTypeCircle.isSelected() && newCircle != null) {
            int w, h, r;
            int x, y, x1, x2, y1, y2;

            x1 = (int) newCircle.getX();
            y1 = (int) newCircle.getY();
            x2 = e.getX() - horizontalOffset;
            y2 = e.getY() - verticalOffset;

            w = x2 - x1;
            h = y2 - y1;

            r = (w > h) ? w : h;

            if (r < 0) {
                return;
            }

            newCircle.setFrame(x1, y1, r, r);

            repaint();
        }
        else if (objectTypeRectangle.isSelected() && newRectangle != null) {
            int w, h;
            int x, y, x1, x2, y1, y2;

            x1 = (int) newRectangle.getX();
            y1 = (int) newRectangle.getY();
            x2 = e.getX() - horizontalOffset;
            y2 = e.getY() - verticalOffset;

            w = x2 - x1;
            h = y2 - y1;
            
            if (w == 0 || h == 0) {
                return;
            }

            newRectangle.setFrame(x1, y1, w, h);

            repaint();
        }
    }

    // </editor-fold> 
    
    // <editor-fold defaultstate="collapsed" desc="Key listener methods">   
    /**
     * Zmáčknutím příslušných tlačítek dojde ke zrušení vytváření nebo k potvrzení nového objektu.
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("pressed");
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.out.println("esc");
            
            if (!points.isEmpty()) {
                points.removeAll(points);
            }
            
            newShapes.remove(currentObjectName);
            newShapes.remove(tmpLineKey);
            
            newPath = null;
            newPolygon = null;
            newCircle = null;
            newRectangle = null;
            newPoint = null;

            drawing = false;
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {

            if (points != null) {
                points.removeAll(points);
            }
            
            newShapes.remove(tmpLineKey);
            newPath = null;
            newPolygon = null;
            newRectangle = null;
            newPoint = null;
            
            selectedBuilding = currentObjectName;
            
            drawing = false;
        }

        repaint();
    }

    /**
     *
     * @param e
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     *
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {
    }

    // </editor-fold>
    
    /* dialog pro ziskani nazvu kresleneho objektu */
    /**
     * Vytvoří nové okno, kde lze zadat název nového objektu.
     */
    public void newObjectNameDialog() {

        String s = (String) JOptionPane.showInputDialog(
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
            } else {
                currentObjectName = s;
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
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        objectTypeButtonGroup = new javax.swing.ButtonGroup();
        jToolBar1 = new javax.swing.JToolBar();
        addBtn = new javax.swing.JButton();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        removeBtn = new javax.swing.JButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(30, 0), new java.awt.Dimension(30, 0), new java.awt.Dimension(30, 32767));
        objectTypePoint = new javax.swing.JRadioButton();
        objectTypeLine = new javax.swing.JRadioButton();
        objectTypeRectangle = new javax.swing.JRadioButton();
        objectTypePolygon = new javax.swing.JRadioButton();
        objectTypeCircle = new javax.swing.JRadioButton();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(50, 0), new java.awt.Dimension(50, 0), new java.awt.Dimension(50, 32767));
        saveBtn = new javax.swing.JButton();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        cancelChangesBtn = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(50, 0), new java.awt.Dimension(50, 0), new java.awt.Dimension(50, 32767));
        areaBtn = new javax.swing.JButton();
        filler8 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        lengthBtn = new javax.swing.JButton();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        distancesBtn = new javax.swing.JButton();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        nearestBtn = new javax.swing.JButton();
        filler9 = new javax.swing.Box.Filler(new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 32767));
        unionBtn = new javax.swing.JButton();

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
        jToolBar1.add(filler6);

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

        objectTypeButtonGroup.add(objectTypePoint);
        objectTypePoint.setText("Point");
        objectTypePoint.setFocusable(false);
        objectTypePoint.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        objectTypePoint.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        objectTypePoint.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(objectTypePoint);

        objectTypeButtonGroup.add(objectTypeLine);
        objectTypeLine.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        objectTypeLine.setText("Line");
        objectTypeLine.setFocusable(false);
        objectTypeLine.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        objectTypeLine.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        objectTypeLine.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(objectTypeLine);

        objectTypeButtonGroup.add(objectTypeRectangle);
        objectTypeRectangle.setSelected(true);
        objectTypeRectangle.setText("Rectangle");
        objectTypeRectangle.setFocusable(false);
        objectTypeRectangle.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        objectTypeRectangle.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(objectTypeRectangle);

        objectTypeButtonGroup.add(objectTypePolygon);
        objectTypePolygon.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        objectTypePolygon.setText("Polygon");
        objectTypePolygon.setFocusable(false);
        objectTypePolygon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        objectTypePolygon.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        objectTypePolygon.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(objectTypePolygon);

        objectTypeButtonGroup.add(objectTypeCircle);
        objectTypeCircle.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        objectTypeCircle.setText("Circle");
        objectTypeCircle.setFocusable(false);
        objectTypeCircle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        objectTypeCircle.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        objectTypeCircle.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(objectTypeCircle);
        jToolBar1.add(filler3);

        saveBtn.setText("Uložit");
        saveBtn.setFocusable(false);
        saveBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(saveBtn);
        jToolBar1.add(filler5);

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
        jToolBar1.add(filler1);

        areaBtn.setText("PLOCHA");
        areaBtn.setFocusable(false);
        areaBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        areaBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        areaBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                areaBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(areaBtn);
        jToolBar1.add(filler8);

        lengthBtn.setText("OBVOD");
        lengthBtn.setFocusable(false);
        lengthBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lengthBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        lengthBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lengthBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(lengthBtn);
        jToolBar1.add(filler7);

        distancesBtn.setText("VZDÁLENOSTI");
        distancesBtn.setFocusable(false);
        distancesBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        distancesBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        distancesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                distancesBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(distancesBtn);
        jToolBar1.add(filler4);

        nearestBtn.setText("NEJBLIŽŠÍ");
        nearestBtn.setFocusable(false);
        nearestBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        nearestBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        nearestBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nearestBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(nearestBtn);
        jToolBar1.add(filler9);

        unionBtn.setText("SPOJIT");
        unionBtn.setFocusable(false);
        unionBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        unionBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        unionBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unionBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(unionBtn);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(534, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        newObjectNameDialog();
    }//GEN-LAST:event_addBtnActionPerformed

    private void removeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBtnActionPerformed
        if (selectedBuilding == null) {
            JOptionPane.showMessageDialog(getParent(), "Vyberte nejprve objekt ke smazání.");
        } else {

            if (shapes.get(selectedBuilding) != null) {
                shapes.remove(selectedBuilding);
                buildingsToDelete.add(selectedBuilding);
            } else if (newShapes.get(selectedBuilding) != null) {
                newShapes.remove(selectedBuilding);
            }

            selectedBuilding = null;

            repaint();
        }
    }//GEN-LAST:event_removeBtnActionPerformed

    private void cancelChangesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelChangesBtnActionPerformed
        reloadShapes();
        repaint();
    }//GEN-LAST:event_cancelChangesBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        saveChanges();
        //repaint();
    }//GEN-LAST:event_saveBtnActionPerformed

    private void areaBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_areaBtnActionPerformed
        if (selectedBuilding == null) {
            JOptionPane.showMessageDialog(getParent(), "Vyberte nejprve objekt.");
        } else {

            try {
                double area = arealModel.getAreaOfBuilding(selectedBuilding);

                JOptionPane.showMessageDialog(getParent(), "Plocha objektu '" + selectedBuilding + "' je " + area);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(getParent(), "Při výpočtu došlo k chybě.");
            }
        }
    }//GEN-LAST:event_areaBtnActionPerformed

    private void unionBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unionBtnActionPerformed
        
        if (!areChangesSaved()) {
        
            JOptionPane.showMessageDialog(getParent(), "Uložte prosím změny a pak operaci opakujte.");
            
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(getParent(), "Tato operace vyhledá překrývající se objekty a spojí je do jednoho. Opravdu chcete operaci provést?");
        
        if (confirm != JOptionPane.YES_OPTION) {
            System.out.println("konec");
            return;
        }
        
        try {
            arealModel.doUnionOperation();
            
            reloadShapes();
            repaint();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(getParent(), "Při operaci došlo k chybě.");
        }
    }//GEN-LAST:event_unionBtnActionPerformed

    private void nearestBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nearestBtnActionPerformed
        if (selectedBuilding == null) {
            JOptionPane.showMessageDialog(getParent(), "Vyberte nejprve objekt.");
        } else {

            try {
                
                String n = JOptionPane.showInputDialog(getParent(), "Vyhledat n nejbližších sousedů:");
                
                if (n == null)
                    return;
                
                Map<String, Float> distances = arealModel.getNNearestNeighboursFromBuilding(selectedBuilding, Integer.parseInt(n));
                
                String msg = "Nejbližší sousedé ("+n+") objektu '" + selectedBuilding + "' jsou:\n";

                for (Map.Entry<String, Float> entry : distances.entrySet()) {

                    msg += "'" + entry.getKey() + "'" + " ve vzdálenosti " + entry.getValue().toString() + "\n";
                }

                JOptionPane.showMessageDialog(getParent(), msg);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(getParent(), "Při výpočtu došlo k chybě.");
            }
        }
    }//GEN-LAST:event_nearestBtnActionPerformed

    private void distancesBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if (selectedBuilding == null) {
            JOptionPane.showMessageDialog(getParent(), "Vyberte nejprve objekt.");
        } else {

            try {
                Map<String, Float> distances = arealModel.getDistancesFromBuilding(selectedBuilding);

                String msg = "Vzdálenost objektu '" + selectedBuilding + "' od objektu:\n";

                for (Map.Entry<String, Float> entry : distances.entrySet()) {

                    msg += "'" + entry.getKey() + "'" + " je " + entry.getValue().toString() + "\n";
                }

                JOptionPane.showMessageDialog(getParent(), msg);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(getParent(), "Při výpočtu došlo k chybě.");
            }
        }
    }

    private void lengthBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if (selectedBuilding == null) {
            JOptionPane.showMessageDialog(getParent(), "Vyberte nejprve objekt.");
        } else {

            try {
                double length = arealModel.getLengthOfBuilding(selectedBuilding);

                JOptionPane.showMessageDialog(getParent(), "Obvod objektu '" + selectedBuilding + "' je " + length);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(getParent(), "Při výpočtu došlo k chybě.");
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JButton areaBtn;
    private javax.swing.JButton cancelChangesBtn;
    private javax.swing.JButton distancesBtn;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.Box.Filler filler7;
    private javax.swing.Box.Filler filler8;
    private javax.swing.Box.Filler filler9;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton lengthBtn;
    private javax.swing.JButton nearestBtn;
    private javax.swing.ButtonGroup objectTypeButtonGroup;
    private javax.swing.JRadioButton objectTypeCircle;
    private javax.swing.JRadioButton objectTypeLine;
    private javax.swing.JRadioButton objectTypePoint;
    private javax.swing.JRadioButton objectTypePolygon;
    private javax.swing.JRadioButton objectTypeRectangle;
    private javax.swing.JButton removeBtn;
    private javax.swing.JButton saveBtn;
    private javax.swing.JButton unionBtn;
    // End of variables declaration//GEN-END:variables
}
