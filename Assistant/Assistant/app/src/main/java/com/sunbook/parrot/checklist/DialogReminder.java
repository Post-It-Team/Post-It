package com.sunbook.parrot.checklist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sunbook.parrot.MainActivity;
import com.sunbook.parrot.R;
import com.sunbook.parrot.database.checklist.CheckListDB;
import com.sunbook.parrot.material.DatePickerDialog;
import com.sunbook.parrot.material.DialogFragment;
import com.sunbook.parrot.material.TimePickerDialog;
import com.sunbook.parrot.material.dialog.Dialog;
import com.sunbook.parrot.postit.Reminder;
import com.sunbook.parrot.utils.PostItUI;

import java.text.SimpleDateFormat;

/**
 * Created by hieuapp on 21/06/2016.
 */
public class DialogReminder implements View.OnClickListener {
    private View positiveAction;
    private EditText remindInput;
    private TextView tvDate, tvTime;
    private CheckListDB checkListDB;
    private Context context;
    private ImageView star;
    private long dateSelected = 0;
    private long timeSelected = 0;
    private boolean starImportant = false;
    private Reminder task = null;

    public DialogReminder(Context context){
        this.context = context;
        checkListDB = new CheckListDB(context);
    }

    public DialogReminder(Context context, Reminder taskEdit){
        this.task = taskEdit;
        this.context = context;
        this.checkListDB = new CheckListDB(context);
    }
    public void show(){
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(R.string.check_list)
                .customView(R.layout.layout_dialog_add_checklist, true)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        checkListDB.open();
                        Reminder reminder = new Reminder(remindInput.getText().toString(),
                                dateSelected,timeSelected,false, starImportant);
                        insertToDatabase(reminder);
                        Intent intent = new Intent(CheckListDB.ACTION_UPDATE_REMINDER);
                        context.sendBroadcast(intent);
                    }
                }).build();

        tvDate = (TextView)dialog.getCustomView().findViewById(R.id.tv_date);
        tvDate.setPaintFlags(tvDate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvDate.setOnClickListener(this);
        tvTime = (TextView)dialog.getCustomView().findViewById(R.id.tv_time);
        tvTime.setPaintFlags(tvTime.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvTime.setOnClickListener(this);
        if(task != null){
            setContentDialog(task);
        }
        star = (ImageView)dialog.findViewById(R.id.im_star_dialog);
        star.setOnClickListener(this);
        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        controlInputRemind(dialog);
        dialog.show();
        positiveAction.setEnabled(false);
    }

    public void showTimePickerDialog(){
        Dialog.Builder builder = new TimePickerDialog.Builder(
                R.style.Material_App_Dialog_TimePicker_Light,
                MainActivity.HOUR_OF_DAY,MainActivity.MINUTE){
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
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragment.show(fragmentManager, null);
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
                Log.e("Date = ",""+dateSelected);
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
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragment.show(fragmentManager, null);
    }

    public void setContentDialog(Reminder task){
        remindInput.setText(task.getTitle());
        String date = PostItUI.dateFomat(task.getDeadline());
        tvDate.setText(date);
        tvTime.setText("12h:00");
        starImportant = task.isImportant();
        toggleStar();
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.im_star_dialog:
                        starImportant = !starImportant;
                        toggleStar();
                        break;
                }
            }
        });
    }

    public void toggleStar(){
        if(starImportant){
            star.setImageResource(R.mipmap.ic_star_yellow);
        }else {
            star.setImageResource(R.mipmap.ic_star_border_48dp);
        }
    }

    public void insertToDatabase(Reminder object){
        checkListDB.daoAccess.addCheckList(object);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_date:
                showDatePickerDialog();
                break;
            case R.id.tv_time:
                showTimePickerDialog();
                break;
            case R.id.im_star_dialog:
                starImportant = !starImportant;
                toggleStar();
                break;
            default:
                break;
        }
    }
}
