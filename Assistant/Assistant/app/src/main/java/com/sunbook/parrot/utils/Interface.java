package com.sunbook.parrot.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunbook.parrot.R;
import com.sunbook.parrot.database.checklist.CheckListDB;
import com.sunbook.parrot.postit.Checklist;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by hieuapp on 03/04/2016.
 */
public class Interface {
    public static final String TYPE_ROBOTO_THIN = "Thin";
    public static final String TYPE_ROBOTO_LIGHT = "Light";
    public static final String ROBOTO_SLAB_REGULAR = "RobotoSlab/RobotoSlab-Regular.ttf";
    public static final String ROBOTO_SLAB_BOLD = "RobotoSlab/RobotoSlab-Bold.ttf";
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
        RelativeLayout layoutTask = (RelativeLayout)convertview.findViewById(R.id.task_layout);
        ImageView star = (ImageView)convertview.findViewById(R.id.im_star);
        TextView taskName = (TextView) convertview.findViewById(R.id.tv_reminder);
        TextView date = (TextView) convertview.findViewById(R.id.tv_date);
        layoutTask.setBackgroundColor(Color.WHITE);
        star.setImageResource(R.mipmap.ic_start_hiden);
        taskName.setTextColor(Color.parseColor(GRAY_300));
        taskName.setPaintFlags(taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        Interface.setTextFont(context,taskName,ROBOTO_SLAB_REGULAR);
        date.setTextColor(Color.parseColor(GRAY_300));
        date.setBackgroundColor(Color.WHITE);
    }

    public static void setTaskDisplay(Context context,View convertview, Checklist task){
        ImageView star = (ImageView)convertview.findViewById(R.id.im_star);
        TextView taskName = (TextView) convertview.findViewById(R.id.tv_reminder);
        TextView date = (TextView) convertview.findViewById(R.id.tv_date);
        star.setImageResource(R.mipmap.ic_star_yellow);
        taskName.setTextColor(PostItColor.getSecondaryColor());
        taskName.setPaintFlags(taskName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        date.setTextColor(PostItColor.getSecondaryColor());
        if(task.checkDeadline() != Checklist.IN_DATE){
            Interface.setTextFont(context,taskName,ROBOTO_SLAB_BOLD);
            taskName.setTextColor(Color.BLACK);
            date.setTextColor(Color.WHITE);
            date.setBackgroundColor(Color.parseColor(PostItColor.TEAL_500));
        }
        setBackgroundTask(context,task,convertview);
    }

    public static void setBackgroundTask(Context context, Checklist task, View convertView){
        RelativeLayout taskLayout = (RelativeLayout)convertView.findViewById(R.id.task_layout);
        int deadlineStatus = task.checkDeadline();
        switch (deadlineStatus){
            case Checklist.IN_DATE:
                taskLayout.setBackgroundColor(Color.WHITE);
                break;
            case Checklist.NEAR_DEADLINE:
                int bgColor = context.getResources().getColor(R.color.yellow_300);
                taskLayout.setBackgroundColor(bgColor);
                break;
            case Checklist.OUT_OF_DATE:
                int bgRed = context.getResources().getColor(R.color.red_300);
                taskLayout.setBackgroundColor(bgRed);
                break;
            default:
                taskLayout.setBackgroundColor(Color.WHITE);
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
