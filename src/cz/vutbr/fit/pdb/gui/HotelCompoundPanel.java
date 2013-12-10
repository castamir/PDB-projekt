/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.vutbr.fit.pdb.gui;

import cz.vutbr.fit.pdb.Loader;
import cz.vutbr.fit.pdb.ServiceLocator;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;

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
import java.awt.geom.AffineTransform;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class HotelCompoundPanel extends JPanel implements MouseListener
{
    private static final long serialVersionUID = 1L;
    private double zoom = 1.0;

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

    public void loadShapesFromDb(List<Shape> shapes) throws SQLException, Exception {
        if (shapes == null) {
            shapes = new ArrayList<>();
        }
        
        Loader loader = new Loader();
        ServiceLocator serviceLocator = new ServiceLocator(loader.getProperties());
        
        OracleDataSource ods = serviceLocator.getConnection();
        try (Connection conn = ods.getConnection(); Statement stmt = conn.createStatement(); ResultSet resultSet = stmt.executeQuery("select nazev, geometrie from areal")) {
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
        //g.clearRect(0, 0, getWidth(), getHeight());
        super.paint(g);
        AffineTransform a = new AffineTransform();
        AffineTransform a2 = new AffineTransform();
        a2.setToIdentity();
        a2.translate(0, 50);
        a.concatenate(a2);
        a.scale(zoom, zoom);
        List<Shape> shapes = new ArrayList<>();
        Graphics2D g2D = (Graphics2D) g;
        //super.paint(g2D);
        try {
            loadShapesFromDb(shapes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Iterator<Shape> iterator = shapes.iterator(); iterator.hasNext();) {
            Shape shape = iterator.next();
            g2D.setTransform(a);
            g2D.setPaint(Color.GRAY);
            g2D.fill(shape);
            g2D.setPaint(Color.BLACK);
            g2D.draw(shape);
        }
    }
    
    
    private void objectAtPoint(int x, int y) throws SQLException, Exception
    {
        Loader loader = new Loader();
        ServiceLocator serviceLocator = new ServiceLocator(loader.getProperties());
        
        OracleDataSource ods = serviceLocator.getConnection();
        try (Connection conn = ods.getConnection(); Statement stmt = conn.createStatement(); ResultSet resultSet = stmt.executeQuery("select nazev from areal where SDO_RELATE(geometrie, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE("+x+","+y+",NULL), NULL, NULL), 'mask=contains') = 'TRUE'")) {
            while (resultSet.next()) {                
                System.out.println(resultSet.getString("nazev"));
            }
        }
        
    }
    

    /* MouseListener methods */
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse clicked at ("+e.getX()+","+e.getY()+")");
        
        try {
            this.objectAtPoint(e.getX(), e.getY());
        }
        catch (Exception exc) {
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
    
    public void setZoom(double zoomFactor) {
        zoom = zoomFactor;
        repaint();
    }
}
