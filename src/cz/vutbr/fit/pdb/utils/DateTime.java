package cz.vutbr.fit.pdb.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class DateTime {

    /**
     * dalsi zname formaty pouzite v aplikaci: "yyyy-MM-dd kk:mm:ss.S"
     */
    public static final String DEFAULT = "yyyy-MM-dd";

    /**
     *
     * @return
     */
    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    /**
     *
     * @param original
     * @param from_format
     * @return
     * @throws ParseException
     */
    public static String format(String original, String from_format) throws ParseException {
        return format(original, from_format, DEFAULT);
    }

    /**
     *
     * @param original
     * @param from_format
     * @param to_format
     * @return
     * @throws ParseException
     */
    public static String format(String original, String from_format, String to_format) throws ParseException {
        SimpleDateFormat sdf_from = new SimpleDateFormat(from_format);
        SimpleDateFormat sdf_to = new SimpleDateFormat(to_format);
        Date tmp = sdf_from.parse(original);
        return sdf_to.format(tmp);
    }
}
