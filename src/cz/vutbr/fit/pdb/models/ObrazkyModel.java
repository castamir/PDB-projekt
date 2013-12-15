/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.vutbr.fit.pdb.models;

import cz.vutbr.fit.pdb.application.ServiceLocator;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    
    public Integer insertImage(String path) throws SQLException {
        
        Integer id;
        
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection();)
        {
            conn.setAutoCommit(false);
            
            try (Statement stmt = conn.createStatement(); )
            {
                stmt.executeUpdate("INSERT INTO obrazky (id, img) VALUES (obrazky_seq.nextval, ORDSYS.ORDImage.init())");
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
                
                // TODO - dodelat vlasntosti obrazku
            }
            
            conn.commit();
        }
        
        return id;
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
}