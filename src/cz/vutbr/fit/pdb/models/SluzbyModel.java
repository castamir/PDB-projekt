
package cz.vutbr.fit.pdb.models;

import cz.vutbr.fit.pdb.models.BaseModel;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.sql.Date;
import java.text.SimpleDateFormat;

import oracle.jdbc.pool.OracleDataSource;
        
/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class SluzbyModel extends BaseModel {
    
    public Map<String,Object> getSluzba(String jmeno) throws SQLException, Exception {
        
        Map<String,Object> row = new HashMap<String,Object>();
        
        OracleDataSource ods = serviceLocator.getConnection();
        try (Connection conn = ods.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM sluzby WHERE nazev = ?");
             )
        {
            stmt.setString(1,jmeno);

            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next()) {
                    row.put("nazev",jmeno);
                    row.put("objekt",rs.getString("objekt"));
                    row.put("dostupnost_od", rs.getInt("dostupnost_od"));
                    row.put("dostupnost_do", rs.getInt("dostupnost_do"));
                }
                else {
                    return null;
                }
            }
        }
        
        return row;    
    }    
    
    public List<Map<String,Object>> getRezervace(String sluzba, String datum) throws SQLException, Exception 
    {
        Map<String,Object> sluzbaInfo = this.getSluzba(sluzba);
        
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        
        if (sluzbaInfo == null) {
            return result;
        }
        
        // init den pro sluzbu
        for (int i=(int)sluzbaInfo.get("dostupnost_od"); i <= (int)sluzbaInfo.get("dostupnost_do"); i++) {
            Map<String,Object> hodina = new HashMap<String,Object>();
            hodina.put("hodina",i);
            hodina.put("den", datum);
            result.add(hodina);
        }
        
        OracleDataSource ods = serviceLocator.getConnection();
        try (Connection conn = ods.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM sluzby_rezervace WHERE sluzba = ? AND den = ?");
            ) 
        {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            Date d = new Date(date.parse(datum).getTime());
            
            stmt.setString(1, sluzba);
            stmt.setDate(2, d);
            
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    
                    Map<String,Object> row = result.get(resultSet.getInt("hodina")-(int)sluzbaInfo.get("dostupnost_od"));

                    row.put("id", resultSet.getInt("id"));
                    row.put("zakaznik", resultSet.getInt("zakaznik"));
                }
            }
        }
        
        return result;
    }
    
}
