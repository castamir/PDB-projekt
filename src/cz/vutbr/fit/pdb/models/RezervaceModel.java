package cz.vutbr.fit.pdb.models;

import cz.vutbr.fit.pdb.application.ServiceLocator;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oracle.jdbc.pool.OracleDataSource;

/**
 *
 * @author Pavel
 */
public class RezervaceModel extends BaseModel {
    
    public void vytvoritRezervaci(int zakaznik, List<Integer> pokoje, String datum_od, String datum_do) throws SQLException, ParseException {
        
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO rezervace (zakaznik, pokoj, od, do) VALUES(?,?,?,?)");
             )
        {
            conn.setAutoCommit(false);
            
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            Date d_od = new Date(date.parse(datum_od).getTime());
            Date d_do = new Date(date.parse(datum_do).getTime());
            
            for (Integer i : pokoje) {
                stmt.setInt(1, zakaznik);
                stmt.setInt(2, i);
                stmt.setDate(3,d_od);
                stmt.setDate(4,d_do);
                stmt.addBatch();
            }

            int[] result = stmt.executeBatch();
            
            conn.commit();
        }
    }
    
    public List<Integer> rezervovanePokojeVObdobi(String datum_od, String datum_do) throws SQLException, ParseException {
    
        List<Integer> pokoje = new ArrayList<>();
        
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("SELECT pokoj FROM rezervace WHERE (od BETWEEN ? AND ?) OR (do BETWEEN ? AND ?) OR (? BETWEEN od AND do) OR (? BETWEEN od AND do)");
             )
        {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            Date d_od = new Date(date.parse(datum_od).getTime());
            Date d_do = new Date(date.parse(datum_do).getTime());   
            
            stmt.setDate(1, d_od);
            stmt.setDate(2, d_do);
            stmt.setDate(3, d_od);
            stmt.setDate(4, d_do);
            
            stmt.setDate(5, d_od);
            stmt.setDate(6, d_do);
            
            try (ResultSet rs = stmt.executeQuery()) {
           
                while (rs.next()) {
                    pokoje.add(rs.getInt("pokoj"));
                }
            }
        }
        
        return pokoje;
    }
    
    
    public Map<Integer, String> getPokoje() throws SQLException {
        
        Map<Integer, String> pokoje = new LinkedHashMap<>();
        
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); 
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM pokoje")
             )
        {
            while (rs.next()) {
                pokoje.put(rs.getInt("id"), rs.getString("nazev"));
            }
        }
        
        return pokoje;
    }
}
