package com.sunbook.parrot;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sunbook.parrot.checklist.CheckListAdapter;
import com.sunbook.parrot.database.checklist.CheckListDB;
import com.sunbook.parrot.postit.Reminder;

import java.util.ArrayList;

/**
 * Created by hieuapp on 12/07/2016.
 */
public class ReminderFragment extends Fragment {

    ListView lvReminder;
    ArrayList<Reminder> listTask;
    CheckListDB checkListDB;
    private static Activity mContext;
    private ChecklistChange checklistReceiver = null;
    CheckListAdapter adapter;

    public static ReminderFragment newInstance(Activity context) {
        Bundle args = new Bundle();
        ReminderFragment fragment = new ReminderFragment();
        fragment.setArguments(args);
        mContext = context;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        IntentFilter filter = new IntentFilter(CheckListDB.ACTION_UPDATE_REMINDER);
        if(checklistReceiver == null){
            checklistReceiver = new ChecklistChange();
            mContext.registerReceiver(checklistReceiver, filter);
        }
        return inflater.inflate(R.layout.reminder_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        listTask = (ArrayList<Reminder>) checkListDB.daoAccess.getAllCheckList();
        lvReminder = (ListView)view.findViewById(R.id.lv_reminder);
        adapter = new CheckListAdapter(mContext, R.layout.checklist_item_detail, listTask);
        lvReminder.setAdapter(adapter);
    }

    class ChecklistChange extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            openDB(context);
            switch (action) {
                case CheckListDB.ACTION_UPDATE_REMINDER:
                    listTask = (ArrayList<Reminder>) checkListDB.daoAccess.getAllCheckList();
                    refresh(listTask);
                    break;
                default:
                    break;
            }
        }

        public void openDB(Context context) {
            if (checkListDB == null) {
                checkListDB = new CheckListDB(context);
                checkListDB.open();
            }
        }
    }

    public void refresh(ArrayList<Reminder> reminders){
        this.listTask = reminders;
        adapter.notifyDataSetChanged();
    }

    public void onDestroy (){
        super.onDestroy();
        if(checklistReceiver != null){
            mContext.unregisterReceiver(checklistReceiver);
        }
    }
}
