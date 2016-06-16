package com.sunbook.parrot.checklist;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.daimajia.swipe.SwipeLayout;
import com.sunbook.parrot.R;
import com.sunbook.parrot.database.checklist.CheckListDB;
import com.sunbook.parrot.parrot.Checklist;

import java.util.ArrayList;

/**
 * Created by hieuapp on 23/03/2016.
 */
public class ChecklistActivity extends AppCompatActivity {
    private ListTaskAdapter listTaskAdapter;
    private ListView lvTask;
    private CheckListDB checkListDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        checkListDB = new CheckListDB(this);
        checkListDB.open();
        ArrayList<Checklist> listTask = (ArrayList<Checklist>) checkListDB.daoAccess.getAllCheckList();
        lvTask = (ListView)findViewById(R.id.lv_reminder);
        listTaskAdapter = new ListTaskAdapter(this, listTask);
        lvTask.setAdapter(listTaskAdapter);
        lvTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SwipeLayout)(lvTask.getChildAt(position - lvTask.getFirstVisiblePosition()))).open(true);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        checkListDB.close();
    }
}
