package com.sunbook.parrot.checklist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunbook.parrot.R;
import com.sunbook.parrot.database.checklist.CheckListDB;
import com.sunbook.parrot.database.checklist.ChecklistSchema;
import com.sunbook.parrot.postit.Reminder;
import com.sunbook.parrot.utils.PostItColor;
import com.sunbook.parrot.utils.PostItDate;
import com.sunbook.parrot.utils.PostItUI;

import java.util.ArrayList;

/**
 * Created by hieuapp on 17/02/2016.
 */
public class CheckListAdapter extends ArrayAdapter<Reminder> {
    ArrayList<Reminder> reminders = null;
    Activity context;
    CheckListDB checkListDB;

    public static final String TAG = "ChecklistAdapter";
    public CheckListAdapter(Activity context, int resource, ArrayList<Reminder> data) {
        super(context, resource, data);
        this.reminders = data;
        this.context = context;
        this.checkListDB = new CheckListDB(context);
        checkListDB.open();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final Reminder reminder = reminders.get(position);
        LayoutInflater layoutInflater = context.getLayoutInflater();
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.checklist_item_detail,null);
            viewHolder = new ViewHolder();
            viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.cb_item);
            viewHolder.star = (ImageView)convertView.findViewById(R.id.im_star);
            viewHolder.taskName = (TextView) convertView.findViewById(R.id.tv_reminder);
            viewHolder.date = (TextView) convertView.findViewById(R.id.tv_date);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        displayTimeRemind(viewHolder.date, reminder);
        if(reminder.isDone()){
            setTaskHiden(context, viewHolder);
        }else {
            setTaskDisplay(context, reminder, viewHolder);
        }
        if(!reminder.isImportant()){
            viewHolder.star.setImageResource(R.mipmap.ic_star_border_48dp);
        }
        viewHolder.taskName.setText(reminder.getTitle());
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finalViewHolder.checkBox.isChecked()){
                    setTaskHiden(context, finalViewHolder);
                    if(!reminder.isImportant()){
                        finalViewHolder.star.setImageResource(R.mipmap.ic_star_border_48dp);
                    }
                }else {
                    setTaskDisplay(context, reminder, finalViewHolder);
                    if(!reminder.isImportant()){
                        finalViewHolder.star.setImageResource(R.mipmap.ic_star_border_48dp);
                    }
                }
            }
        });
        return convertView;
    }

    public void setDoneChecklist(int position, boolean done){
        Reminder reminder = reminders.get(position);
        reminder.setIsDone(done);
        ContentValues values = new ContentValues();
        values.put(ChecklistSchema._ID, reminder.getId());
        values.put(ChecklistSchema.COLUMN_TITLE, reminder.getTitle());
        values.put(ChecklistSchema.COLUMN_DEADLINE, reminder.getDeadline());
        values.put(ChecklistSchema.COLUMN_IMPORTANT,1);
        if(reminder.isDone()){
            values.put(ChecklistSchema.COLUMN_DONE,1);
        }else {
            values.put(ChecklistSchema.COLUMN_DONE,0);
        }

        checkListDB.daoAccess.update(ChecklistSchema.CHECKLIST_TABLE,values,
                "_id = "+ reminder.getId(),null);
    }

    private void setDateColor(Context context, Reminder task, ViewHolder viewHolder){
        int deadlineStatus = task.checkDeadline();
        switch (deadlineStatus){
            case Reminder.IN_DATE:
                viewHolder.date.setBackgroundColor(Color.WHITE);
                viewHolder.date.setTextColor(Color.BLACK);
                break;
            case Reminder.NEAR_DEADLINE:
                int bgColor = context.getResources().getColor(R.color.yellow_300);
                viewHolder.date.setBackgroundColor(bgColor);
                viewHolder.date.setTextColor(Color.BLACK);
                break;
            case Reminder.OUT_OF_DATE:
                int bgRed = context.getResources().getColor(R.color.red_300);
                viewHolder.date.setBackgroundColor(bgRed);
                break;
            default:
                viewHolder.date.setBackgroundColor(Color.WHITE);
                break;
        }
    }

    private void setTaskHiden(Context context, ViewHolder viewHolder){
        viewHolder.star.setImageResource(R.mipmap.ic_start_hiden);
        viewHolder.taskName.setTextColor(PostItColor.getDisableColor());
        viewHolder.taskName.setPaintFlags(viewHolder.taskName.getPaintFlags()
                | Paint.STRIKE_THRU_TEXT_FLAG);
        PostItUI.setTextFont(context,viewHolder.taskName,PostItUI.ROBOTO_LIGHT);
        viewHolder.date.setTextColor(PostItColor.getDisableColor());
        viewHolder.date.setBackgroundColor(Color.WHITE);
    }

    private void setTaskDisplay(Context context, Reminder reminder, ViewHolder viewHolder){
        viewHolder.star.setImageResource(R.mipmap.ic_star_yellow);
        viewHolder.taskName.setPaintFlags(viewHolder.taskName.getPaintFlags()
                & (~Paint.STRIKE_THRU_TEXT_FLAG));
        viewHolder.taskName.setTextColor(Color.BLACK);
        if(reminder.checkDeadline() != Reminder.IN_DATE){
            PostItUI.setTextFont(context,viewHolder.taskName,PostItUI.ROBOTO_BOLD);
            viewHolder.date.setTextColor(Color.WHITE);
            viewHolder.date.setBackgroundColor(Color.parseColor(PostItColor.TEAL_500));
        }
        setDateColor(context, reminder, viewHolder);
    }

    private void displayTimeRemind(TextView nextAlarm, Reminder nextReminder){
        long time = nextReminder.getTime();
        long date = nextReminder.getDeadline();
        String dateRemind = PostItDate.convertToVietnam(date);
        if(dateRemind.equals(PostItDate.TODAY)){
            String hour = PostItDate.formatHour(time);
            nextAlarm.setText(hour);
            if(!hour.equals(PostItDate.NO_TIME)){
                nextAlarm.setText("");
                nextAlarm.setBackgroundColor(Color.WHITE);
            }
        }else if(dateRemind.equals(PostItDate.NO_DATE)){
            nextAlarm.setText("");
        }else {
            nextAlarm.setText(dateRemind);
        }
    }

    static class ViewHolder {
        CheckBox checkBox;
        TextView taskName, date;
        ImageView star;
    }
}
