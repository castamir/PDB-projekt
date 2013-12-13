
package cz.vutbr.fit.pdb.models;

import cz.vutbr.fit.pdb.models.BaseModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import java.util.Map;        
import oracle.jdbc.pool.OracleDataSource;
        
        
/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class ZakaznikModel extends BaseModel {
    
    public Map<String,Object> get(int id) throws SQLException, Exception {
        
        Map<String,Object> row = new HashMap<>();
        
        OracleDataSource ods = serviceLocator.getConnection();
        try (Connection conn = ods.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM zakaznik WHERE id = ?");
             )
        {
            stmt.setInt(1,id);

            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next()) {
                    row.put("id",id);
                    row.put("jmeno",rs.getString("jmeno"));
                }
                else {
                    return null;
                }
            }
        }
        
        return row; 
    
    }
    
    public Map<Integer, String> getList() throws SQLException {
        
        Map<Integer,String> listOfCustomers = new HashMap<>();
        
        OracleDataSource ods = serviceLocator.getConnection();
        try (Connection conn = ods.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM zakaznik ORDER BY jmeno");
             )
        {

            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next()) {
                    listOfCustomers.put(rs.getInt("id"), rs.getString("jmeno"));
                }
            }
        }
        
        return listOfCustomers;
    }
}
