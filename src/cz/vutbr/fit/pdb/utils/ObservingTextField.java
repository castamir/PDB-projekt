package cz.vutbr.fit.pdb.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTextField;

public class ObservingTextField extends JTextField implements Observer {

    private static final long serialVersionUID = 1L;

    @Override
    public void update(Observable o, Object arg) {
        Calendar calendar = (Calendar) arg;
        setText(reformatDate(calendar));
    }

    public static String reformatDate(Calendar cal) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }
}
