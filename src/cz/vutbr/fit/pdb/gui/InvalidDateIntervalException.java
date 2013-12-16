
package cz.vutbr.fit.pdb.gui;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class InvalidDateIntervalException extends Exception {

    String message;

    @Override
    public String getMessage() {
        return this.message;
    }
    public InvalidDateIntervalException(String message, int i) {
        this.message = message;
    }

}
