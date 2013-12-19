package cz.vutbr.fit.pdb.security;

/**
 * Třída pro identitu, přihlašovací údaje bere od uživatele z dialogu
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class Identity extends BaseIdentity implements IIdentity {

    /**
     *
     * @param username
     * @param password
     */
    public Identity(String username, String password) {
        super(username, password);
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isLoggendIn() {
        return true;
    }
}
