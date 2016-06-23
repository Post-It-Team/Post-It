package com.sunbook.parrot.checklist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.sunbook.parrot.MainActivity;
import com.sunbook.parrot.R;
import com.sunbook.parrot.database.checklist.CheckListDB;
import com.sunbook.parrot.postit.Reminder;
import com.sunbook.parrot.utils.PostItUI;

import java.util.ArrayList;


/**
 * Created by hieuapp on 15/06/2016.
 */
public class ListTaskAdapter extends BaseSwipeAdapter {
    private Context context;
    private ArrayList<Reminder> reminders;
    public ListTaskAdapter(Context context, ArrayList<Reminder> reminders){
        this.context = context;
        this.reminders = reminders;
    }
    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.checklist_item_detail, null);
        SwipeLayout swipeLayout = (SwipeLayout)view.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener(){
            @Override
            public void onOpen(SwipeLayout layout) {

            }
        });
        view.findViewById(R.id.layout_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDel = new Intent(CheckListDB.ACTION_DELETE_REMINDER);
                String idReminder = String.valueOf(getItemId(position));
                intentDel.putExtra(CheckListDB.KEY_ID_REMINDER,idReminder);
                intentDel.putExtra("position",position);
                context.sendBroadcast(intentDel);
            }
        });
        return view;
    }

    @Override
    public void fillValues(int position, final View convertView) {
        final Reminder task = (Reminder)getItem(position);
        final ImageView star = (ImageView)convertView.findViewById(R.id.im_star);
        final TextView taskName = (TextView)convertView.findViewById(R.id.tv_reminder);
        final TextView date = (TextView)convertView.findViewById(R.id.tv_date);
        MainActivity.displayTimeRemind(date,task);
        //star yellow if task is not done
        if(task.isDone()){
           PostItUI.setTaskHiden(context,convertView);
        }else {
            PostItUI.setTaskDisplay(context,convertView,task);
        }
        if(!task.isImportant()){
            star.setImageResource(R.mipmap.ic_star_border_48dp);
        }
        //hiden task name if task is done
        taskName.setText(task.getTitle());
        final CheckBox cbDone = (CheckBox)convertView.findViewById(R.id.cb_item);
        cbDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbDone.isChecked()){
                    PostItUI.setTaskHiden(context,convertView);
                    if(!task.isImportant()){
                        star.setImageResource(R.mipmap.ic_star_border_48dp);
                    }
                }else {
                    PostItUI.setTaskDisplay(context,convertView,task);
                    if(!task.isImportant()){
                        star.setImageResource(R.mipmap.ic_star_border_48dp);
                    }
                }
            }
        });

        cbDone.setChecked(task.isDone());

    }


    @Override
    public int getCount() {
        return reminders.size();
    }

    @Override
    public Object getItem(int position) {
        return reminders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return reminders.get(position).getId();
    }
}
