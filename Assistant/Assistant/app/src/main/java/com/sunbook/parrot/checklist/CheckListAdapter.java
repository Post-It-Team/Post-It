package com.sunbook.parrot.checklist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
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
import com.sunbook.parrot.parrot.Checklist;
import com.sunbook.parrot.utils.StringUtil;

import java.util.ArrayList;

/**
 * Created by hieuapp on 17/02/2016.
 */
public class CheckListAdapter extends ArrayAdapter<Checklist> {
    ArrayList<Checklist> dataCheckList = null;
    Activity context;

    public static final String TAG = "ChecklistAdapter";
    public CheckListAdapter(Activity context, int resource, ArrayList<Checklist> data) {
        super(context, resource, data);
        this.dataCheckList = data;
        this.context = context;
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

        /*if task is not done then show*/
        if(!dataCheckList.get(position).isDone()){
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

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((CheckBox)v).isChecked()){
                        finalViewHolder.name.setPaintFlags(finalViewHolder.name.getPaintFlags()
                                | Paint.STRIKE_THRU_TEXT_FLAG);
                    }else {
                        // un trike text
                        finalViewHolder.name.setPaintFlags(finalViewHolder.name.getPaintFlags()
                                & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    }
                }
            });
        }

        return convertView;
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
