package cz.vutbr.fit.pdb.security;

/**
 * Třída pro identitu, údaje bere z konfguračního souboru
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class DefaultIdentity extends BaseIdentity implements IIdentity {

    /**
     * 
     * @param username
     * @param password
     */
    public DefaultIdentity(String username, String password) {
        super(username, password);
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isLoggendIn() {
        return false;
    }
}
