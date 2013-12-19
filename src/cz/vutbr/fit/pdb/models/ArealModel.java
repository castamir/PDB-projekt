
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
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
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
 * Model pro práci s tabulkou 'areal'
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class ArealModel extends BaseModel {
    
    /**
     * Ukládá objekt type Shape do databáze.
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
     * Ukládá objekt typu Point2D do databáze.
     * @param name
     * @param point
     * @throws SQLException
     * @throws Exception
     */
    public void savePoint(String name, Point2D point) throws SQLException, Exception {
    
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO areal (nazev, geometrie) VALUES (?, ?)");
             )
        {
            stmt.setString(1, name);
            
            JGeometry jgeomPoint = JGeometry.createPoint(new double[]{point.getX(), point.getY()}, 2, 0);
            
            STRUCT obj = JGeometry.store(conn, jgeomPoint);
            stmt.setObject(2, obj);
            
            stmt.execute();
        }        
    }
    
    /**
     * Načte všechny tvary z databáze.
     * @return Všechny tvary v databázi. Klíč je název objektu, hodnota je objekt typu Shape nebo Pojnt2D.
     * @throws SQLException
     * @throws Exception
     */
    public Map<String, Object> loadShapes() throws SQLException, Exception {
    
        Map<String, Object> shapes = new HashMap<>();
        
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); Statement stmt = conn.createStatement(); ResultSet resultSet = stmt.executeQuery("select nazev, geometrie from areal")) {
            while (resultSet.next()) {
                byte[] image = resultSet.getBytes("geometrie");
                JGeometry jGeometry = JGeometry.load(image);
                
                Object shape = jGeometry2Shape(jGeometry);
                if (shape != null) {
                    shapes.put(resultSet.getString("nazev"), shape);
                }
            }
        }
        
        return shapes;
    }
    
    private Object jGeometry2Shape(JGeometry jGeometry) {
        Object shape;
            
        switch (jGeometry.getType()) {
            case JGeometry.GTYPE_POLYGON:
                shape = jGeometry.createShape();
                break;
            case JGeometry.GTYPE_CURVE:
            case JGeometry.GTYPE_MULTICURVE:
                shape = new Path2D.Float(jGeometry.createShape());
                break;  
            case JGeometry.GTYPE_POINT:
                shape = jGeometry.getJavaPoint();
                break;
            default:
                System.out.println("TYPE:"+jGeometry.getType());
                return null; // TODO: throw exception
        }
        
        return shape;
    }
    
    private JGeometry shape2JGeometry(Shape shape) {
       JGeometry jgeom;
       
        int polygon = 1;
        int circle = 2;
        int line = 3;
        int rectangle = 4;
        
        int etype = 1003;
        int gtype = 2003;
        int shapeType = 0;
        int shapeTypeToDB = 0;

        if (shape instanceof Ellipse2D) {
            System.out.println("elipsa");
            shapeType = circle;
            shapeTypeToDB = 4;
        }
        else if (shape instanceof Rectangle2D) {
            System.out.println("rectangle");
            shapeType = rectangle;
            shapeTypeToDB = 3;
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
        
        if (shapeType != rectangle) {
            
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
        }
        else {
        
            Rectangle2D rec = shape.getBounds2D();
            points.add(rec.getMinX());
            points.add(rec.getMinY());
            points.add(rec.getMaxX());
            points.add(rec.getMaxY());
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
     * Vyhledá objekt/budovu na daných souřadnicích v databázi.
     * @param x
     * @param y
     * @return Název objektu/budovy.
     * @throws SQLException
     * @throws Exception
     */
    public String getBuildingAtPoint(int x, int y) throws SQLException, Exception {
    
        int x1,x2,x3,y1,y2,y3;
        int r = 4;
        
        x1 = x3 = x;
        y1 = y - r;
        y3 = y + r;
        x2 = x + r;
        y2 = y;
        
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); Statement stmt = conn.createStatement(); 
             ResultSet resultSet = stmt.executeQuery("select nazev from areal where SDO_RELATE(geometrie, SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1,1003,4), SDO_ORDINATE_ARRAY("+x1+","+y1+", "+x2+","+y2+", "+x3+","+y3+")), 'mask=ANYINTERACT') = 'TRUE'"))
        {
            while (resultSet.next()) {
                return resultSet.getString("nazev");
            }
        }
        
        return null;
    }
    
    /**
     * Smaže objekt/budovu se zadaným názvem.
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
     * Vypočítá plochu objektu/budovy.
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
     * Vypočítá obvod/délku budovy.
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
     * Vypočítá vzdálenosti od budovy/objektu k ostatním budovám/objektům.
     * @param name
     * @return Vzdálenosti. Klíč je název budovy a hodnota je vzdálenost k dané budově od zkoumané budovy.
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
    
    /**
     * Vyhledá n nejbližších sousedů budovy.
     * @param name
     * @param n neighbours to be returned
     * @return Klíč je název budovy a hodnota vzdálenost k tét budově od zkoumané budovy.
     * @throws SQLException
     */
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
    
    /**
     * Spojí překrývající se budovy/objekty pomocí operace SDO_GEOM_SDO_UNION.
     * @return
     * @throws SQLException
     */
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
