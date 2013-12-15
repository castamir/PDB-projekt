
package cz.vutbr.fit.pdb.models;

import cz.vutbr.fit.pdb.application.ServiceLocator;
import cz.vutbr.fit.pdb.models.BaseModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;

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
        
        OracleDataSource ods = ServiceLocator.getConnection();
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
        
        Map<Integer,String> listOfCustomers = new LinkedHashMap<>();
        
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM zakaznik ORDER BY jmeno");
             )
        {

            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next()) {
                    listOfCustomers.put(rs.getInt("id"), rs.getString("id")+" "+rs.getString("jmeno"));
                }
            }
        }
        
        return listOfCustomers;
    }
    
    public int insert(String name, String surname, String address, String city, String postalCode, String region, String phone, String email) throws SQLException {
    
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO zakaznik (jmeno, prijmeni, adresa, mesto, psc, kraj, telefon, email) VALUES(?,?,?,?,?,?,?,?)");
             )
        {
            stmt.setString(1,name);   
            stmt.setString(2,surname);
            stmt.setString(3,address);
            stmt.setString(4,city);
            stmt.setString(5,postalCode);
            stmt.setString(6,region);
            stmt.setString(7,phone);
            stmt.setString(8,email);

            stmt.execute();
            
            try (Statement stmt2 = conn.createStatement();
                 ResultSet rs = stmt2.executeQuery("SELECT id FROM zakaznik ORDER BY id"))
            {
                rs.next();
                
                return rs.getInt("id");
            }
        }
    }
}
