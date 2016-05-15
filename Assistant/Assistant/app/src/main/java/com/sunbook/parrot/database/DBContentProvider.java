package com.sunbook.parrot.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sunbook.parrot.parrot.Checklist;

/**
 * Created by hieuapp on 04/04/2016.
 */
public abstract class DBContentProvider {
    public SQLiteDatabase mDb;

    public DBContentProvider(SQLiteDatabase sqLiteDatabase){
        this.mDb = sqLiteDatabase;
    }

    protected abstract Checklist cursorToEntity(Cursor cursor);

    public int delete(String tableName, String selection, String[] selectionArgs){
        return mDb.delete(tableName,selection,selectionArgs);
    }

    public long insert(String tableName, ContentValues values){
        return mDb.insert(tableName, null, values);
    }

    public Cursor query(String tableName, String[] columns,
                        String selection, String[] selectionArgs, String sortOrder ){
        //tai sao can khai bao cursor la final ???
        Cursor cursor = mDb.query(tableName,columns,selection,selectionArgs,null,null,sortOrder);
        return cursor;
    }

    public Cursor query(String tableName, String[] columns,
                        String selection, String[] selectionArgs, String sortOrder, String limit ){
        //tai sao can khai bao cursor la final ???
        Cursor cursor = mDb.query(tableName,columns,selection,selectionArgs,
                null,null,sortOrder, limit);
        return cursor;
    }

    public Cursor query(String tableName, String[] columns,
                        String selection, String[] selectionArgs, String groupBy,
                        String having, String orderBy, String limit) {

        return mDb.query(tableName, columns, selection,
                selectionArgs, groupBy, having, orderBy, limit);
    }

    public int update(String tableName, ContentValues values,
                      String selection, String[] selectionArgs){
        return mDb.update(tableName, values, selection, selectionArgs);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return mDb.rawQuery(sql, selectionArgs);
    }


}
