
package cz.vutbr.fit.pdb.models;

import cz.vutbr.fit.pdb.application.ServiceLocator;
import cz.vutbr.fit.pdb.models.BaseModel;
import java.awt.Polygon;


import java.util.Map;
import java.util.HashMap;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import oracle.sql.STRUCT;
import oracle.jdbc.pool.OracleDataSource;
import oracle.spatial.geometry.JGeometry;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class ArealModel extends BaseModel {
    
    public void saveShape(String name, Shape shape) throws SQLException, Exception {
                
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO areal (nazev, geometrie) VALUES (?, ?)");
             )
        {
            stmt.setString(1, name);
            
            STRUCT obj = JGeometry.store(conn, shape2JGeometry(shape));
            stmt.setObject(2, obj);
            
            stmt.execute();
        }        
    }
    
    public Map<String, Shape> loadShapes() throws SQLException, Exception {
    
        Map<String, Shape> shapes = new HashMap<String, Shape>();
        
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); Statement stmt = conn.createStatement(); ResultSet resultSet = stmt.executeQuery("select nazev, geometrie from areal")) {
            while (resultSet.next()) {
                byte[] image = resultSet.getBytes("geometrie");
                JGeometry jGeometry = JGeometry.load(image);
                
                Shape shape = jGeometry2Shape(jGeometry);
                if (shape != null) {
                    shapes.put(resultSet.getString("nazev"), shape);
                }
            }
        }
        
        return shapes;
    }
    
    private Shape jGeometry2Shape(JGeometry jGeometry) {
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
    
    private JGeometry shape2JGeometry(Shape shape) {
       JGeometry jgeom;
       
       int polygon = 1;
        int circle = 4;

        int shapeType = 0;

        if (shape instanceof Ellipse2D) {
            System.out.println("elipsa");
            shapeType = circle;
        }
        else if (shape instanceof Polygon) {
            System.out.println("polygon");
            shapeType = polygon;
        }
        else {
            System.out.println("other");
        }

        if (shapeType == 0) 
            return null;

        
        List<Double> points = new ArrayList<>();
        double[] coords = new double[6];

        int ellipse_SEG_CUBICTO_Counter = 0;

        for (PathIterator pi = shape.getPathIterator(null); !pi.isDone(); pi.next()) {

            for (int j=0;j<6;j++) {
                coords[j] = 0.0;
            }

            int type = pi.currentSegment(coords);
            System.out.println(type+":"+coords[0]+","+coords[1]+","+coords[2]+","+coords[3]+","+coords[4]+","+coords[5]);

            if (shapeType == polygon) {
                
                if (type != PathIterator.SEG_LINETO && type != PathIterator.SEG_MOVETO) {
                    continue;
                }
                
                points.add(coords[0]);
                points.add(coords[1]);
            }
            else if (shapeType == circle) {
                if (type != PathIterator.SEG_CUBICTO) {
                    continue;
                }

                if (ellipse_SEG_CUBICTO_Counter < 3) {

                    points.add(coords[4]);
                    points.add(coords[5]);

                    ellipse_SEG_CUBICTO_Counter++;
                }
            }
        }
        
        if (shapeType == polygon) {
            double x,y;
            x = points.get(0);
            y = points.get(1);
            
            points.add(x);
            points.add(y);
        }
            
        double[] ordinates = new double[points.size()];
        int k = 0;
        for (Double d : points) {
            ordinates[k++] = d;
            //System.out.println(d);
        }
        
        jgeom = new JGeometry(2003, 0, new int[]{1, 1003, shapeType}, ordinates);
        
        return jgeom;
    }
    
    
    public String getBuildingAtPoint(int x, int y) throws SQLException, Exception {
    
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); Statement stmt = conn.createStatement(); ResultSet resultSet = stmt.executeQuery("select nazev from areal where SDO_RELATE(geometrie, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE(" + x + "," + y + ",NULL), NULL, NULL), 'mask=contains') = 'TRUE'")) {
            while (resultSet.next()) {
                return resultSet.getString("nazev");
            }
        }
        
        return null;
    }
    
    public void deleteBuildingWithName(String name) throws SQLException {
        
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM areal WHERE nazev = ?");
             )
        {
            stmt.setString(1, name);
            stmt.execute();
        }
    
    }
}
