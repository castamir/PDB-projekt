package cz.vutbr.fit.pdb.security;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public abstract class BaseIdentity implements IIdentity {

    private String username;
    private String password;

    /**
     *
     * @param username
     * @param password
     */
    public BaseIdentity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     *
     * @return
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return
     */
    @Override
    public String getPassword() {
        return password;
    }
}
