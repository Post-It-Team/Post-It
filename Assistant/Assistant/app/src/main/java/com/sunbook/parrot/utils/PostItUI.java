package com.sunbook.parrot.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunbook.parrot.R;
import com.sunbook.parrot.postit.Reminder;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by hieuapp on 03/04/2016.
 */
public class PostItUI {
    public static final String TYPE_ROBOTO_THIN = "Thin";
    public static final String ROBOTO_LIGHT = "RobotoTTF/Roboto-Light.ttf";
    public static final String ROBOTO_SLAB_REGULAR = "RobotoSlab/RobotoSlab-Regular.ttf";
    public static final String ROBOTO_SLAB_BOLD = "RobotoSlab/RobotoSlab-Bold.ttf";
    public static final String ROBOTO_BOLD = "RobotoTTF/Roboto-Bold.ttf";
    public static final String GRAY_300 = "#E0E0E0";
    public static final String GRAY_400 = "#BDBDBD";
    public static final String TEXT_DISABLE_MATERIAL_DARK = "#4DFFFFFF";
    public static Typeface getTypeface(Context context, String type){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), type);
        return typeface;
    }

    public static void setTextFont(Context context, TextView textView, String typeFont){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), typeFont);
        textView.setTypeface(typeface);
    }

    public static String dateFomat(long date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd", Locale.US);
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }
}
