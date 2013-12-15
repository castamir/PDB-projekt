package cz.vutbr.fit.pdb.security;

public interface IIdentity {

    public boolean isLoggendIn();

    public String getUsername();

    public String getPassword();
}
