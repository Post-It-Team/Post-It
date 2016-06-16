package com.sunbook.parrot.checklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.sunbook.parrot.R;
import com.sunbook.parrot.parrot.Checklist;

import java.util.ArrayList;


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

                Toast.makeText(context,"delete "+position,Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void fillValues(int position, View convertView) {
        Checklist task = (Checklist)getItem(position);
        TextView taskName = (TextView)convertView.findViewById(R.id.tv_reminder);
        ImageView star = (ImageView)convertView.findViewById(R.id.im_star);
        CheckBox cbDone = (CheckBox)convertView.findViewById(R.id.cb_item);
        taskName.setText(task.getTitle());
        cbDone.setChecked(task.isDone());
        if(task.isImportant()){
            star.setImageResource(R.mipmap.ic_star_yellow);
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
        return position;
    }
}
