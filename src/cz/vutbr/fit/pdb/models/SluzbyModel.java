
package cz.vutbr.fit.pdb.models;

import cz.vutbr.fit.pdb.application.ServiceLocator;
import java.sql.Connection;

import java.sql.ResultSet;

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
    
    private ZakaznikModel zakaznikModel;
    
    public SluzbyModel() {
        zakaznikModel = new ZakaznikModel();
    }
    
    public Map<String,Object> getSluzba(String jmeno) throws SQLException, Exception {
        
        Map<String,Object> row = new HashMap<String,Object>();
        
        OracleDataSource ods = ServiceLocator.getConnection();
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
        for (int i=(int)sluzbaInfo.get("dostupnost_od"); i < (int)sluzbaInfo.get("dostupnost_do"); i++) {
            Map<String,Object> hodina = new HashMap<String,Object>();
            hodina.put("hodina",i);
            hodina.put("den", datum);
            result.add(hodina);
        }
        
        OracleDataSource ods = ServiceLocator.getConnection();
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
                    row.put("zakaznik", zakaznikModel.get(resultSet.getInt("zakaznik")));
                    row.put("poznamka",resultSet.getString("poznamka"));
                }
            }
        }
        
        return result;
    }
    
    
    public boolean novaRezervace(int zakaznik, String sluzba, String datum, int hodina, String poznamka) throws SQLException, Exception {
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO sluzby_rezervace (sluzba, zakaznik, den, hodina, poznamka) VALUES(?,?,?,?,?)");
             )
        {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            Date d = new Date(date.parse(datum).getTime());
            
            stmt.setString(1,sluzba);   
            stmt.setInt(2, zakaznik);
            stmt.setDate(3,d);
            stmt.setInt(4,hodina);
            stmt.setString(5,poznamka);

            return stmt.execute();
        }
    }
    
    public boolean upravitRezervaci(int id, int zakaznik, String sluzba, String datum, int hodina, String poznamka) throws SQLException, Exception {
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("UPDATE sluzby_rezervace SET sluzba = ?, zakaznik = ?, den = ?, hodina = ?, poznamka = ? WHERE id = ?");
             )
        {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            Date d = new Date(date.parse(datum).getTime());
            
            stmt.setString(1,sluzba);   
            stmt.setInt(2, zakaznik);
            stmt.setDate(3,d);
            stmt.setInt(4,hodina);
            stmt.setString(5,poznamka);
            
            stmt.setInt(6,id);

            return stmt.execute();
        }
    }
    
    public boolean smazatRezervaci(int id) throws SQLException {
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM sluzby_rezervace WHERE id = ?");
             )
        {
            stmt.setInt(1, id);
            return stmt.execute();
        }
    }
    
}
