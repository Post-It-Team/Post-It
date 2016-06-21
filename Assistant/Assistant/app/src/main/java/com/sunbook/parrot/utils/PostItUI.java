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

    public static void setTaskHiden(Context context,View convertview){
        ImageView star = (ImageView)convertview.findViewById(R.id.im_star);
        TextView taskName = (TextView) convertview.findViewById(R.id.tv_reminder);
        TextView date = (TextView) convertview.findViewById(R.id.tv_date);
        star.setImageResource(R.mipmap.ic_start_hiden);
        taskName.setTextColor(PostItColor.getDisableColor());
        taskName.setPaintFlags(taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        setTextFont(context,taskName,ROBOTO_LIGHT);
        date.setTextColor(PostItColor.getDisableColor());
        date.setBackgroundColor(Color.WHITE);
    }

    public static void setTaskDisplay(Context context,View convertview, Reminder task){
        ImageView star = (ImageView)convertview.findViewById(R.id.im_star);
        TextView taskName = (TextView) convertview.findViewById(R.id.tv_reminder);
        TextView date = (TextView) convertview.findViewById(R.id.tv_date);
        star.setImageResource(R.mipmap.ic_star_yellow);
        taskName.setPaintFlags(taskName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        taskName.setTextColor(Color.BLACK);
        if(task.checkDeadline() != Reminder.IN_DATE){
            PostItUI.setTextFont(context,taskName,ROBOTO_BOLD);
            date.setTextColor(Color.WHITE);
            date.setBackgroundColor(Color.parseColor(PostItColor.TEAL_500));
        }
        setDateColor(context,task,convertview);
    }

    public static void setDateColor(Context context, Reminder task, View convertView){
        TextView date = (TextView) convertView.findViewById(R.id.tv_date);
        int deadlineStatus = task.checkDeadline();
        switch (deadlineStatus){
            case Reminder.IN_DATE:
                date.setBackgroundColor(Color.WHITE);
                date.setTextColor(Color.BLACK);
                break;
            case Reminder.NEAR_DEADLINE:
                int bgColor = context.getResources().getColor(R.color.yellow_300);
                date.setBackgroundColor(bgColor);
                date.setTextColor(Color.BLACK);
                break;
            case Reminder.OUT_OF_DATE:
                int bgRed = context.getResources().getColor(R.color.red_300);
                date.setBackgroundColor(bgRed);
                break;
            default:
                date.setBackgroundColor(Color.WHITE);
                break;
        }
    }

    public static void setTextHiden(TextView textView){
        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        textView.setTextColor(PostItColor.getDisableColor());
    }

    public static void setTaskDisplay(TextView textView){
        textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        textView.setTextColor(PostItColor.getSecondaryColor());
    }
}
