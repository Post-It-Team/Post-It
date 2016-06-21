package com.sunbook.parrot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.sunbook.parrot.calendar.CalendarActivity;
import com.sunbook.parrot.checklist.CheckListAdapter;
import com.sunbook.parrot.checklist.DialogReminder;
import com.sunbook.parrot.database.checklist.CheckListDB;
import com.sunbook.parrot.material.DatePickerDialog;
import com.sunbook.parrot.material.DialogFragment;
import com.sunbook.parrot.material.TimePickerDialog;
import com.sunbook.parrot.material.dialog.Dialog;
import com.sunbook.parrot.postit.Reminder;
import com.sunbook.parrot.utils.PostItUI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        FloatingActionsMenu.OnFloatingActionsMenuUpdateListener {

    public static final String TAG = "MainActivity";
    public static FloatingActionsMenu fabMenu;
    FrameLayout frameLayout;

    public static final int HOUR_OF_DAY = 24;
    public static final int MINUTE = 00;

    private View positiveAction;
    private EditText remindInput;
    private TextView tvDate, tvTime;
    private TextView tvCardTitle;
    private ImageView star;
    private boolean starImportant = false;
    private RelativeLayout report;
    private CardView cardChecklist;
    public CheckListDB checkListDB;
    private long dateSelected = 0;
    private long timeSelected = 0;
    private ChecklistChange checklistReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvCardTitle = (TextView)findViewById(R.id.tv_title_card);
        Typeface light = Typeface.createFromAsset(getAssets(), PostItUI.ROBOTO_SLAB_REGULAR);
        tvCardTitle.setTextColor(Color.BLACK);
        tvCardTitle.setTypeface(light);
        report = (RelativeLayout)findViewById(R.id.report_empty);
        cardChecklist = (CardView)findViewById(R.id.cv_checklist);

        frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        frameLayout.getBackground().setAlpha(0);
        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        fabMenu.setOnFloatingActionsMenuUpdateListener(this);

        FloatingActionButton fabNote = (FloatingActionButton)findViewById(R.id.fab_note);
        fabNote.setOnClickListener(this);
        FloatingActionButton fabChecklist = (FloatingActionButton)findViewById(R.id.fab_checklist);
        fabChecklist.setOnClickListener(this);
        FloatingActionButton fabCamera = (FloatingActionButton)findViewById(R.id.fab_camera);
        fabCamera.setOnClickListener(this);
        checklistReceiver = new ChecklistChange();
        IntentFilter filter = new IntentFilter(CheckListDB.ACTION_UPDATE_REMINDER);
        registerReceiver(checklistReceiver,filter);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkListDB = new CheckListDB(this);
        checkListDB.open();
        loadCheckList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            case R.id.action_search:
                Toast.makeText(this,"Search tool",Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_calendar:
                Intent intent = new Intent(this, CalendarActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        checkListDB.close();
        if(checklistReceiver != null){
            unregisterReceiver(checklistReceiver);
        }
    }
    /**
     * Load 5 checklist in database
     */
    public void loadCheckList(){
        ArrayList<Reminder> listTask = (ArrayList<Reminder>) checkListDB.daoAccess.getAllCheckList();
        if(listTask.size() == 0){
            report.setVisibility(View.VISIBLE);
            cardChecklist.setVisibility(View.INVISIBLE);
        }else {
            CheckListAdapter checkListAdapter = new CheckListAdapter(MainActivity.this,
                    R.layout.checklist_item, listTask, checkListDB);
            ListView lvCheckList = (ListView)findViewById(R.id.lv_checklist);
            lvCheckList.setAdapter(checkListAdapter);
            report.setVisibility(View.INVISIBLE);
            cardChecklist.setVisibility(View.VISIBLE);
        }
    }
    /**
     * When click on fab button, onClick has called
     * @param v fab button
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_note:
                Toast.makeText(this,"Note",Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_checklist:
                DialogReminder dialogReminder = new DialogReminder(this);
                dialogReminder.show();
                fabMenu.collapseImmediately();
                break;
            case R.id.fab_camera:
                Toast.makeText(this,"Camera",Toast.LENGTH_LONG).show();
                break;
            case R.id.tv_date:
                showDatePickerDialog();
                break;
            case R.id.tv_time:
                showTimePickerDialog();
                break;
            case R.id.im_star_dialog:
                starImportant = !starImportant;
                Log.e("Star = ",String.valueOf(starImportant));
                toggleStar();
                break;
            default:
                Log.e(TAG,"ID not define" + v.getId());
        }
    }

    public static void startActivity(Context context, Class mClass){
        Intent intent = new Intent(context, mClass);
        context.startActivity(intent);
        fabMenu.collapseImmediately();
    }

    /**
     *Called when fab button click on.
     */
    @Override
    public void onMenuExpanded() {
        frameLayout.getBackground().setAlpha(240);
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                fabMenu.collapse();
                return true;
            }
        });
    }

    /**
     * Fab button close.
     */
    @Override
    public void onMenuCollapsed() {
        frameLayout.getBackground().setAlpha(0);
        frameLayout.setOnTouchListener(null);
    }

    public void toggleStar(){
        if(starImportant){
            star.setImageResource(R.mipmap.ic_star_yellow);
        }else {
            star.setImageResource(R.mipmap.ic_star_border_48dp);
        }
    }

    public void showDatePickerDialog(){
        Dialog.Builder builder = new DatePickerDialog.Builder(
                R.style.Material_App_Dialog_DatePicker_Light){
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                DatePickerDialog dialog = (DatePickerDialog)fragment.getDialog();
                String date = dialog.getFormattedDate(SimpleDateFormat.getDateInstance());
                tvDate.setText(date);
                dateSelected = dialog.getDate();
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };

        builder.positiveAction("OK")
                .negativeAction("CANCEL");
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    public void showTimePickerDialog(){
        Dialog.Builder builder = new TimePickerDialog.Builder(
                R.style.Material_App_Dialog_TimePicker_Light,HOUR_OF_DAY, MINUTE){
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                TimePickerDialog dialog = (TimePickerDialog)fragment.getDialog();
                timeSelected = dialog.getHour()*60 + dialog.getMinute();
                tvTime.setText(dialog.getHour()+":"+dialog.getMinute());
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };

        builder.positiveAction("OK").negativeAction("CANCEL");
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    class ChecklistChange extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            openDB(context);
            switch (action) {
                case CheckListDB.ACTION_UPDATE_REMINDER:
                    loadCheckList();
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
}
