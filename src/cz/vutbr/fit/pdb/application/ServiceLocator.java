package cz.vutbr.fit.pdb.application;

import cz.vutbr.fit.pdb.config.Loader;
import cz.vutbr.fit.pdb.security.DefaultIdentity;
import cz.vutbr.fit.pdb.security.IIdentity;
import cz.vutbr.fit.pdb.security.Identity;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import oracle.jdbc.pool.OracleDataSource;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class ServiceLocator {

    private static Properties properties = null;
    private static IIdentity identity = null;

    public ServiceLocator() {
        Loader loader = new Loader();
        ServiceLocator.properties = loader.getProperties();
    }

    public static OracleDataSource getConnection() throws SQLException {
        OracleDataSource ods = new OracleDataSource();
        IIdentity current_identity = ServiceLocator.getIdentity();
        Properties current_properties = ServiceLocator.getProperties();
        String connectionString = "jdbc:oracle:thin:@" + current_properties.getProperty("DB.HOST") + ":" + current_properties.getProperty("DB.PORT") + ":" + current_properties.getProperty("DB.SID");
        ods.setURL(connectionString);

        ods.setUser(current_identity.getUsername());
        ods.setPassword(current_identity.getPassword());
        return ods;
    }

    public static Properties getProperties() {
        if (ServiceLocator.properties == null) {
            Loader loader = new Loader();
            ServiceLocator.properties = loader.getProperties();
        }
        return ServiceLocator.properties;
    }

    public static IIdentity getIdentity() {
        if (ServiceLocator.identity == null) {
            Properties current_properties = ServiceLocator.getProperties();
            ServiceLocator.identity = new DefaultIdentity(current_properties.getProperty("DB.LOGIN"), current_properties.getProperty("DB.PASSWORD"));
        }
        return ServiceLocator.identity;
    }

    public static IIdentity login(String username, String password) throws InvalidCredentialsException {
        ServiceLocator.identity = new Identity(username, password);
        if (!ServiceLocator.isConnectionValid()) {
            ServiceLocator.logout();
            throw new InvalidCredentialsException("Neplatné uživatelské jméno nebo heslo.");
        }
        return ServiceLocator.identity;
    }

    public static void logout() {
        ServiceLocator.identity = null;
    }

    private static boolean isConnectionValid() {
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
