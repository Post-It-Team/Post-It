package com.sunbook.parrot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.sunbook.parrot.calendar.CalendarActivity;
import com.sunbook.parrot.checklist.CheckListAdapter;
import com.sunbook.parrot.checklist.DialogReminder;
import com.sunbook.parrot.database.checklist.CheckListDB;
import com.sunbook.parrot.postit.Reminder;
import com.sunbook.parrot.utils.PostItDate;
import com.sunbook.parrot.utils.PostItUI;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        FloatingActionsMenu.OnFloatingActionsMenuUpdateListener {

    public static final String TAG = "MainActivity";
    public static FloatingActionsMenu fabMenu;
    FloatingActionButton fabNote, fabCamera;
    android.support.design.widget.FloatingActionButton fabReminder;
    FrameLayout frameLayout;
    public static final int HOUR_OF_DAY = 24;
    public static final int MINUTE = 00;
    private TextView tvCardTitle;
    private boolean starImportant = false;
    private RelativeLayout report;
    private CardView cardChecklist;
    public CheckListDB checkListDB;
    private ChecklistChange checklistReceiver;

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        setupViewpager(viewPager);
        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.drawable.tab_selected));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));

        viewPager.addOnPageChangeListener( new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                animateFab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        frameLayout.getBackground().setAlpha(0);
        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        fabMenu.setOnFloatingActionsMenuUpdateListener(this);

        fabNote = (FloatingActionButton)findViewById(R.id.fab_note);
        fabNote.setOnClickListener(this);
        fabCamera = (FloatingActionButton)findViewById(R.id.fab_camera);
        fabCamera.setOnClickListener(this);
        fabReminder = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fab_reminder);
        fabReminder.setOnClickListener(this);
    }

    public void animateFab(int position){
        switch (position){
            case 0:
                fabReminder.hide();
                fabMenu.setVisibility(View.VISIBLE);
                break;
            case 1:
                fabReminder.show();
                fabMenu.setVisibility(View.INVISIBLE);
                break;
            default:
                fabReminder.hide();
                fabMenu.setVisibility(View.VISIBLE);
                break;

        }
    }

    private void setupViewpager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CardNoteFragment(), "Note");
        adapter.addFragment(new ReminderFragment(), "Reminder");
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
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
    }
    /**
     * Load 5 checklist in database
     */
    public void loadCheckList(){
        ArrayList<Reminder> listTask = (ArrayList<Reminder>) checkListDB
                .daoAccess.getTaskMostImportant();
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
            TextView nextAlarm = (TextView)findViewById(R.id.tv_next_alarm);
            displayTimeRemind(nextAlarm,listTask.get(0));
        }
    }

    public static void displayTimeRemind(TextView nextAlarm, Reminder nextReminder){
        long time = nextReminder.getTime();
        long date = nextReminder.getDeadline();
        String dateRemind = PostItDate.convertToVietnam(date);
        if(dateRemind.equals(PostItDate.TODAY)){
            String hour = PostItDate.formatHour(time);
            nextAlarm.setText(hour);
            if(!hour.equals(PostItDate.NO_TIME)){
                nextAlarm.setText("");
                nextAlarm.setBackgroundColor(Color.WHITE);
            }
        }else if(dateRemind.equals(PostItDate.NO_DATE)){
            nextAlarm.setText("");
        }else {
            nextAlarm.setText(dateRemind);
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
            case R.id.fab_camera:
                Toast.makeText(this,"Camera",Toast.LENGTH_LONG).show();
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

    class ViewPagerAdapter extends FragmentPagerAdapter{
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitle = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragmentList.add(fragment);
            fragmentTitle.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }
}
