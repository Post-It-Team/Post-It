package com.sunbook.parrot;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sunbook.parrot.calendar.CalendarActivity;
import com.sunbook.parrot.camera.CameraActivity;
import com.sunbook.parrot.checklist.DialogReminder;
import com.sunbook.parrot.database.checklist.CheckListDB;
import com.sunbook.parrot.postit.Reminder;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "MainActivity";
    FloatingActionButton fabReminder, fabCamera;
    private CoordinatorLayout coordinatorLayout;
    public static final int HOUR_OF_DAY = 24;
    public static final int MINUTE = 00;
    public static ArrayList<Reminder> listTask;
    public CheckListDB checkListDB;

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

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator);
        fabReminder = (FloatingActionButton) findViewById(R.id.fab_reminder);
        fabReminder.setOnClickListener(this);
        fabCamera = (FloatingActionButton)findViewById(R.id.fab_camera);
        fabCamera.setOnClickListener(this);
        checkListDB = new CheckListDB(this);
        checkListDB.open();
        listTask = (ArrayList<Reminder>) checkListDB.daoAccess.getAllCheckList();
    }

    public void animateFab(int position){
        switch (position){
            case 0:
                fabReminder.hide();
                fabCamera.show();
                break;
            case 1:
                fabCamera.hide();
                fabReminder.show();
                break;
            default:
                fabReminder.hide();
                fabCamera.show();
                break;

        }
    }

    private void setupViewpager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CardNoteFragment(), "Note");
        adapter.addFragment(ReminderFragment.newInstance(this), "Reminder");
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
        checkListDB.close();
    }

    /**
     * When click on fab button, onClick has called
     * @param v fab button
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_camera:
                if(hasCamera(this)){
                    Intent intent = new Intent(this, CameraActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "Camera is unavailable", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.fab_reminder:
                DialogReminder reminder = new DialogReminder(this);
                reminder.show();
                break;
            default:
                Log.e(TAG,"ID not define" + v.getId());
        }
    }

    private boolean hasCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
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
