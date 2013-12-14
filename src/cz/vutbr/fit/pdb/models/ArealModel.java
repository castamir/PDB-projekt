
package cz.vutbr.fit.pdb.models;

import cz.vutbr.fit.pdb.application.ServiceLocator;
import cz.vutbr.fit.pdb.models.BaseModel;


import java.util.Map;
import java.util.HashMap;
import java.awt.Shape;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import oracle.jdbc.pool.OracleDataSource;
import oracle.spatial.geometry.JGeometry;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class ArealModel extends BaseModel {
    
    
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
    
    
    public String getBuildingAtPoint(int x, int y) throws SQLException, Exception {
    
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); Statement stmt = conn.createStatement(); ResultSet resultSet = stmt.executeQuery("select nazev from areal where SDO_RELATE(geometrie, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE(" + x + "," + y + ",NULL), NULL, NULL), 'mask=contains') = 'TRUE'")) {
            while (resultSet.next()) {
                return resultSet.getString("nazev");
            }
        }
        
        return null;
    }
}
