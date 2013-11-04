package cz.vutbr.fit.pdb;

import java.sql.SQLException;
import java.util.Properties;
import oracle.jdbc.pool.OracleDataSource;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class ServiceLocator {

    private Properties properties;

    public ServiceLocator(Properties properties) {
        this.properties = properties;
    }

    public OracleDataSource getConnection() throws SQLException {
        OracleDataSource ods = new OracleDataSource();
        String connectionString = "jdbc:oracle:thin:@" + properties.getProperty("DB.HOST") + ":" + properties.getProperty("DB.PORT") + ":" + properties.getProperty("DB.SID");
        ods.setURL(connectionString);
        ods.setUser(properties.getProperty("DB.LOGIN"));
        ods.setPassword(properties.getProperty("DB.PASSWORD"));
        return ods;
    }
}
