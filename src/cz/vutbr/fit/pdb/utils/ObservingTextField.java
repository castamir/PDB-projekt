package cz.vutbr.fit.pdb.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTextField;

/**
 * @author originál z http://thehow2tutorial.blogspot.cz/p/downloads.html
 * Upravy:
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class ObservingTextField extends JTextField implements Observer {

    private static final long serialVersionUID = 1L;

    /**
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        Calendar calendar = (Calendar) arg;
        setText(reformatDate(calendar));
    }

    /**
     *
     * @param cal
     * @return
     */
    public static String reformatDate(Calendar cal) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }
}
