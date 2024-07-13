package com.example.YoYoer.Entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class CRUD {
    SQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase sqLiteDatabase;

    private static final String[] columns = {
            TrickDatabase.ID,
            TrickDatabase.CONTENT,
            TrickDatabase.TIME,
            TrickDatabase.MODE
    };

    public CRUD(Context context) {
        sqLiteOpenHelper = new TrickDatabase(context);
    }

    public void open() {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close() {
        sqLiteOpenHelper.close();
    }

    public Trick getItem(long id) {
        Cursor cursor = sqLiteDatabase.query(TrickDatabase.TABLE_NAME, columns, TrickDatabase.ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();
        Trick trick = new Trick(cursor.getString(1), cursor.getString(2), cursor.getInt(3));
        return trick;
    }

    public List<Trick> getItemList() {
        Cursor cursor = sqLiteDatabase.query(TrickDatabase.TABLE_NAME, columns, null, null, null, null, null);
        List<Trick> tricks = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Trick trick = new Trick();
                trick.setId(cursor.getLong(cursor.getColumnIndexOrThrow(TrickDatabase.ID)));
                trick.setContent(cursor.getString(cursor.getColumnIndexOrThrow(TrickDatabase.CONTENT)));
                trick.setTime(cursor.getString(cursor.getColumnIndexOrThrow(TrickDatabase.TIME)));
                trick.setTag(cursor.getInt(cursor.getColumnIndexOrThrow(TrickDatabase.MODE)));
                tricks.add(trick);
            }
        }
        return tricks;
    }

    public Trick addItem(Trick trick) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TrickDatabase.CONTENT, trick.getContent());
        contentValues.put(TrickDatabase.TIME, trick.getTime());
        contentValues.put(TrickDatabase.MODE, trick.getTag());
        long insertID = sqLiteDatabase.insert(TrickDatabase.TABLE_NAME, null, contentValues);
        trick.setId(insertID);
        return trick;
    }

    public int updateItem(Trick trick) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TrickDatabase.CONTENT, trick.getContent());
        contentValues.put(TrickDatabase.TIME, trick.getTime());
        contentValues.put(TrickDatabase.MODE, trick.getTag());

        return sqLiteDatabase.update(TrickDatabase.TABLE_NAME, contentValues,
                TrickDatabase.ID + "=?", new String[] { String.valueOf(trick.getId())});
    }

    public void removeItem(Trick trick) {
        sqLiteDatabase.delete(TrickDatabase.TABLE_NAME, TrickDatabase.ID + "=" + trick.getId(), null);
    }

    public void clear() {
        sqLiteDatabase.delete(TrickDatabase.TABLE_NAME, null, null);
        sqLiteDatabase.execSQL("update sqlite_sequence set seq=0 where name='tricks'");
    }
}
