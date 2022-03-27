package org.jeecg.modules.KM.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
 
public class KMDateUtils {

    public static String formatDateyyyyMM(Date date){
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMM");
        return sd.format(date);
    }

    public static String formatDateyyyyMMdd(Date date){
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
        return sd.format(date);
    }

    public static String formatDate1(Date date){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        return sd.format(date);
    }

    public static String formatDate2(Date date){
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sd.format(date);
    }



    public static String formatDate3(Date date){
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmms");
        return sd.format(date);
    }

    public static String formatDate4(Date date){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sd.format(date);
    }

    public static Date parseTZDateString(String dateString){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getTimeStamp(){
        Long timeStamp = System.currentTimeMillis();
        String timeStampString  =  Long.toString(timeStamp);
        return timeStampString.substring(0,timeStampString.length()-3);
    }

    public static Date parse(String src){
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmms");
        try {
            return sd.parse(src);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parse2(String src){
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        try {
            return sd.parse(src);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
