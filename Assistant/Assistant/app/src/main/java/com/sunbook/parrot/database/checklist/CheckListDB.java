package com.sunbook.parrot.database.checklist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hieuapp on 04/04/2016.
 */
public class CheckListDB {

    private static final String DB_NAME = "checklist.db";
    private DatabaseHelper mDBHelper;
    private static final int DB_VERSION = 1;
    private final Context mContext;
    public static CheckListDAOAccess daoAccess;

    public CheckListDB(Context mContext) {
        this.mContext = mContext;
    }

    public CheckListDB open(){
        mDBHelper = new DatabaseHelper(mContext);
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        daoAccess = new CheckListDAOAccess(database);
        return this;
    }

    public void close() {
        mDBHelper.close();
    }

    private static class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(ChecklistSchema.CREATE_TABLE_CHECKLIST);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + ChecklistSchema.CHECKLIST_TABLE);
            onCreate(db);
        }
    }
}
