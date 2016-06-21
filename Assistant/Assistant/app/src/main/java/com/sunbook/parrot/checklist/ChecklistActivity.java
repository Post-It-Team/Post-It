package com.sunbook.parrot.checklist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.sunbook.parrot.R;
import com.sunbook.parrot.database.checklist.CheckListDB;
import com.sunbook.parrot.postit.Reminder;

import java.util.ArrayList;

/**
 * Created by hieuapp on 23/03/2016.
 */
public class ChecklistActivity extends AppCompatActivity {
    private ListTaskAdapter listTaskAdapter;
    private ListView lvTask;
    private CheckListDB checkListDB;
    private ArrayList<Reminder> listTask;
    private DBUpdateReceiver updateReceiver;
    private EditText remindInput;
    private long dateSelected = 0;
    private ImageView star;
    private boolean starImportant = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        updateReceiver = new DBUpdateReceiver();
        IntentFilter filter = new IntentFilter(CheckListDB.ACTION_DELETE_REMINDER);
        filter.addAction(CheckListDB.ACTION_UPDATE_REMINDER);
        registerReceiver(updateReceiver,filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_checklist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
            case R.id.action_add:
                DialogReminder dialog = new DialogReminder(ChecklistActivity.this);
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void generateReminder(){
        listTask = (ArrayList<Reminder>) checkListDB.daoAccess.getAllCheckList();
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
    public void onResume() {
        super.onResume();
        checkListDB = new CheckListDB(this);
        checkListDB.open();
        generateReminder();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        checkListDB.close();
        if(updateReceiver != null){
            unregisterReceiver(updateReceiver);
        }
    }

    class DBUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            openDB(context);
            switch (action){
                case CheckListDB.ACTION_UPDATE_REMINDER:
                    generateReminder();
                    break;
                case CheckListDB.ACTION_DELETE_REMINDER:
                    String id = intent.getStringExtra(CheckListDB.KEY_ID_REMINDER);
                    int position = intent.getIntExtra("position",-1);
                    boolean res = checkListDB.daoAccess.deleteReminder(id);
                    if(!res){
                        Toast.makeText(context,
                                context.getString(R.string.delete_reminder_fail),
                                Toast.LENGTH_SHORT).show();
                        break;
                    }
                    listTask.remove(position);
                    listTaskAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }

        public void openDB(Context context){
            if(checkListDB == null){
                checkListDB = new CheckListDB(context);
                checkListDB.open();
            }
        }
    }

}
