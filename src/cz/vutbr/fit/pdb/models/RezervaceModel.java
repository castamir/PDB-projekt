package cz.vutbr.fit.pdb.models;

import cz.vutbr.fit.pdb.application.ServiceLocator;
import cz.vutbr.fit.pdb.utils.DateTime;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oracle.jdbc.pool.OracleDataSource;

/**
 *
 * @author Pavel
 */
public class RezervaceModel extends BaseModel {

    /**
     *
     * @param datum_od
     * @param datum_do
     * @param pokoj
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public Map<Integer, Map<String, Object>> getRezervaceVObdobi(String datum_od, String datum_do, Integer pokoj) throws SQLException, ParseException {
        String[] pokoje = {pokoj.toString()};
        return getRezervaceVObdobi(datum_od, datum_do, pokoje);
    }

    /**
     *
     * @param datum_od
     * @param datum_do
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public Map<Integer, Map<String, Object>> getRezervaceVObdobi(String datum_od, String datum_do) throws SQLException, ParseException {
        String[] pokoje = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        return getRezervaceVObdobi(datum_od, datum_do, pokoje);
    }

    /**
     *
     * @param datum_od
     * @param datum_do
     * @param pokoje
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public Map<Integer, Map<String, Object>> getRezervaceVObdobi(String datum_od, String datum_do, String[] pokoje) throws SQLException, ParseException {

        String query = "SELECT r.id as id, r.pokoj as pokoj, r.do as do, r.od as od, z.jmeno || ' ' || z.prijmeni || ' (' || z.id || ')' as zakaznik "
                + "FROM rezervace r join zakaznik z on (z.id = r.zakaznik) WHERE "
                + "((od BETWEEN ? AND ?) OR (do BETWEEN ? AND ?) OR (? BETWEEN od AND do) OR (? BETWEEN od AND do))";

        if (pokoje.length > 0) {
            StringBuilder builder = new StringBuilder();
            for (String s : pokoje) {
                builder.append(",");
                builder.append(s);
            }
            query = query + " AND pokoj IN (" + builder.toString().substring(1) + ")";
        }

        Map<Integer, Map<String, Object>> result = new LinkedHashMap<>();

        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);) {
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

                    Map<String, Object> value = new HashMap<>();

                    value.put("id", rs.getInt("id"));
                    value.put("zakaznik", rs.getString("zakaznik"));
                    value.put("pokoj", rs.getInt("pokoj"));
                    value.put("od", DateTime.format(rs.getString("od"), "yyyy-MM-dd kk:mm:ss.S"));
                    value.put("do", DateTime.format(rs.getString("do"), "yyyy-MM-dd kk:mm:ss.S"));

                    result.put(rs.getInt("id"), value);
                }
            }
        }

        return result;
    }

    /**
     *
     * @param zakaznik
     * @param pokoje
     * @param datum_od
     * @param datum_do
     * @throws SQLException
     * @throws ParseException
     */
    public void vytvoritRezervaci(int zakaznik, List<Integer> pokoje, java.util.Date datum_od, java.util.Date datum_do) throws SQLException, ParseException {

        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO rezervace (zakaznik, pokoj, od, do) VALUES(?,?,?,?)");) {
            conn.setAutoCommit(false);

            Date d_od, d_do;

            d_od = new Date(datum_od.getTime());
            d_do = new Date(datum_do.getTime());

            for (Integer i : pokoje) {
                stmt.setInt(1, zakaznik);
                stmt.setInt(2, i);
                stmt.setDate(3, d_od);
                stmt.setDate(4, d_do);
                stmt.addBatch();
            }
            System.out.println(stmt);

            int[] result = stmt.executeBatch();

            conn.commit();
        }
    }

    /**
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public boolean smazatRezervaci(int id) throws SQLException {
        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM rezervace WHERE id = ?");) {
            stmt.setInt(1, id);
            return stmt.execute();
        }
    }

    /**
     *
     * @param datum_od
     * @param datum_do
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public List<Integer> rezervovanePokojeVObdobi(String datum_od, String datum_do) throws SQLException, ParseException {

        List<Integer> pokoje = new ArrayList<>();

        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT pokoj FROM rezervace WHERE (od BETWEEN ? AND ?) OR (do BETWEEN ? AND ?) OR (? BETWEEN od AND do) OR (? BETWEEN od AND do)");) {
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

    /**
     *
     * @return
     * @throws SQLException
     */
    public Map<Integer, String> getPokoje() throws SQLException {

        Map<Integer, String> pokoje = new LinkedHashMap<>();

        OracleDataSource ods = ServiceLocator.getConnection();
        try (Connection conn = ods.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM pokoje")) {
            while (rs.next()) {
                pokoje.put(rs.getInt("id"), rs.getString("nazev"));
            }
        }

        return pokoje;
    }
}
