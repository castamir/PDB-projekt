package cz.vutbr.fit.pdb.security;

import cz.vutbr.fit.pdb.application.InvalidCredentialsException;
import cz.vutbr.fit.pdb.application.ServiceLocator;
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
public class Authenticator {

    private IIdentity identity = null;

    /**
     *
     * @return
     */
    public IIdentity getIdentity() {
        if (identity == null) {
            Properties current_properties = ServiceLocator.getProperties();
            identity = new DefaultIdentity(current_properties.getProperty("DB.LOGIN"), current_properties.getProperty("DB.PASSWORD"));
        }
        return identity;
    }

    /**
     *
     * @param username
     * @param password
     * @return
     * @throws InvalidCredentialsException
     */
    public IIdentity login(String username, String password) throws InvalidCredentialsException {

        identity = new Identity(username, password);
        if (!isConnectionValid()) {
            logout();
            throw new InvalidCredentialsException("Neplatné uživatelské jméno nebo heslo.");
        }
        return getIdentity();
    }

    /**
     *
     */
    public void logout() {
        identity = null;
    }

    private boolean isConnectionValid() {
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
