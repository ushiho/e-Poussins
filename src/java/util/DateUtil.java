/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author moulaYounes
 */
public class DateUtil {

    public static java.sql.Date getSqlDate(java.util.Date date) {
        if (date != null) {
            return new java.sql.Date(date.getTime());
        }
        return null;
    }

    public static java.sql.Timestamp convertFromDateToTimestamp(java.util.Date date) {
        return new java.sql.Timestamp(date.getTime());
    }

    public static java.sql.Timestamp getSqlDateTime(java.util.Date date) {
        return new java.sql.Timestamp(date.getTime());
    }

    public static String getYearOfCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        return simpleDateFormat.format(new Date());
    }

    public static String formateDate(String pattern, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        if (date != null) {
            return simpleDateFormat.format(date);
        } else {
            return "";
        }
    }

    public static Date parse(String date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return simpleDateFormat.parse(date);
        } catch (ParseException ex) {
            return null;
        }
    }

    public static java.sql.Date convertFormUtilToSql(java.util.Date date) {
        if (date != null) {
            return new java.sql.Date(date.getTime());
        } else {
            return null;
        }
    }

    public static java.sql.Timestamp convertFormUtilToTimestamp(java.util.Date date) {
        if (date != null) {
            return new java.sql.Timestamp(date.getTime());
        } else {
            return null;
        }
    }

    public static int addMonthToDate(int nb, java.util.Date dateAModify) {
        if (dateAModify != null) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(dateAModify);
            gc.add(GregorianCalendar.MONTH, nb);
            return gc.getTime().getMonth();
        } else {
            return -1;
        }
    }

    public static int addYearToDate(int nb, java.util.Date dateAModify) {
        if (dateAModify != null) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(dateAModify);
            gc.add(GregorianCalendar.YEAR, nb);
            return gc.getTime().getYear();
        } else {
            return -1;
        }
    }

    //test two dates
    public static int compareYrearsOfTwoDates(Date dateDebut, Date dateFin) {
        if (dateDebut == null || dateFin == null) {
            return -1;
        } else if (dateDebut.compareTo(dateFin) > 0) {
            return -2;
        } else if (dateDebut.getYear() != dateFin.getYear()) {
            return -3;
        }
        return 1;
    }

    public static Date getSqlDateToSaveInDB(String date) {
        if (date != null && !"".equals(date)) {
            return getSqlDate(parse(date));
        }
        return null;
    }

    public static Date subDayFromDate(Date dateToSubs) {
        if (dateToSubs != null) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(dateToSubs);
            gc.add(GregorianCalendar.DATE, -1);
            return getSqlDate(gc.getTime());
        }
        return null;
    }

//    public static Date constructDateFromDays(Integer jrMin, Integer jrMax, Integer month,Integer annee) {
//        if(month==null||annee==null){
//            return null;
//        }
//        
//    }
    public static Integer getYearOfDate(Date date) {
        if (date != null) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(date);
            System.out.println("From getYearOfDate ha year :" + gc.get(Calendar.YEAR));
            return gc.get(Calendar.YEAR);
        }
        return null;
    }

    // Get the number of days in that month
    public static int maxDayInMonthOfYear(int iYear, int iMonth) {
        //must init iMonth by 1 
        System.out.println("from maxDayInMonthOfYear : month choisis" + iMonth);
        Calendar mycal = new GregorianCalendar(iYear, iMonth - 1, 1);
        return mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int weekNumberFromDate(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.WEEK_OF_YEAR);
        }
        return -1;
    }
}
