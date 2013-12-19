package cz.vutbr.fit.pdb.models;

import cz.vutbr.fit.pdb.application.ServiceLocator;
import java.sql.Connection;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.PreparedStatement;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.sql.Date;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import oracle.jdbc.pool.OracleDataSource;

/**
 * Model pro práci s tabulkou 'sluzby' a 'sluzby_rezervace'
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class SluzbyModel extends BaseModel {

    private ZakaznikModel zakaznikModel;

    /**
     *
     */
    public SluzbyModel() {
        zakaznikModel = new ZakaznikModel();
    }

    /**
     * Vrátí informace o zvolené službě
     * @param jmeno
     * @return Klíč je atribut služby, hodnota je typu Object, konkrétní typ hodnoty je nutné testovat nebo vypisovat pomocí metody .toString()
     * @throws SQLException
     * @throws Exception
     */
    public Map<String, Object> getSluzba(String jmeno) throws SQLException, Exception {

        Map<String, Object> row = new HashMap<String, Object>();

        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM sluzby WHERE nazev = ?");) {
            stmt.setString(1, jmeno);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    row.put("nazev", jmeno);
                    row.put("objekt", rs.getString("objekt"));
                    row.put("dostupnost_od", rs.getInt("dostupnost_od"));
                    row.put("dostupnost_do", rs.getInt("dostupnost_do"));
                } else {
                    return null;
                }
            }
        }

        return row;
    }

    /**
     * Vrátí reezrvace služby pro zvolené datum
     * @param sluzba
     * @param datum
     * @return Seznam pro každou hodinu v dostupném časovém období pro službu. Hodnoty jsou mapu, kdy klíč je hodina a hodnota je informace o rezervaci.
     * @throws SQLException
     * @throws Exception
     */
    public List<Map<String, Object>> getRezervace(String sluzba, String datum) throws SQLException, Exception {
        Map<String, Object> sluzbaInfo = this.getSluzba(sluzba);

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        if (sluzbaInfo == null) {
            return result;
        }

        // init den pro sluzbu
        for (int i = (int) sluzbaInfo.get("dostupnost_od"); i < (int) sluzbaInfo.get("dostupnost_do"); i++) {
            Map<String, Object> hodina = new HashMap<String, Object>();
            hodina.put("hodina", i);
            hodina.put("den", datum);
            result.add(hodina);
        }

        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM sluzby_rezervace WHERE sluzba = ? AND den = ?");) {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            Date d = new Date(date.parse(datum).getTime());

            stmt.setString(1, sluzba);
            stmt.setDate(2, d);

            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {

                    Map<String, Object> row = result.get(resultSet.getInt("hodina") - (int) sluzbaInfo.get("dostupnost_od"));

                    row.put("id", resultSet.getInt("id"));
                    row.put("zakaznik", zakaznikModel.get(resultSet.getInt("zakaznik")));
                    row.put("poznamka", resultSet.getString("poznamka"));
                }
            }
        }

        return result;
    }

    /**
     * Vytvoří novo rezervaci služby
     * @param zakaznik
     * @param sluzba
     * @param datum
     * @param hodina
     * @param poznamka
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public boolean novaRezervace(int zakaznik, String sluzba, String datum, int hodina, String poznamka) throws SQLException, Exception {
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO sluzby_rezervace (sluzba, zakaznik, den, hodina, poznamka) VALUES(?,?,?,?,?)");) {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            Date d = new Date(date.parse(datum).getTime());

            stmt.setString(1, sluzba);
            stmt.setInt(2, zakaznik);
            stmt.setDate(3, d);
            stmt.setInt(4, hodina);
            stmt.setString(5, poznamka);

            return stmt.execute();
        }
    }

    /**
     * Upraví rezervaci služby
     * @param id
     * @param zakaznik
     * @param sluzba
     * @param datum
     * @param hodina
     * @param poznamka
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public boolean upravitRezervaci(int id, int zakaznik, String sluzba, String datum, int hodina, String poznamka) throws SQLException, Exception {
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection();
                PreparedStatement stmt = conn.prepareStatement("UPDATE sluzby_rezervace SET sluzba = ?, zakaznik = ?, den = ?, hodina = ?, poznamka = ? WHERE id = ?");) {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            Date d = new Date(date.parse(datum).getTime());

            stmt.setString(1, sluzba);
            stmt.setInt(2, zakaznik);
            stmt.setDate(3, d);
            stmt.setInt(4, hodina);
            stmt.setString(5, poznamka);

            stmt.setInt(6, id);

            return stmt.execute();
        }
    }

    /**
     * Smaže rezervaci služby
     * @param id
     * @return
     * @throws SQLException
     */
    public boolean smazatRezervaci(int id) throws SQLException {
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM sluzby_rezervace WHERE id = ?");) {
            stmt.setInt(1, id);
            return stmt.execute();
        }
    }

    /**
     * Vrátí průměrný počet rezervací na zákazníka do dnešního dne
     * @return Pruměrný počet rezervací
     * @throws SQLException 
     */
    public float prumernyPocetRezervaci() throws SQLException {
        float prumer = 0;

        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT nvl(avg(nvl2(sluzby_rezervace.id, 1, 0)),0) as prumer FROM zakaznik LEFT JOIN sluzby_rezervace ON sluzby_rezervace.zakaznik = zakaznik.id WHERE sluzby_rezervace.den <= trunc(sysdate) OR sluzby_rezervace.den IS NULL")) {
            while (rs.next()) {
                prumer = Float.parseFloat(rs.getString("prumer"));
            }
        }

        return prumer;
    }
}
