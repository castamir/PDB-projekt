package cz.vutbr.fit.pdb.security;

/**
 * Rozhraní pro identitu
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
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
