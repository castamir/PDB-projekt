package cz.vutbr.fit.pdb.security;

/**
 *
 * @author castamir
 */
public interface IIdentity {

    /**
     *
     * @return
     */
    public boolean isLoggendIn();

    /**
     *
     * @return
     */
    public String getUsername();

    /**
     *
     * @return
     */
    public String getPassword();
}
