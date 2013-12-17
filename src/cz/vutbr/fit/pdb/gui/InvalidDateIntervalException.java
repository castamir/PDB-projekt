package cz.vutbr.fit.pdb.gui;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class InvalidDateIntervalException extends Exception {

    String message;

    /**
     *
     * @return
     */
    @Override
    public String getMessage() {
        return this.message;
    }

    /**
     *
     * @param message
     * @param i
     */
    public InvalidDateIntervalException(String message, int i) {
        this.message = message;
    }
}
