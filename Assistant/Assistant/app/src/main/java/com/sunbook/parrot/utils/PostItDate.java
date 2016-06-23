package com.sunbook.parrot.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hieuapp on 21/06/2016.
 */
public class PostItDate {

    public static final String TODAY = "today";
    public static final String NO_TIME = "no_time";
    public static final String NO_DATE = "no_date";
    public static String convertDate(long timestamp, String languageCode){
        String dateConverted = "";
        switch (languageCode){
            case LanguageCode.VIETNAM:
                dateConverted = convertToVietnam(timestamp);
                break;
            case LanguageCode.UNITED_STATES:
                break;
            default:
                break;
        }
        return dateConverted;
    }

    public static String convertToVietnam(long timestamp){
        String result = "";
        Calendar calendar = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        calendar.setTime(new Date(timestamp));
        today.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int thisYear = today.get(Calendar.YEAR);
        if(thisYear == year){
            int dayTimeStamp = calendar.get(Calendar.DAY_OF_MONTH);
            int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);
            if(dayTimeStamp == dayOfMonth){
                result = PostItDate.TODAY;
            }else {
                result = formatMonthVN(timestamp);
            }
        }else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            result = dateFormat.format(new Date(timestamp));
        }

        return result;
    }

    public static String formatMonthVN(long timestamp){
        if(timestamp == 0){
            return NO_DATE;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp));
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        return day + " thg "+month;
    }

    public static String formatHour(long timestamp){
        if(timestamp == 0){
            return NO_TIME;
        }
        long hour = timestamp / 60;
        long minute = timestamp % 60;
        String strMinute = String.valueOf(minute);
        if(minute/10 == 0){
            strMinute = "0"+minute;
        }
        return hour + ":" + strMinute;
    }
}
