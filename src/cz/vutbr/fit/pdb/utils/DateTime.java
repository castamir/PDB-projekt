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
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DateTime.DEFAULT);
        return sdf.format(c.getTime());
    }

    /**
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDay(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    /**
     *
     * @param string
     * @return
     * @throws ParseException
     */
    public static Date toDate(String string) throws ParseException {
        return DateTime.toDate(string, DEFAULT);
    }

    /**
     *
     * @param string
     * @param from_format
     * @return
     * @throws ParseException
     */
    public static Date toDate(String string, String from_format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DateTime.DEFAULT);
        return sdf.parse(string);
    }

    /**
     *
     * @param original
     * @return
     * @throws ParseException
     */
    public static String format(String original) throws ParseException {
        return format(original, DEFAULT, DEFAULT);
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
