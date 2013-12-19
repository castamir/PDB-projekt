package cz.vutbr.fit.pdb.security;

import cz.vutbr.fit.pdb.application.InvalidCredentialsException;
import cz.vutbr.fit.pdb.application.ServiceLocator;
import cz.vutbr.fit.pdb.models.ReloadDatabaseModel;
import java.util.Properties;

/**
 * Třída pro ověření identity uživatele DB
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
        if (!ReloadDatabaseModel.isConnectionValid()) {
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
}
