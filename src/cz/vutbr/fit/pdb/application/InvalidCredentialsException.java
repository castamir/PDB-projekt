package cz.vutbr.fit.pdb.application;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class InvalidCredentialsException extends Exception {

    /**
     *
     */
    public InvalidCredentialsException() {
        super();
    }

    /**
     *
     * @param message
     */
    public InvalidCredentialsException(String message) {
        super(message);
    }
}