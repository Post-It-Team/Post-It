package com.sunbook.parrot.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by hieuapp on 03/04/2016.
 */
public class StringUtil {
    public static final String TYPE_ROBOTO_THIN = "Thin";
    public static final String TYPE_ROBOTO_LIGHT = "Light";
    public static final String ROBOTO_SLAB_REGULAR = "RobotoSlab/RobotoSlab-Regular.ttf";
    public static Typeface getTypeface(Context context, String type){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                type);
        return typeface;
    }

    public static String dateFomat(long date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd", Locale.US);
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }
}
