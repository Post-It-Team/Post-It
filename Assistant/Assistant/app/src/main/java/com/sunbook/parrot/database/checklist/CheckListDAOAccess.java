package com.sunbook.parrot.database.checklist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import com.sunbook.parrot.database.DBContentProvider;
import com.sunbook.parrot.postit.Reminder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hieuapp on 04/04/2016.
 */
public class CheckListDAOAccess extends DBContentProvider implements CheckListDAOInt,ChecklistSchema {
    private Cursor cursor;
    private ContentValues contentValues;

    public CheckListDAOAccess(SQLiteDatabase sqLiteDatabase) {
        super(sqLiteDatabase);
    }

    @Override
    public CheckListDAOInt getCheckListById(int checkListId) {
        return null;
    }

    @Override
    public List<Reminder> getAllCheckList() {
        List<Reminder> reminders = new ArrayList<Reminder>();
        cursor = super.query(CHECKLIST_TABLE,CHECKLIST_COLUMNS,null,null,COLUMN_DEADLINE+" ASC");
        if(cursor == null){
            return null;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Reminder task = cursorToEntity(cursor);
            reminders.add(task);
            cursor.moveToNext();
        }
        cursor.close();
        return reminders;
    }

    @Override
    public boolean addCheckList(Reminder reminder) {
        setContentValues(reminder);
        try{
            return super.insert(CHECKLIST_TABLE,getContentValues()) > 0;
        }catch (SQLiteConstraintException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addChecklists(List<Reminder> lists) {
        return false;
    }

    @Override
    public boolean deleteAllCheckList() {
        return false;
    }

    @Override
    public boolean deleteReminder(String id) {
        String where = ChecklistSchema._ID + " = ?";
        return super.delete(CHECKLIST_TABLE,where,new String[]{id}) > 0;
    }

    @Override
    protected Reminder cursorToEntity(Cursor cursor) {
        Reminder task = new Reminder();
        if(cursor == null){
            return null;
        }
        int id = cursor.getInt(cursor.getColumnIndex(_ID));
        task.setId(id);
        String taskTitle = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
        task.setTitle(taskTitle);
        long deadline = cursor.getLong(cursor.getColumnIndex(COLUMN_DEADLINE));
        task.setDeadline(deadline);
        int done = cursor.getInt(cursor.getColumnIndex(COLUMN_DONE));
        boolean isDone = (done == 1);
        task.setIsDone(isDone);
        int important = cursor.getInt(cursor.getColumnIndex(COLUMN_IMPORTANT));
        boolean isImportant = (important == 1);
        task.setImportant(isImportant);
        return task;
    }

    public ContentValues getContentValues() {
        return contentValues;
    }

    public void setContentValues(Reminder task) {
        this.contentValues = new ContentValues();
//        contentValues.put(_ID,task.getId());
        contentValues.put(COLUMN_TITLE,task.getTitle());
        contentValues.put(COLUMN_DEADLINE,task.getDeadline());
        int important = (task.isImportant())?1:0;
        contentValues.put(COLUMN_IMPORTANT,important);
        if(task.isDone()){
            contentValues.put(COLUMN_DONE,1);
        }else {
            contentValues.put(COLUMN_DONE,0);
        }
    }
}
