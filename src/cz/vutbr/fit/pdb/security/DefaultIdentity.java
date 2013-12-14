package cz.vutbr.fit.pdb.security;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class DefaultIdentity extends BaseIdentity implements IIdentity {

    public DefaultIdentity(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean isLoggendIn() {
        return false;
    }
}
