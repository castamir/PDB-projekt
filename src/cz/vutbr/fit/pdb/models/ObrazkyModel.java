/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.vutbr.fit.pdb.models;

import java.io.IOException;
import java.sql.Connection;
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
        
        OracleDataSource ods = serviceLocator.getConnection();
        try (Connection conn = ods.getConnection();)
        {
            conn.setAutoCommit(false);
            
            try (Statement stmt = conn.createStatement(); )
            {
                stmt.executeUpdate("INSERT INTO obrazky (img) VALUES (ordsys.ordimage.init())");
            }
            
            try (Statement stmt = conn.createStatement(); )
            {
                OracleResultSet rs = (OracleResultSet) stmt.executeQuery("SELECT id, img FROM obrazky ORDER BY id DESC LIMIT 1 FOR UPDATE");
                
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
    
    public OrdImage getImage(Integer id) throws SQLException {
        
        OracleDataSource ods = serviceLocator.getConnection();
        try (Connection conn = ods.getConnection();
             OraclePreparedStatement pstmt = (OraclePreparedStatement)conn.prepareStatement("SELECT img FROM obrazky WHERE id = ?"))
        {
            pstmt.setInt(1, id);
            
            OracleResultSet rs = (OracleResultSet) pstmt.executeQuery();
            
            if (!rs.next()) {
                return null;
            }
            
            return (OrdImage) rs.getORAData("img", OrdImage.getORADataFactory());
        }
    }
}
