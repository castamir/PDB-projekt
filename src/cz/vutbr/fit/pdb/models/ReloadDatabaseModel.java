package cz.vutbr.fit.pdb.models;

import cz.vutbr.fit.pdb.application.ServiceLocator;
import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.pool.OracleDataSource;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class ReloadDatabaseModel {

    private static final String COMMANDS_DELIMITER = ";";
    private static final String COMMANDS_TRIGGER = "; /";

    /**
     *
     * @throws SQLException
     */
    public static void resetDatabase() throws SQLException {
        ReloadDatabaseModel.loadSqlScript("table_init.sql");
        ReloadDatabaseModel.loadSqlScript(ReloadDatabaseModel.COMMANDS_TRIGGER, "triggers_init.sql");
        ReloadDatabaseModel.loadSqlScript("data_init.sql");
    }

    private static void loadSqlScript(String filename) throws SQLException {
        ReloadDatabaseModel.loadSqlScript(ReloadDatabaseModel.COMMANDS_DELIMITER, filename);
    }

    private static void loadSqlScript(String delimiter, String filename) throws SQLException {
        String originalLine, path, modifiedString, pattern, instrukce;
        StringBuilder sb = new StringBuilder();
        FileReader fr;
        try {
            path = new File("").getCanonicalPath();
            path = path.concat("/sql/" + filename);
            System.out.println(path);
            fr = new FileReader(new File(path));
        } catch (IOException ex) {
            Logger.getLogger(ReloadDatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        try (BufferedReader br = new BufferedReader(fr)) {
            while ((originalLine = br.readLine()) != null) {
                // odstraneni jednoradkovych komentaru
                pattern = "(.*)(--.*)$";
                modifiedString = originalLine.replaceAll(pattern, "$1");
                // prazdne radky jsou ignorovany
                if (!modifiedString.trim().equals("") || !modifiedString.trim().equals("/")) {
                    sb.append(modifiedString).append(" ");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ReloadDatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        modifiedString = sb.toString();
        // odstraneni viceradkovych komentaru
        //pattern = "(.*)(/\\*.*\\*/)(.*)";
        //modifiedString = sb.toString().replaceAll(pattern, "$1$3");

        // separovani instrukci
        String[] inst = modifiedString.split(delimiter);

        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection()) {

            for (int i = 0; i < inst.length; i++) {
                instrukce = inst[i].trim();
                if (!instrukce.equals("")) {
                    try (Statement stmt = conn.createStatement()) {
                        if (delimiter.equals(ReloadDatabaseModel.COMMANDS_TRIGGER)) {
                            instrukce = instrukce.concat(ReloadDatabaseModel.COMMANDS_DELIMITER);
                            stmt.executeUpdate(instrukce);
                        } else {
                            stmt.executeQuery(instrukce);
                        }
                    } catch (SQLException ex) {
                        System.out.println(">>" + instrukce + "<<");
                        Logger.getLogger(ReloadDatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }

    }

    /**
     *
     * @return
     */
    public static boolean isReloadRequired() {
        int pocet_tabulek = 0;
        try {
            OracleDataSource ods = ServiceLocator.getConnection();

            try (Connection conn = ods.getConnection(); Statement stmt = conn.createStatement(); ResultSet rset = stmt.executeQuery(
                    "select count(*) as pocet_tabulek from user_tables where table_name in ('REZERVACE', 'MAPA', 'SLUZBY_REZERVACE', 'SLUZBY', 'POKOJE', 'ZAKAZNIK', 'OBRAZKY', 'AREAL')")) {
                while (rset.next()) {
                    pocet_tabulek = rset.getInt("pocet_tabulek");
                }
            }
        } catch (SQLException sqlEx) {
            return false;
        }
        return pocet_tabulek != 8;
    }

    /**
     *
     * @return
     */
    public static boolean isConnectionValid() {
        try {
            OracleDataSource ods = ServiceLocator.getConnection();

            try (Connection conn = ods.getConnection(); Statement stmt = conn.createStatement(); ResultSet rset = stmt.executeQuery(
                    "select 1+2 as col1, 3-4 as col2 from dual")) {
                while (rset.next()) {
                }
            }
        } catch (SQLException sqlEx) {
            return false;
        }
        return true;
    }
}