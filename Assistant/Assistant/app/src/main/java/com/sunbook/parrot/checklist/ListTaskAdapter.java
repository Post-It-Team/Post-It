package com.sunbook.parrot.checklist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.sunbook.parrot.R;
import com.sunbook.parrot.database.checklist.CheckListDB;
import com.sunbook.parrot.postit.Checklist;
import com.sunbook.parrot.utils.StringUtil;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by hieuapp on 15/06/2016.
 */
public class ListTaskAdapter extends BaseSwipeAdapter {
    private Context context;
    private ArrayList<Checklist> checklists;
    public ListTaskAdapter(Context context, ArrayList<Checklist> checklists){
        this.context = context;
        this.checklists = checklists;
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
    public void fillValues(int position, View convertView) {
        final Checklist task = (Checklist)getItem(position);
        setBackgroundTask(task,convertView);
        //star yellow if task is not done
        final ImageView star = (ImageView)convertView.findViewById(R.id.im_star);
        if(task.isImportant() && !task.isDone()){
            star.setImageResource(R.mipmap.ic_star_yellow);
        }
        //hiden task name if task is done
        final TextView taskName = (TextView)convertView.findViewById(R.id.tv_reminder);
        taskName.setText(task.getTitle());
        StringUtil.setTextFont(context,taskName,StringUtil.ROBOTO_SLAB_REGULAR);
        if(task.isDone()){
            StringUtil.setTaskHiden(taskName);
            star.setImageResource(R.mipmap.ic_start_hiden);
        }
        final TextView date = (TextView)convertView.findViewById(R.id.tv_date);
        StringUtil.setTextFont(context,date,StringUtil.ROBOTO_SLAB_REGULAR);
        final CheckBox cbDone = (CheckBox)convertView.findViewById(R.id.cb_item);
        cbDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbDone.isChecked()){
                    StringUtil.setTaskHiden(star,taskName,date);
                }else {
                    StringUtil.setTaskDisplay(taskName,date);
                    if(task.isImportant()){
                        star.setImageResource(R.mipmap.ic_star_yellow);
                    }
                }
            }
        });

        cbDone.setChecked(task.isDone());

    }

    public void setBackgroundTask(Checklist task, View convertView){
        RelativeLayout taskLayout = (RelativeLayout)convertView.findViewById(R.id.task_layout);
        int deadlineStatus = task.checkDeadline();
        switch (deadlineStatus){
            case Checklist.IN_DATE:
                taskLayout.setBackgroundColor(Color.WHITE);
                break;
            case Checklist.NEAR_DEADLINE:
                int bgColor = context.getResources().getColor(R.color.yellow_200);
                taskLayout.setBackgroundColor(bgColor);
                break;
            case Checklist.OUT_OF_DATE:
                int bgRed = context.getResources().getColor(R.color.red);
                taskLayout.setBackgroundColor(bgRed);
                break;
            default:
                taskLayout.setBackgroundColor(Color.WHITE);
                break;
        }
    }


    @Override
    public int getCount() {
        return checklists.size();
    }

    @Override
    public Object getItem(int position) {
        return checklists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return checklists.get(position).getId();
    }
}
