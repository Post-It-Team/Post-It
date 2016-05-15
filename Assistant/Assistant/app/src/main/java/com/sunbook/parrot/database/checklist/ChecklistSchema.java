package com.sunbook.parrot.database.checklist;

import android.provider.BaseColumns;

/**
 * Created by hieuapp on 04/04/2016.
 */
public interface ChecklistSchema extends BaseColumns {
    String CHECKLIST_TABLE = "checklist";
    String COLUMN_TITLE = "title";
    String COLUMN_DEADLINE = "deadline";
    String COLUMN_DONE = "done";
    String COLUMN_IMPORTANT = "important";

    String CREATE_TABLE_CHECKLIST = "CREATE TABLE IF NOT EXISTS "
            + CHECKLIST_TABLE
            + " ("
            + BaseColumns._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TITLE
            + " TEXT NOT NULL, "
            + COLUMN_DEADLINE
            + " INTEGER NOT NULL, "
            + COLUMN_DONE
            + " INTEGER NOT NULL, "
            + COLUMN_IMPORTANT
            + " INTEGER NOT NULL "
            + ")";

    String[] CHECKLIST_COLUMNS = new String[]{BaseColumns._ID,
            COLUMN_TITLE,COLUMN_DEADLINE,COLUMN_DONE,COLUMN_IMPORTANT};
}