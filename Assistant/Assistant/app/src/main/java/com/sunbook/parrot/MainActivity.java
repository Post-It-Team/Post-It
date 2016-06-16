package com.sunbook.parrot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.sunbook.parrot.calendar.CalendarActivity;
import com.sunbook.parrot.checklist.CheckListAdapter;
import com.sunbook.parrot.database.checklist.CheckListDB;
import com.sunbook.parrot.material.DatePickerDialog;
import com.sunbook.parrot.material.DialogFragment;
import com.sunbook.parrot.material.TimePickerDialog;
import com.sunbook.parrot.material.dialog.Dialog;
import com.sunbook.parrot.parrot.Checklist;
import com.sunbook.parrot.utils.StringUtil;

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
    private RelativeLayout report;
    private CardView cardChecklist;
    public CheckListDB checkListDB;
    private long dateSelected = 0;
    private long timeSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvCardTitle = (TextView)findViewById(R.id.tv_title_card);
        Typeface light = Typeface.createFromAsset(getAssets(), StringUtil.ROBOTO_SLAB_REGULAR);
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
    }
    /**
     * Load 5 checklist in database
     */
    public void loadCheckList(){
        ArrayList<Checklist> listTask = (ArrayList<Checklist>) checkListDB.daoAccess.getAllCheckList();
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
                addCheckList();
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

    private void addCheckList(){
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.check_list)
                .customView(R.layout.layout_dialog_add_checklist, true)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Toast.makeText(MainActivity.this, remindInput.getText(), Toast.LENGTH_SHORT).show();
                        Checklist reminder = new Checklist(remindInput.getText().toString(),dateSelected,false);
                        insertToDatabase(reminder);
                    }
                }).build();

        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        controlInputRemind(dialog);
        setDateTimeRemind(dialog);
        dialog.show();
        fabMenu.collapseImmediately();
        positiveAction.setEnabled(false);
    }

    public void setDateTimeRemind(MaterialDialog dialog){
        tvDate = (TextView)dialog.getCustomView().findViewById(R.id.tv_date);
        tvDate.setPaintFlags(tvDate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvTime = (TextView)dialog.getCustomView().findViewById(R.id.tv_time);
        tvTime.setPaintFlags(tvTime.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public void controlInputRemind(MaterialDialog dialog){
        remindInput = (EditText)dialog.getCustomView().findViewById(R.id.et_add_checklist);
        remindInput.setFocusable(true);
        remindInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                positiveAction.setEnabled(s.toString().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void insertToDatabase(Checklist object){
        checkListDB.daoAccess.addCheckList(object);
        loadCheckList();
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
}
