/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.vutbr.fit.pdb.models;

import cz.vutbr.fit.pdb.gui.myIcon;

import cz.vutbr.fit.pdb.application.ServiceLocator;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.pool.OracleDataSource;
import oracle.ord.im.OrdImage;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class ObrazkyModel extends BaseModel {
    
    public Integer insertImage(String path, int zakaznik) throws SQLException {
        
        Integer id;
        
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection();)
        {
            conn.setAutoCommit(false);
            
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO obrazky (id, img, zakaznik) VALUES (obrazky_seq.nextval, ORDSYS.ORDImage.init(), ?)"); )
            {
                stmt.setInt(1, zakaznik);
                
                stmt.executeUpdate();
            }
            
            try (Statement stmt = conn.createStatement(); )
            {
                OracleResultSet rs = (OracleResultSet) stmt.executeQuery("SELECT id, img FROM obrazky ORDER BY id DESC FOR UPDATE");
                
                if (!rs.next()) {
                    return null;
                }
                
                OrdImage imgProxy = (OrdImage) rs.getORAData("img", OrdImage.getORADataFactory());
                id = rs.getInt("id");
                
                rs.close();
                
                try {
                    imgProxy.loadDataFromFile(path);
                    imgProxy.setProperties();
                }
                catch (IOException e) {
                    return null;
                }
                
                try (OraclePreparedStatement pstmt = (OraclePreparedStatement) conn.prepareStatement("UPDATE obrazky SET img = ? WHERE id = ?"))
                {
                    pstmt.setORAData(1, imgProxy);
                    pstmt.setInt(2, id);
                    pstmt.executeUpdate();
                }
                
                try (OraclePreparedStatement pstmt = (OraclePreparedStatement) conn.prepareStatement("UPDATE obrazky o SET o.img_si = SI_StillImage(o.img.getContent()) WHERE id = ?"))
                {
                    pstmt.setInt(1, id);
                    pstmt.executeUpdate();
                }
                
                try (OraclePreparedStatement pstmt = (OraclePreparedStatement) conn.prepareStatement("UPDATE obrazky SET "
                        + "img_ac = SI_AverageColor(img_si), "
                        + "img_ch = SI_ColorHistogram(img_si), "
                        + "img_pc = SI_PositionalColor(img_si), "
                        + "img_tx = SI_Texture(img_si) "
                        + "WHERE id = ?"))
                {
                    pstmt.setInt(1, id);
                    pstmt.executeUpdate();
                }
            }
            
            conn.commit();
        }
        
        return id;
    }
    
    public Map<Integer, myIcon> getImagesOfCustomer(int customer) throws SQLException {
    
        Map<Integer, myIcon> result = new HashMap<>();
        
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection();
               OraclePreparedStatement pstmt = (OraclePreparedStatement)conn.prepareStatement("SELECT id, img FROM obrazky WHERE zakaznik = ?"))
        {
            pstmt.setInt(1, customer);
            
            OracleResultSet rs = (OracleResultSet) pstmt.executeQuery();
            
            while (rs.next()) {
                OrdImage img = (OrdImage) rs.getORAData("img", OrdImage.getORADataFactory());
                byte[] tmp = img.getDataInByteArray();
                
                ImageIcon i = new ImageIcon(tmp);
                myIcon tmpIcon = new myIcon(i);
                result.put(rs.getInt("id"), tmpIcon);
            }
        } 
        catch (IOException e) {
            result = null;
        }
        
        return result;
    }
    
    public byte[] getImage(Integer id) throws SQLException {
        

        byte[] result;
        OrdImage img;
        
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection();
             OraclePreparedStatement pstmt = (OraclePreparedStatement)conn.prepareStatement("SELECT img FROM obrazky WHERE id = ?"))
        {
            pstmt.setInt(1, id);
            
            OracleResultSet rs = (OracleResultSet) pstmt.executeQuery();
            
            if (!rs.next()) {
                result = null;
            }
            else {
                img = (OrdImage) rs.getORAData("img", OrdImage.getORADataFactory());

                result = img.getDataInByteArray();
            }
        } 
        catch (IOException e) {
            result = null;
        }
        
        return result;
    }
    
    public boolean delete(Integer id) throws SQLException {
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM obrazky WHERE id = ?");
             )
        {
            stmt.setInt(1, id);
            return stmt.execute();
        }
    }
    
    public void rotateImage(int id) throws SQLException {
        
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("CALL Rotate_image(?)");)
        {
            stmt.setInt(1,id);
            
            stmt.execute();
        }
    }
    
    public Map<Integer, myIcon> getTheMostSimilar(Integer id, double weightAC, double weightCH, double weightPC, double weightTX) throws SQLException {
        Map<Integer, myIcon> result = new HashMap<>();
        OracleDataSource ods = ServiceLocator.getConnection();
         try (Connection conn = ods.getConnection();
               OraclePreparedStatement pstmt = (OraclePreparedStatement)conn.prepareStatement("SELECT dst.id, dst.img, SI_ScoreByFtrList("
                + "new SI_FeatureList(src.img_ac,?,src.img_ch,?,src.img_pc,?,src.img_tx,?),dst.img_si)"
                + " as similarity FROM obrazky src, obrazky dst "
                + "WHERE src.id = ? AND dst.id <> src.id ORDER BY similarity ASC")
              )
        {
            //pstmt.setInt(1, customer);
            pstmt.setDouble(1, weightAC);
            pstmt.setDouble(2, weightCH);
            pstmt.setDouble(3, weightPC);
            pstmt.setDouble(4, weightTX);
            pstmt.setInt(5, id);
            
            OracleResultSet rs = (OracleResultSet) pstmt.executeQuery();
            
            while (rs.next()) {
                OrdImage img = (OrdImage) rs.getORAData("img", OrdImage.getORADataFactory());
                byte[] tmp = img.getDataInByteArray();
                
                ImageIcon i = new ImageIcon(tmp);
                myIcon tmpIcon = new myIcon(i);
                result.put(rs.getInt("id"), tmpIcon);
            }
        } 
        catch (IOException e) {
            result = null;
        }
        
        return result;
        /*String simVyrobce = null;
        String simModel = null;
        // najdeme zaznam podobneho fota
        PreparedStatement pstmtSelect = connection.prepareStatement(
                "SELECT dst.vyrobce, dst.model, SI_ScoreByFtrList("
                + "new SI_FeatureList(src.foto_ac,?,src.foto_ch,?,src.foto_pc,?,src.foto_tx,?),dst.foto_si)"
                + " as similarity FROM vozidlo src, vozidlo dst "
                + "WHERE (src.vyrobce <> dst.vyrobce OR src.model <> dst.model) "
                + "AND src.vyrobce = ? and src.model = ? ORDER BY similarity ASC");
        try {
            pstmtSelect.setDouble(1, weightAC);
            pstmtSelect.setDouble(2, weightCH);
            pstmtSelect.setDouble(3, weightPC);
            pstmtSelect.setDouble(4, weightTX);
            pstmtSelect.setString(5, this.vyrobce);
            pstmtSelect.setString(6, this.model);
            ResultSet rset = pstmtSelect.executeQuery();
            try {
                if (rset.next()) {
                    simVyrobce = rset.getString(1);
                    simModel = rset.getString(2);
                }
            } finally {
                rset.close();
            }
        } finally {
            pstmtSelect.close();
        }
        // nalezneme ziskane vozidlo v katalogu (tam je "jeho" objekt)
        for (Iterator<Vozidlo> i = katalog.getVozidloIterator(); i.hasNext();) {
            Vozidlo v = i.next();
            if (v.vyrobce.equals(simVyrobce) && v.model.equals(simModel)) {
                return v;
            }
        }
        // pokud nenalezneme, tak null
        return null;*/
        //return null;
    }
}