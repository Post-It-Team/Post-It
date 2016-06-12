package com.sunbook.parrot.checklist;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.sunbook.parrot.R;

/**
 * Created by hieuapp on 23/03/2016.
 */
public class ChecklistActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setElevation(4);
            actionBar.setBackgroundDrawable(
                    new ColorDrawable(getResources().getColor(R.color.yellow_200)));
        }


    }
}
