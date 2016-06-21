package com.sunbook.parrot.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hieuapp on 21/06/2016.
 */
public class PostItDate {

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
                result = convertByHour(timestamp);
            }else {
                result = convertByMonth(timestamp);
            }
        }else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            result = dateFormat.format(new Date(timestamp));
        }

        return result;
    }

    public static String convertByMonth(long timestamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp));
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        return day + " thg "+month;
    }

    public static String convertByHour(long timestamp){
        return null;
    }
}
