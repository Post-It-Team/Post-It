package com.sunbook.parrot.checklist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.sunbook.parrot.parrot.Checklist;
import com.sunbook.parrot.utils.StringUtil;

import java.util.ArrayList;

/**
 * Created by hieuapp on 17/02/2016.
 */
public class CheckListAdapter extends ArrayAdapter<Checklist> {
    ArrayList<Checklist> dataCheckList = null;
    Activity context;
    public static final int MAX_ITEM_CHECKLIST = 5;
    CheckListDB checkListDB;

    public static final String TAG = "ChecklistAdapter";
    public CheckListAdapter(Activity context, int resource, ArrayList<Checklist> data, CheckListDB checkListDB) {
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

    public void showCheckList(ViewHolder viewHolder, final ArrayList<Checklist> dataCheckList, final int position){
        viewHolder.name.setText(dataCheckList.get(position).getTitle());
        Typeface regular = StringUtil.getTypeface(context,StringUtil.ROBOTO_SLAB_REGULAR);
        viewHolder.name.setTypeface(regular);
        final ViewHolder finalViewHolder = viewHolder;
        long deadline = dataCheckList.get(position).getDeadline();
        if(deadline != 0){
            String date = StringUtil.dateFomat(deadline);
            viewHolder.deadline.setText(date);
        }else {
            viewHolder.deadline.setText("");
        }
        if(dataCheckList.get(position).isDone()){
            viewHolder.checkBox.setChecked(true);
            viewHolder.name.setPaintFlags(viewHolder.name.getPaintFlags()  | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    finalViewHolder.name.setPaintFlags(finalViewHolder.name.getPaintFlags()
                            | Paint.STRIKE_THRU_TEXT_FLAG);
                    setDoneChecklist(position,true);
                }else {
                    // un trike text
                    finalViewHolder.name.setPaintFlags(finalViewHolder.name.getPaintFlags()
                            & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    setDoneChecklist(position,false);
                }
            }
        });
    }

    public void setDoneChecklist(int position, boolean done){
        Checklist checklist = dataCheckList.get(position);
        checklist.setIsDone(done);
        checkListDB.daoAccess.setContentValues(checklist);
        ContentValues updateChecklist = checkListDB.daoAccess.getContentValues();
        checkListDB.daoAccess.update(ChecklistSchema.CHECKLIST_TABLE,updateChecklist,
                "_id = "+checklist.getId(),null);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox checkBox;
        TextView name, deadline;
        Context context;

        public ViewHolder(Context context,View itemView) {
            super(itemView);
            checkBox = (CheckBox)itemView.findViewById(R.id.cb_checklist);
            name = (TextView)itemView.findViewById(R.id.tv_name);
            deadline = (TextView)itemView.findViewById(R.id.tv_deadline);
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
