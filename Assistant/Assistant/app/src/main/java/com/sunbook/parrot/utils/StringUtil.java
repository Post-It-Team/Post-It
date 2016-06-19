package com.sunbook.parrot.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunbook.parrot.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by hieuapp on 03/04/2016.
 */
public class StringUtil {
    public static final String TYPE_ROBOTO_THIN = "Thin";
    public static final String TYPE_ROBOTO_LIGHT = "Light";
    public static final String ROBOTO_SLAB_REGULAR = "RobotoSlab/RobotoSlab-Regular.ttf";
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

    public static void setTaskHiden(ImageView star, TextView taskName, TextView date){

        taskName.setPaintFlags(taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        taskName.setTextColor(Color.parseColor(GRAY_300));
        date.setTextColor(Color.parseColor(GRAY_300));
        star.setImageResource(R.mipmap.ic_start_hiden);
    }

    public static void setTaskHiden(TextView textView){
        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        textView.setTextColor(PostItColor.getDisableColor());
    }

    public static void setTaskDisplay(TextView taskName, TextView date){
        taskName.setPaintFlags(taskName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        taskName.setTextColor(PostItColor.getSecondaryColor());
        date.setTextColor(PostItColor.getSecondaryColor());
    }

    public static void setTaskDisplay(TextView textView){
        textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        textView.setTextColor(PostItColor.getSecondaryColor());
    }
}
