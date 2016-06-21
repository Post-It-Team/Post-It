package com.sunbook.parrot.checklist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sunbook.parrot.MainActivity;
import com.sunbook.parrot.R;
import com.sunbook.parrot.database.checklist.CheckListDB;
import com.sunbook.parrot.database.checklist.ChecklistSchema;
import com.sunbook.parrot.postit.Reminder;
import com.sunbook.parrot.utils.PostItUI;

import java.util.ArrayList;

/**
 * Created by hieuapp on 17/02/2016.
 */
public class CheckListAdapter extends ArrayAdapter<Reminder> {
    ArrayList<Reminder> dataCheckList = null;
    Activity context;
    public static final int MAX_ITEM_CHECKLIST = 5;
    CheckListDB checkListDB;

    public static final String TAG = "ChecklistAdapter";
    public CheckListAdapter(Activity context, int resource, ArrayList<Reminder> data, CheckListDB checkListDB) {
        super(context, resource, data);
        this.dataCheckList = data;
        this.context = context;
        this.checkListDB = checkListDB;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater layoutInflater = context.getLayoutInflater();
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.checklist_item,null);
            viewHolder = new ViewHolder(context,convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        showCheckList(viewHolder,dataCheckList, position);
        return convertView;
    }

    public void showCheckList(ViewHolder viewHolder, final ArrayList<Reminder> dataCheckList, final int position){
        viewHolder.name.setText(dataCheckList.get(position).getTitle());
        Typeface regular = PostItUI.getTypeface(context, PostItUI.ROBOTO_SLAB_REGULAR);
        viewHolder.name.setTypeface(regular);
        final ViewHolder finalViewHolder = viewHolder;
        if(dataCheckList.get(position).isDone()){
            viewHolder.checkBox.setChecked(true);
            PostItUI.setTextHiden(viewHolder.name);
        }
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    PostItUI.setTextHiden(finalViewHolder.name);
                    setDoneChecklist(position,true);
                }else {
                    // un trike text
                    PostItUI.setTaskDisplay(finalViewHolder.name);
                    setDoneChecklist(position,false);
                }
            }
        });
    }

    public void setDoneChecklist(int position, boolean done){
        Reminder reminder = dataCheckList.get(position);
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox checkBox;
        TextView name, deadline;
        Context context;

        public ViewHolder(Context context,View itemView) {
            super(itemView);
            checkBox = (CheckBox)itemView.findViewById(R.id.cb_checklist);
            name = (TextView)itemView.findViewById(R.id.tv_name);
            itemView.setOnClickListener(this);
            this.context = context;
        }

        /**
         * Called when click on Card checklist
         * @param v
         */
        @Override
        public void onClick(View v) {
            MainActivity.startActivity(context, ChecklistActivity.class);
        }
    }
}
