
package cz.vutbr.fit.pdb.models;

import cz.vutbr.fit.pdb.application.ServiceLocator;
import cz.vutbr.fit.pdb.models.BaseModel;
import java.awt.Polygon;


import java.util.Map;
import java.util.HashMap;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
    
    /**
     *
     * @param name
     * @param shape
     * @throws SQLException
     * @throws Exception
     */
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
    
    /**
     *
     * @return
     * @throws SQLException
     * @throws Exception
     */
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
        
        int[] elemInfo = jGeometry.getElemInfo();
        
        boolean isLine = false;
        
        if (elemInfo.length >= 3) {
            if (elemInfo[1] == 2) { // if etype = 2
                isLine = true;
                System.out.println("mela by byt line");
            }
        }
        
        switch (jGeometry.getType()) {
            case JGeometry.GTYPE_POLYGON:
                shape = jGeometry.createShape();
                break;
            case JGeometry.GTYPE_CURVE:
            case JGeometry.GTYPE_MULTICURVE:
                shape = new Path2D.Float(jGeometry.createShape());
                break;
            default:
                System.out.println("TYPE:"+jGeometry.getType());
                return null; // TODO: throw exception
        }
        
        if (isLine) {
           // shape = new Path2D.Float(shape);
        }
        
        return shape;
    }
    
    private JGeometry shape2JGeometry(Shape shape) {
       JGeometry jgeom;
       
        int polygon = 1;
        int circle = 2;
        int line = 3;
        
        int etype = 1003;
        int gtype = 2003;
        int shapeType = 0;
        int shapeTypeToDB = 0;

        if (shape instanceof Ellipse2D) {
            System.out.println("elipsa");
            shapeType = circle;
            shapeTypeToDB = 4;
        }
        else if (shape instanceof Polygon) {
            System.out.println("polygon");
            shapeType = polygon;
            shapeTypeToDB = 1;
        }
        else if (shape instanceof Path2D) {
            System.out.println("line");
            shapeType = line;
            shapeTypeToDB = 1;
            etype = 2;
            gtype = 2002; // line, multiline je 2006 ??
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

            if (shapeType == polygon || shapeType == line) {
                
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
        
        jgeom = new JGeometry(gtype, 0, new int[]{1, etype, shapeTypeToDB}, ordinates);
        
        return jgeom;
    }
    
    
    /**
     *
     * @param x
     * @param y
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public String getBuildingAtPoint(int x, int y) throws SQLException, Exception {
    
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); Statement stmt = conn.createStatement(); ResultSet resultSet = stmt.executeQuery("select nazev from areal where SDO_RELATE(geometrie, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE(" + x + "," + y + ",NULL), NULL, NULL), 'mask=contains') = 'TRUE'")) {
            while (resultSet.next()) {
                return resultSet.getString("nazev");
            }
        }
        
        return null;
    }
    
    /**
     *
     * @param name
     * @throws SQLException
     */
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
    
    
    /**
     *
     * @param name
     * @return
     * @throws SQLException
     */
    public double getAreaOfBuilding(String name) throws SQLException {
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("select SDO_GEOM.SDO_AREA(geometrie, 0.005) as area from areal where nazev = ?")) 
        {
            stmt.setString(1, name);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getFloat("area");
                }
                else 
                    return 0;
            }
        }
    }
    
    /**
     *
     * @param name
     * @return
     * @throws SQLException
     */
    public double getLengthOfBuilding(String name) throws SQLException {
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("select SDO_GEOM.SDO_LENGTH(geometrie, 0.005) as length from areal where nazev = ?")) 
        {
            stmt.setString(1, name);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getFloat("length");
                }
                else 
                    return 0;
            }
        }
    }
    
    /**
     *
     * @param name
     * @return
     * @throws SQLException
     */
    public Map<String, Float> getDistancesFromBuilding(String name) throws SQLException {
        
        Map<String, Float> result = new LinkedHashMap<>();
        
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("select a1.nazev as n1, a2.nazev as n2, SDO_GEOM.SDO_DISTANCE(a1.geometrie, a2.geometrie, 0.005) as distance from areal a1, areal a2 where a1.nazev = ? AND a1.nazev <> a2.nazev ORDER BY distance")) 
        {
            stmt.setString(1, name);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getString("n2"), rs.getFloat("distance"));
                }
            }
        }
        
        return result;
    }
    
    public Map<String, Float> getNNearestNeighboursFromBuilding(String name, int n) throws SQLException {
        
        Map<String, Float> result = new LinkedHashMap<>();
        
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("select nazev, SDO_NN_DISTANCE(1) as distance from areal where SDO_NN(geometrie, (SELECT x.geometrie FROM areal x WHERE x.nazev='"+name+"'), 'sdo_batch_size=1', 1) = 'TRUE' AND ROWNUM <= ? AND nazev <> ? ORDER BY distance")) 
        {
            stmt.setString(2, name);
            stmt.setInt(1, n);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getString("nazev"), (float)rs.getInt("distance"));
                }
            }
        }
        
        return result;
    }
    
    
    public void doUnionOperation() throws SQLException {
    
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("select distinct a1.nazev n1, a2.nazev n2 from areal a1, areal a2 WHERE a1.nazev<>a2.nazev AND SDO_RELATE(a1.geometrie, a2.geometrie,'MASK=OVERLAPBDYINTERSECT+CONTAINS+INSIDE') = 'TRUE' ORDER BY a1.nazev")) 
        {
            //stmt.setString(1, name);
            //stmt.setInt(2, n);
            
            boolean shouldEnd = false;
            
            while (!shouldEnd) {
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String n1 = rs.getString("n1");
                        String n2 = rs.getString("n2");

                        String newName = n1+"+"+n2;

                        PreparedStatement insertStmt = conn.prepareStatement("insert into areal (nazev, geometrie) VALUES (?, SDO_GEOM.SDO_UNION((SELECT geometrie FROM areal WHERE nazev=?),(SELECT geometrie FROM areal WHERE nazev=?),0.005))");
                        PreparedStatement deleteStmt = conn.prepareStatement("delete from areal where nazev IN (?,?)");
                        try {
                            insertStmt.setString(1, newName);
                            insertStmt.setString(2, n1);
                            insertStmt.setString(3, n2);

                            deleteStmt.setString(1, n1);
                            deleteStmt.setString(2, n2);

                            insertStmt.execute();
                            deleteStmt.execute();
                        }
                        finally {
                            insertStmt.close();
                            deleteStmt.close();
                        }
                    }
                    else {
                        shouldEnd = true;
                    }
                }
            }
        }
    }
}
