package cz.vutbr.fit.pdb.security;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class Identity extends BaseIdentity implements IIdentity {

    public Identity(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean isLoggendIn() {
        return true;
    }
}
