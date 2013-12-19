package cz.vutbr.fit.pdb.application;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class InvalidCredentialsException extends Exception {

    /**
     * @param message zprava popisuji, odkud pochazi nevalidni prihlasovaci udaje
     */
    public InvalidCredentialsException(String message) {
        super(message);
    }
}